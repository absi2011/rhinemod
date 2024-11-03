package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import rhinemod.monsters.JesseltonWilliams;

public class JesseltonUpdateHitboxAction extends AbstractGameAction {
    JesseltonWilliams Jesselton;
    public JesseltonUpdateHitboxAction(JesseltonWilliams Jesselton) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.Jesselton = Jesselton;
    }

    @Override
    public void update() {
        Jesselton.updateHitbox();
        isDone = true;
    }
}
