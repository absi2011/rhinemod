package rhinemod.actions;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CtBehavior;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.characters.RhineLab;

import java.util.*;

public class SuperficialRegulationAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rhinemod:SelectScreenText");
    public static final String[] TEXT = uiStrings.TEXT;
    public static boolean[] exist = new boolean[4];
    public boolean isSelected = false;
    public ArrayList<AbstractCard> cardToDraw;

    public static HashMap<Integer, Integer> rank;
    public final AbstractPlayer p;
    public SuperficialRegulationAction() {
        actionType = ActionType.SPECIAL;
        if (Settings.FAST_MODE) {
            startDuration = duration = Settings.ACTION_DUR_XFAST;
        } else {
            startDuration = duration = Settings.ACTION_DUR_FASTER;
        }
        p = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding() || p.drawPile.group.isEmpty()) {
            isDone = true;
            return;
        }
        if (duration == startDuration) {
            for (int i = 0; i < 4; i++) exist[i] = false;
            for (AbstractCard c : p.drawPile.group)
                if (c instanceof AbstractRhineCard && ((AbstractRhineCard) c).realBranch > 0)
                    exist[((AbstractRhineCard) c).realBranch] = true;
                else
                    exist[0] = true;
            AbstractDungeon.gridSelectScreen.open(p.drawPile, p.drawPile.group.size(), true, TEXT[0]);
            AbstractDungeon.gridSelectScreen.confirmButton.isDisabled = true;
            tickDuration();
        }

        if (!isSelected) {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                cardToDraw = AbstractDungeon.gridSelectScreen.selectedCards;
                ArrayList<Integer> branchRank = new ArrayList<>();
                for (int i = 0; i < 4; i++) branchRank.add(i);
                Collections.shuffle(branchRank, new Random(AbstractDungeon.miscRng.randomLong()));
                isSelected = true;
                rank = new HashMap<>();
                for (int i = 0; i < 4; i++) rank.put(branchRank.get(i), i);
                BranchComparator cmp = new BranchComparator();
                p.drawPile.group.sort(cmp);
                cardToDraw.sort(cmp);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                if (cardToDraw.isEmpty()) isDone = true;
            }
            return;
        }
        if (p.hasPower("No Draw")) {
            p.getPower("No Draw").flash();
            isDone = true;
            return;
        } else if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            p.createHandIsFullDialog();
            isDone = true;
            return;
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (!cardToDraw.isEmpty() && duration < 0.0F) {
            duration = startDuration;
            ((RhineLab) p).draw(cardToDraw.get(0));
            cardToDraw.remove(0);
        }
        if (cardToDraw.isEmpty()) isDone = true;
    }

    public static class BranchComparator implements Comparator<AbstractCard> {
        public BranchComparator() {}
        public int compare(AbstractCard c1, AbstractCard c2) {
            return rank.get(getBranch(c2)) - rank.get(getBranch(c1));
        }
        private int getBranch(AbstractCard c) {
            if (c instanceof AbstractRhineCard) return ((AbstractRhineCard) c).realBranch;
            else return 0;
        }
    }
    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class AddCardPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GridCardSelectScreen __instance, String ___tipMsg) {
            if (___tipMsg.equals(TEXT[0])) {
                int[] cnt = new int[4];
                for (int i = 0; i < 4; i++) cnt[i] = 0;
                for (AbstractCard c : __instance.selectedCards)
                    if (c instanceof AbstractRhineCard && ((AbstractRhineCard) c).realBranch > 0)
                        cnt[((AbstractRhineCard) c).realBranch]++;
                    else
                        cnt[0]++;
                __instance.confirmButton.isDisabled = false;
                for (int i = 0; i < 4; i++)
                    if (exist[i] && cnt[i] != 1) {
                        __instance.confirmButton.isDisabled = true;
                        break;
                    }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(GridCardSelectScreen.class, "numCards");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class RemoveCardPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GridCardSelectScreen __instance, String ___tipMsg, AbstractCard ___hoveredCard) {
            if (___tipMsg.equals(TEXT[0])) {
                int[] cnt = new int[4];
                for (int i = 0; i < 4; i++) cnt[i] = 0;
                for (AbstractCard c : __instance.selectedCards)
                    if (c instanceof AbstractRhineCard && ((AbstractRhineCard) c).realBranch > 0)
                        cnt[((AbstractRhineCard) c).realBranch]++;
                    else
                        cnt[0]++;
                if (___hoveredCard instanceof AbstractRhineCard && ((AbstractRhineCard) ___hoveredCard).realBranch > 0)
                    cnt[((AbstractRhineCard) ___hoveredCard).realBranch]++;
                else
                    cnt[0]++;
                __instance.confirmButton.isDisabled = false;
                for (int i = 0; i < 4; i++)
                    if (exist[i] && cnt[i] != 1) {
                        __instance.confirmButton.isDisabled = true;
                        break;
                    }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "remove");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }
}
