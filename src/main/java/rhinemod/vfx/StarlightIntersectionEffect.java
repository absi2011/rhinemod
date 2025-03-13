package rhinemod.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class StarlightIntersectionEffect extends AbstractGameEffect {
    public static final int width = 522;
    public static final int height = 700;
    public static final int center_height = 175;
    public static final int delta_height = 50;
    public static final int texture_cnt = 25;
    public static final ArrayList<TextureAtlas.AtlasRegion> textures = new ArrayList<>();

    private final float centerX;
    private final float centerY;
    private int step;
    private final int speed;
    public StarlightIntersectionEffect(float x, float y) {
        scale = Settings.scale * 1.2F;
        centerX = x;
        centerY = y;
        step = -1;
        if (Settings.FAST_MODE) {
            speed = 2;
        } else {
            speed = 3;
        }
    }

    @Override
    public void update() {
        step++;
        if (step >= texture_cnt * speed) isDone = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(textures.get(step / speed), centerX - width / 2.0F, centerY - (height / 2.0F - center_height - delta_height), width / 2.0F, height / 2.0F - center_height, width, height, scale, scale, 0.0F);
    }

    @Override
    public void dispose() {}
}
