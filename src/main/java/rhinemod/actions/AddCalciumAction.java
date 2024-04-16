package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.characters.RhineLab;
import rhinemod.powers.InvisibleCalcium;
import rhinemod.powers.LikeMindPower;

public class AddCalciumAction extends AbstractGameAction {
    public AddCalciumAction(int amount) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p instanceof RhineLab)
            ((RhineLab)AbstractDungeon.player).globalAttributes.addCalcium(amount);
        if (amount > 0 && p.hasPower(LikeMindPower.POWER_ID))
            p.getPower(LikeMindPower.POWER_ID).onApplyPower(new InvisibleCalcium(), p, p);
        isDone = true;
    }
}
