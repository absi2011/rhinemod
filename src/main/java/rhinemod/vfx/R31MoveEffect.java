package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class R31MoveEffect extends AbstractGameEffect {
    private final AbstractMonster m;
    private final float startX;
    private final float decX;

    public R31MoveEffect(AbstractMonster m, float decX) {
        startingDuration = duration = 2.6F;
        this.m = m;
        startX = m.drawX;
        this.decX = decX;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        m.drawX = startX - decX * (startingDuration - duration) / startingDuration;
        if (duration < 0.0F) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {}

    @Override
    public void dispose() {}
}
