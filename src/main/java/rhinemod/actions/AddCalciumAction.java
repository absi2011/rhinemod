package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.characters.RhineLab;

public class AddCalciumAction extends AbstractGameAction {
    public AddCalciumAction(int amount) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player instanceof RhineLab)
            ((RhineLab)AbstractDungeon.player).globalAttributes.addCalcium(amount);
        isDone = true;
    }
}
