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

public class InvisibleSmokeBlurEffect extends AbstractGameEffect {
    private final float startX;
    private final float startY;
    private final float targetX;
    private final float targetY;
    private float x;
    private float y;
    private final float aV;
    private final float targetScale;
    private final TextureAtlas.AtlasRegion img;
    public boolean disposing;

    public InvisibleSmokeBlurEffect(float x, float y) {
        float gray = MathUtils.random(0.4F, 0.6F);
        color = new Color(gray, gray, gray, 0.4F);
        duration = startingDuration = 0.5F;
        scale = 0.01F;
        if (MathUtils.randomBoolean()) {
            img = ImageMaster.EXHAUST_L;
            targetScale = MathUtils.random(0.8F, 2.2F);
        } else {
            img = ImageMaster.EXHAUST_S;
            targetScale = MathUtils.random(0.8F, 1.2F);
        }
        startX = this.x = x - img.packedWidth / 2.0F;
        startY = this.y = y - img.packedHeight / 2.0F;
        targetX = this.x + MathUtils.random(-100.0F * Settings.scale, 100.0F * Settings.scale);
        targetY = this.y + MathUtils.random(-120.0F * Settings.scale, 120.0F * Settings.scale);
        rotation = MathUtils.random(360.0F);
        aV = MathUtils.random(-250.0F, 250.0F);
        disposing = false;
    }

    @Override
    public void update() {
        rotation += this.aV * Gdx.graphics.getDeltaTime();
        if (!disposing) {
            if (duration > 0.0F) {
                duration -= Gdx.graphics.getDeltaTime();
                if (duration < 0.0F) return;
                x = targetX + (startX - targetX) * duration / startingDuration;
                y = targetY + (startY - targetY) * duration / startingDuration;
                scale = Interpolation.exp10Out.apply(0.01F, targetScale, 1.0F - duration / startingDuration);
            }
        } else {
            duration += Gdx.graphics.getDeltaTime();
            if (duration < startingDuration) {
                color.a = 0.4F * (1 - duration / startingDuration);
            } else {
                isDone = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(img, x, y, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, scale, scale, rotation);
    }

    @Override
    public void dispose() {}
}
