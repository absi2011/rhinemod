package rhinemod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import rhinemod.monsters.SleepingR31;

public class WaitAndAwakeAction extends AbstractGameAction {
    SleepingR31 r31;
    public WaitAndAwakeAction(SleepingR31 r31) {
        actionType = ActionType.SPECIAL;
        this.r31 = r31;
        duration = Settings.ACTION_DUR_LONG;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0) {
            r31.Awaken();
            isDone = true;
        }
    }
}
