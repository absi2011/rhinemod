package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class R11MoveEffect extends AbstractGameEffect {
    private final AbstractMonster m;
    private final float startX;
    private final float decX;
    private final float stage2;

    public R11MoveEffect(AbstractMonster m, float decX) {
        float stage1 = 2.0F;
        stage2 = 2.6F;
        duration = stage1 + stage2;
        this.m = m;
        startX = m.drawX;
        this.decX = decX;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < stage2) {
            m.drawX = startX - decX * (stage2 - duration) / stage2;
        }
        if (duration < 0.0F) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {}

    @Override
    public void dispose() {}
}
