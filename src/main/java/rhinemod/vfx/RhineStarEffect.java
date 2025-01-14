package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

// Almost copied from SilentVictoryStarEffect
public class RhineStarEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private final float vX;
    private final float vY;
    private final TextureAtlas.AtlasRegion img;

    public RhineStarEffect(boolean onVictoryScreen) {
        img = ImageMaster.ROOM_SHINE_1;
        rotation = MathUtils.random(-5.0F, 5.0F);
        x = MathUtils.random(-100.0F, 1870.0F) * Settings.xScale - img.packedWidth / 2.0F;
        if (onVictoryScreen) {
            duration = MathUtils.random(10.0F, 20.0F);
            float h = MathUtils.random(0.15F, 0.9F);
            y = Settings.HEIGHT * h;
            vX = MathUtils.random(12.0F, 20.0F) * Settings.scale;
            scale = h * MathUtils.random(1.5F, 1.8F) * Settings.scale;
        } else {
            duration = 2.0F;
            float h = MathUtils.random(0.1F, 0.8F);
            y = Settings.HEIGHT * h;
            vX = MathUtils.random(150.0F, 155.0F) * Settings.scale;
            scale = (0.5F - Math.abs(h - 0.45F)) * 2.0F * MathUtils.random(1.5F, 1.8F) * Settings.scale;
        }
        startingDuration = duration;
        renderBehind = onVictoryScreen;
        vY = MathUtils.random(-5.0F, 5.0F) * Settings.scale;
        color = new Color(MathUtils.random(0.95F, 1.0F), MathUtils.random(0.8F, 1.0F), MathUtils.random(0.55F, 0.6F), 0.0F);
    }

    public void update() {
        y += vY * Gdx.graphics.getDeltaTime();
        x += vX * Gdx.graphics.getDeltaTime();
        if (duration > startingDuration / 2.0F) {
            color.a = Interpolation.fade.apply(0.9F, 0.0F, (duration - startingDuration / 2.0F) / (startingDuration / 2.0F));
        } else {
            color.a = Interpolation.fade.apply(0.0F, 0.9F, duration / (startingDuration / 2.0F));
        }

        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0F) {
            isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(img, x, y, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, scale * MathUtils.random(0.9F, 1.1F), scale * MathUtils.random(0.8F, 1.3F), rotation);
    }

    public void dispose() {}
}
