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
    private final float dst;
    private static TextureAtlas.AtlasRegion img;

    public MyLaserEffect(float sX, float sY, float dX, float dY, int type) {
        this.sX = sX;
        this.sY = sY;
        this.dX = dX;
        this.dY = dY;
        dst = Vector2.dst(sX, sY, dX, dY) / 2.0F / Settings.scale;
        rotation = MathUtils.atan2(dX - sX, dY - sY);
        rotation *= 57.295776F;
        rotation = -rotation + 90.0F;
        if (img == null) {
            img = ImageMaster.vfxAtlas.findRegion("combat/laserThin");
        }
        this.duration = this.startingDuration = 0.3F;
        if (type == 0) color = Color.RED.cpy();
        else if (type == 1) color = Color.BLUE.cpy();
        else color = Color.GREEN.cpy();
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        float prop = duration / startingDuration / 2.0F + 0.5F;
        x = sX * prop + dX * (1 - prop);
        y = sY * prop + dY * (1 - prop);
        if (duration < 0) {
            isDone = true;
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
