package rhinemod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rhinemod.cards.AbstractRhineCard;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

public class GridCardSelectPatch {

    @SpirePatch(clz = GridCardSelectScreen.class, method = SpirePatch.CLASS)
    public static class OptFields {
        public static SpireField<Boolean> selectingBranch = new SpireField<>(() -> false);
    }
    static public final int MAX_BRANCH = 4;
    public static AbstractCard[] branches = new AbstractCard[MAX_BRANCH];
    public static Hitbox[] hitboxes = new Hitbox[MAX_BRANCH];
    public static int branchesNum;
    public static boolean cardChecked;

    public static boolean IsSelectingBranch() {
        return OptFields.selectingBranch.get(AbstractDungeon.gridSelectScreen);
    }

    public static AbstractCard GetHoveredCard() {
        try {
            Field card = GridCardSelectScreen.class.getDeclaredField("hoveredCard");
            card.setAccessible(true);
            return (AbstractCard) card.get(AbstractDungeon.gridSelectScreen);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setBranchesPreview(GridCardSelectScreen _inst, AbstractRhineCard card) {
        if (!cardChecked) {
            if (card.upgraded) {
                branchesNum = card.possibleBranches().size() - 1;
                int afterChosen = 0;
                for (int i = 0; i < branchesNum + 1; i++) {
                    if (i == card.chosenBranch) {
                        afterChosen = 1;
                        continue;
                    }
                    AbstractRhineCard preview = card.makeStatEquivalentCopy();
                    preview.swapBranch(i);
                    preview.displayUpgrades();
                    branches[i - afterChosen] = preview;
                    hitboxes[i - afterChosen] = new Hitbox(0, 0);
                }
            } else {
                branchesNum = card.possibleBranches().size();
                for (int i = 0; i < branchesNum; i++) {
                    AbstractRhineCard preview = card.makeStatEquivalentCopy();
                    preview.chosenBranch = i;
                    preview.possibleBranches().get(i).upgrade();
                    preview.displayUpgrades();
                    branches[i] = preview;
                    hitboxes[i] = new Hitbox(0, 0);
                }
            }
            cardChecked = true;
        }
        OptFields.selectingBranch.set(_inst, true);
        updateBranchPreview();
    }

    public static void updateBranchPreview() {
        branches[0].drawScale = 1F;
        branches[0].current_x = Settings.WIDTH * 0.64F;
        branches[0].current_y = Settings.HEIGHT / 2F;
        branches[0].target_x = Settings.WIDTH * 0.64F;
        branches[0].target_y = Settings.HEIGHT / 2F;
        branches[0].transparency = 1F;
        for (int i = 1; i < branchesNum; i++) {
            branches[i].drawScale = 0.75F;
            branches[i].current_x = Settings.WIDTH * (0.79F + (i == 3? 0.13F : 0.0F));
            branches[i].current_y = Settings.HEIGHT * (0.65F - (i - 1) % 2 * 0.32F);
            branches[i].target_x = Settings.WIDTH * (0.79F + (i == 3? 0.13F : 0.0F));
            branches[i].target_y = Settings.HEIGHT * (0.65F - (i - 1) % 2 * 0.32F);
            branches[i].transparency = 1F;
            branches[i].targetTransparency = 1F;
            hitboxes[i].resize(300.0F * Settings.scale * 0.75F, 420.0F * Settings.scale * 0.75F);
            hitboxes[i].move(branches[i].current_x, branches[i].current_y);
        }
    }

    public static boolean isCardHasBranches() {
        AbstractCard card = GetHoveredCard();
        if (!(card instanceof AbstractRhineCard)) return false;
        return ((AbstractRhineCard) card).possibleBranches().size() > 1;
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class CheckIfCardHasBranches {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GridCardSelectScreen _inst) {
            if (isCardHasBranches()) {
                setBranchesPreview(_inst, (AbstractRhineCard) GetHoveredCard());
            } else {
                branchesNum = 0;
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractCard.class,
                        "makeStatEquivalentCopy");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class UpdateInputButtons {
        @SpireInsertPatch(rloc = 199)
        public static void Insert(GridCardSelectScreen _inst) {
            if (IsSelectingBranch()) {
                for (int i = 1; i < branchesNum; i++) {
                    Hitbox hb = hitboxes[i];
                    hb.update();
                    if (hb.justHovered) {
                        CardCrawlGame.sound.play("UI_HOVER");
                    }
                    if (hb.hovered && InputHelper.justClickedLeft) {
                        hb.clickStarted = true;
                        CardCrawlGame.sound.play("UI_CLICK_1");
                    }
                    if (hb.clicked) {
                        hb.clicked = false;
                        Collections.swap(Arrays.asList(branches), 0, i);
                        updateBranchPreview();
                    }
                }
            }
        }
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "render")
    public static class RenderUpgradeBranches {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(GridCardSelectScreen.class.getName()) && m.getMethodName().equals("renderArrows")) {
                        m.replace("$_ = $proceed($$); " +
                                "if (" + GridCardSelectPatch.class.getName() + ".RenderingBranches(this, sb)) {return;}");
                    }
                }
            };
        }
    }

    public static boolean RenderingBranches(GridCardSelectScreen _inst, SpriteBatch sb) {
        if (!OptFields.selectingBranch.get(_inst)) return false;
        AbstractCard card = GetHoveredCard();
        if (card == null) return false;
        card.current_x = Settings.WIDTH * 0.35F;
        card.current_y = Settings.HEIGHT / 2F;
        card.target_x = Settings.WIDTH * 0.35F;
        card.target_y = Settings.HEIGHT / 2F;
        card.drawScale = 1F;
        card.render(sb);
        card.updateHoverLogic();
        branches[0].beginGlowing();
        for (int i = 0; i < branchesNum; i++) {
            if (i != 0) branches[i].stopGlowing();
            branches[i].render(sb);
            branches[i].updateHoverLogic();
            branches[i].renderCardTip(sb);
        }
        if (!PeekButton.isPeeking && (_inst.forUpgrade || _inst.forTransform || _inst.forPurge
                || _inst.isJustForConfirming || _inst.anyNumber)) {
            _inst.confirmButton.render(sb);
        }
        _inst.peekButton.render(sb);
        CardGroup targetGroup = ReflectionHacks.getPrivate(_inst, GridCardSelectScreen.class, "targetGroup");
        String tipMsg = ReflectionHacks.getPrivate(_inst, GridCardSelectScreen.class, "tipMsg");
        if ((!_inst.isJustForConfirming || targetGroup.size() > 5) && !PeekButton.isPeeking) {
            FontHelper.renderDeckViewTip(sb, tipMsg, 96F * Settings.scale, Settings.CREAM_COLOR);
        }
        return true;
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "open", paramtypez = {CardGroup.class, int.class, String.class, boolean.class, boolean.class, boolean.class, boolean.class})
    public static class OpenCleanPatch {
        @SpirePrefixPatch
        public static void Prefix(GridCardSelectScreen _inst, CardGroup group, int numCards, String tipMsg, boolean forUpgrade, boolean forTransform, boolean canCancel, boolean forPurge) {
            OptFields.selectingBranch.set(_inst, false);
            cardChecked = false;
            branchesNum = 0;
        }
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "cancelUpgrade")
    public static class CancelUpgradeCheck {
        @SpirePrefixPatch
        public static void Prefix(GridCardSelectScreen _inst) {
            OptFields.selectingBranch.set(_inst, false);
            cardChecked = false;
            branchesNum = 0;
        }
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class ConfirmBranchingCheck {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GridCardSelectScreen _inst) {
            AbstractCard card = GetHoveredCard();
            if (IsSelectingBranch()) {
                Logger.getLogger(GridCardSelectPatch.class.getName()).info("final select branch:" + ((AbstractRhineCard) branches[0]).chosenBranch);
                if (!(card instanceof AbstractRhineCard)) return;
                ((AbstractRhineCard) card).chosenBranch = ((AbstractRhineCard) branches[0]).chosenBranch;
                OptFields.selectingBranch.set(_inst, false);
                cardChecked = false;
                branchesNum = 0;
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class,
                        "closeCurrentScreen");
                int[] totalTimes = LineFinder.findAllInOrder(ctMethodToPatch, matcher);
                int lastTime = totalTimes.length - 1;
                return new int[] {totalTimes[lastTime]};
            }
        }
    }
}
