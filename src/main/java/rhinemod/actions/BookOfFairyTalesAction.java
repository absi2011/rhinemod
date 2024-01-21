package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BookOfFairyTalesAction extends AbstractGameAction {
    public BookOfFairyTalesAction() {
        actionType = ActionType.DRAW;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        for (AbstractCard c : DrawCardAction.drawnCards)
            if (AbstractDungeon.cardRng.random(0, 4) == 0) {
                c.upgrade();
                c.displayUpgrades();
                c.applyPowers();
            }
        isDone = true;
    }
}
