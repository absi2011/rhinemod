package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

// Almost copied from ShadowOutEffect
public class FlashbangEffect extends AbstractGameEffect {
    private final float toHalfBlack;
    private final float toBlack;
    private final float toWhite;
    private final boolean hideCards;

    public FlashbangEffect() {
        duration = 0.8F;
        toHalfBlack = 0.15F;
        toBlack = 0.25F;
        toWhite = 0.1F;
        startingDuration = duration;
        color = new Color(255, 255, 255, 0);
        hideCards = Settings.hideCards;
        Settings.hideCards = true;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration > startingDuration - toHalfBlack) {
            color.a = (startingDuration - duration) / toHalfBlack * 0.5F;
        } else if (duration > startingDuration - toHalfBlack - toBlack) {
            color.a = (startingDuration - toHalfBlack - duration) / toBlack * 0.5F + 0.5F;
        } else if (duration < toWhite) {
            color.a = duration / toWhite;
        } else {
            color.a = 1;
        }
        if (duration < 0.0F) {
            isDone = true;
            Settings.hideCards = hideCards;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture pixelTexture = new Texture(pixmap);
        pixmap.dispose();
        sb.draw(pixelTexture, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }

    @Override
    public void dispose() {}
}