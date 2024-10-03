package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.characters.RhineLab;
import rhinemod.powers.SolidifyPower;

public class ReduceCalciumAction extends AbstractGameAction {
    public ReduceCalciumAction(int amount) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (!AbstractDungeon.player.hasPower(SolidifyPower.POWER_ID)) {
            if ((AbstractDungeon.player instanceof RhineLab)) {
                ((RhineLab) AbstractDungeon.player).globalAttributes.calciumNum -= amount;
                if (((RhineLab) AbstractDungeon.player).globalAttributes.calciumNum < 0) {
                    ((RhineLab) AbstractDungeon.player).globalAttributes.calciumNum = 0;
                }
            }
        } else {
            AbstractDungeon.player.getPower(SolidifyPower.POWER_ID).flash();
        }
        isDone = true;
    }
}
