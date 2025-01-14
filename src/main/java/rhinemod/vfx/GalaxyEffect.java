package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GalaxyEffect extends AbstractGameEffect {
    private final ArrayList<AbstractGameEffect> starEffect = new ArrayList<>();
    public GalaxyEffect() {
        duration = startingDuration = 2.0F;
        color = new Color(0, 0, 0, 0);
    }

    @Override
    public void update() {
        if (duration == startingDuration) {
            for (int i = 0; i < 100; i++) {
                starEffect.add(new RhineStarEffect(false));
            }
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (duration > 1.5F) {
            color.a = Interpolation.pow5In.apply(0.5F, 0.0F, (duration - 1.5F) / 0.5F);
        } else if (duration < 0.5F) {
            color.a = Interpolation.exp10In.apply(0.0F, 0.5F, this.duration / 0.5F);
        } else {
            color.a = 0.5F;
        }
        if (duration < 0.0) {
            isDone = true;
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
        sb.setColor(Color.WHITE);
        Logger.getLogger(GalaxyEffect.class.getName()).info("starEffect.size = " + starEffect.size());
        for (AbstractGameEffect e : starEffect) {
            e.render(sb);
            e.update();
        }
        starEffect.removeIf(e -> e.isDone);
    }

    @Override
    public void dispose() {}
}
