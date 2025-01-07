package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class MyLaserEffect extends AbstractGameEffect {
    private final float sX;
    private final float sY;
    private final float dX;
    private final float dY;
    private float x;
    private float y;
    private int cnt;
    private final float dst;
    private final Color[] colors = new Color[]{Color.RED.cpy(), Color.BLUE.cpy(), Color.GREEN.cpy()};
    private static TextureAtlas.AtlasRegion img;

    public MyLaserEffect(float sX, float sY, float dX, float dY, int cnt) {
        this.sX = sX;
        this.sY = sY;
        this.dX = dX;
        this.dY = dY;
        this.cnt = cnt;
        dst = Vector2.dst(sX, sY, dX, dY) / 2.0F * Settings.scale;
        rotation = MathUtils.atan2(dX - sX, dY - sY);
        rotation *= 57.295776F;
        rotation = -rotation + 90.0F;
        if (img == null) {
            img = ImageMaster.vfxAtlas.findRegion("combat/laserThin");
        }
        this.duration = this.startingDuration = 0.1F;
        color = colors[0];
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        float prop = duration / startingDuration / 2.0F + 0.5F;
        x = sX * prop + dX * (1 - prop);
        y = sY * prop + dY * (1 - prop);
        if (duration < 0) {
            cnt--;
            if (cnt < 0) {
                isDone = true;
            } else {
                duration = startingDuration;
                color = colors[cnt % 3];
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(color);
        sb.draw(img, x, y - img.packedHeight / 2.0F, 0.0F, img.packedHeight / 2.0F, dst, 50.0F, scale, scale, rotation);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {}
}
