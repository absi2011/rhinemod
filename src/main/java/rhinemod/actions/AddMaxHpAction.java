package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import rhinemod.RhineMod;

public class AddMaxHpAction extends AbstractGameAction {
    public AddMaxHpAction(AbstractCreature target) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.target = target;
    }

    @Override
    public void update() {
        target.maxHealth = (int) (target.maxHealth * (1.0F + 0.1F * RhineMod.tagLevel));
        target.currentHealth = target.maxHealth;
        isDone = true;
    }
}
