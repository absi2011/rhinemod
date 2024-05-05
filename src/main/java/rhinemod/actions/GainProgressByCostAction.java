package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import rhinemod.powers.ResearchProgress;

public class GainProgressByCostAction extends AbstractGameAction {
    public final AbstractPlayer p;
    public GainProgressByCostAction(AbstractPlayer p) {
        actionType = ActionType.POWER;
        duration = Settings.ACTION_DUR_XFAST;
        this.p = p;
    }

    @Override
    public void update() {
        int sum = 0;
        for (AbstractCard c : DrawCardAction.drawnCards)
        {
            if (c.cost >= 0)
            {
                sum += c.cost;
            }
        }
        if (sum > 0) {
            addToTop(new ApplyPowerAction(p, p, new ResearchProgress(p, sum)));
        }
        isDone = true;
    }
}
