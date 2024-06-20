package rhinemod.actions;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.characters.RhineLab;

import java.util.*;

public class SuperficialRegulationAction_DUPLICATED extends AbstractGameAction {
    public boolean isSelected = false;
    public final ArrayList<AbstractCard> cardToDraw = new ArrayList<>();
    public static HashMap<Integer, Integer> rank;
    public final AbstractPlayer p;
    public SuperficialRegulationAction_DUPLICATED() {
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
        if (!(p instanceof RhineLab)) {
            addToTop(new DrawCardAction(1));
            isDone = true;
            return;
        }
        if (!isSelected) {
            ArrayList<ArrayList<AbstractCard> > list = new ArrayList<>();
            ArrayList<Integer> branchRank = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                list.add(new ArrayList<>());
                branchRank.add(i);
            }
            for (AbstractCard c : p.drawPile.group)
                if (c instanceof AbstractRhineCard)
                    list.get(((AbstractRhineCard) c).realBranch).add(c);
                else
                    list.get(0).add(c);
            Collections.shuffle(branchRank, new Random(AbstractDungeon.miscRng.randomLong()));
            for (int i = 0; i < 4; i++) {
                int rk = branchRank.get(i);
                if (!list.get(rk).isEmpty())
                    cardToDraw.add(list.get(rk).get(AbstractDungeon.cardRng.random(list.get(rk).size() - 1)));
            }
            isSelected = true;
            rank = new HashMap<>();
            for (int i = 0; i < 4; i++) rank.put(branchRank.get(i), i);
            p.drawPile.group.sort(new BranchComparator());
            if (cardToDraw.isEmpty()) isDone = true;
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
}
