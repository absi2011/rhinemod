package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.characters.RhineLab;
import rhinemod.powers.InvisibleFlowShape;
import rhinemod.powers.LikeMindPower;

public class AddFlowingShapeAction extends AbstractGameAction {
    public AddFlowingShapeAction(int amount) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p instanceof RhineLab)
            ((RhineLab)AbstractDungeon.player).globalAttributes.addFlowsp(amount);
        if (p.hasPower(LikeMindPower.POWER_ID))
            p.getPower(LikeMindPower.POWER_ID).onApplyPower(new InvisibleFlowShape(), p, p);
        isDone = true;
    }
}
