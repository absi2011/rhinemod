package rhinemod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import rhinemod.monsters.Dorothy;

public class DelayRemoveAction extends AbstractGameAction {
    public final Dorothy m;
    public boolean fadeout;
    public DelayRemoveAction(Dorothy m) {
        actionType = ActionType.SPECIAL;
        duration = 2.5f;
        this.m = m;
        fadeout = false;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 1.0f && !fadeout) {
            m.tint.fadeOut();
            fadeout = true;
        }
        if (duration < 0.0f) {
            m.realRemove();
            isDone = true;
        }
    }
}
