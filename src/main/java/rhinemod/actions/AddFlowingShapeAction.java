package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.characters.RhineLab;

public class AddFlowingShapeAction extends AbstractGameAction {
    public AddFlowingShapeAction(int amount) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
    }

    @Override
    public void update() {
        ((RhineLab)AbstractDungeon.player).globalAttributes.addFlowsp(amount);
        isDone = true;
    }
}
