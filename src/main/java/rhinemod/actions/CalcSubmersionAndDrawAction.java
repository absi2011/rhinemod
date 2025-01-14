package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.powers.Submersion;

public class CalcSubmersionAndDrawAction extends AbstractGameAction {
    public CalcSubmersionAndDrawAction() {
        actionType = ActionType.DRAW;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            int cnt = 0;
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters)
                if (!m.isDeadOrEscaped() && m.hasPower(Submersion.POWER_ID))
                    cnt += m.getPower(Submersion.POWER_ID).amount;
            if (cnt > 0) {
                addToTop(new DrawCardAction(cnt));
            }
        }
        tickDuration();
    }
}
