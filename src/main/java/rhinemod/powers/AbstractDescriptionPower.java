package rhinemod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AbstractDescriptionPower extends AbstractPower {
    public final String superScript;
    public AbstractDescriptionPower(String superScript) {
        this.superScript = superScript;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/PlanetaryDebris 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/PlanetaryDebris 32.png"), 0, 0, 32, 32);
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, superScript, x, y, this.fontScale, c);
    }
}
