package rhinemod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import rhinemod.monsters.AbstractRhineMonster;

public class DelayDieAction extends AbstractGameAction {
    public final AbstractRhineMonster m;
    public DelayDieAction(AbstractRhineMonster m, float time) {
        actionType = ActionType.SPECIAL;
        duration = time;
        this.m = m;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0F) {
            m.realDie();
            isDone = true;
        }
    }
}
