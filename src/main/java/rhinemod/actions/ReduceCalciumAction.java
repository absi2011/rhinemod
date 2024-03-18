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
            if ((AbstractDungeon.player instanceof RhineLab))   // TODO: 这个不知道能不能给点兼容性
                ((RhineLab) AbstractDungeon.player).globalAttributes.addCalcium(amount);
        }
        isDone = true;
    }
}
