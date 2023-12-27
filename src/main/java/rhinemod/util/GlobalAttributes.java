package rhinemod.util;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;

import java.util.ArrayList;

public class GlobalAttributes {
    public static final String UP_IMG = "images/ui/gravityUp.png";
    public static final String DOWN_IMG = "images/ui/gravityDown.png";
    public static final String NONE_IMG = "images/ui/gravityNone.png";
    public static final String CA_IMG = "images/ui/calcium.png";
    public enum GravityDirection {
        UP, DOWN, NONE
    }
    public GravityDirection gravity;
    public int calciumNum;
    public int calciumReduceNum;
    public final float gravityX;
    public final float gravityY;
    public final float calciumX;
    public final float calciumY;
    public Hitbox gravityHb;
    public Hitbox calciumHb;
    public float gravityFlash;
    public float calciumFlash;
    public float hbr;

    public GlobalAttributes() {
        gravity = GravityDirection.NONE;
        calciumReduceNum = 3;
        calciumNum = 0;
        gravityX = 410.0F * Settings.scale;
        gravityY = 840.0F * Settings.scale;
        calciumX = 540.0F * Settings.scale;
        calciumY = 840.0F * Settings.scale;
        hbr = 33.0F * Settings.scale;
        gravityHb = new Hitbox(gravityX - hbr, gravityY - hbr, hbr * 2, hbr * 2);
        calciumHb = new Hitbox(calciumX - hbr, calciumY - hbr, hbr * 2, hbr * 2);
    }

    public void changeGravity() {
        if (gravity == GravityDirection.UP) gravity = GravityDirection.DOWN;
        else gravity = GravityDirection.UP;
        gravityFlash = 2.0F;
        AbstractDungeon.onModifyPower();
    }

    public void addCalcium(int amount) {
        calciumNum += amount;
        calciumFlash = 2.0F;
        AbstractDungeon.onModifyPower();
    }

    public void renderSingleFlash(SpriteBatch sb, TextureRegion region, float x, float y, float scale) {
        float w = region.getRegionWidth();
        float h = region.getRegionHeight();
        sb.draw(region, x - w * scale * 0.5F, y - h * scale * 0.5F, 0, 0, w, h, scale, scale, 1.0F);
    }

    public void renderFlash(SpriteBatch sb, TextureRegion region, float x, float y, float timer) {
        float tmp = Interpolation.exp10In.apply(0.0F, 4.0F, timer / 2.0F);
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, timer * 0.2F));
        renderSingleFlash(sb, region, x, y, 1.0F + tmp);
        renderSingleFlash(sb, region, x, y, 1.0F + tmp * 0.66F);
        renderSingleFlash(sb, region, x, y, 1.0F + tmp / 3.0F);
        sb.setBlendFunction(770, 771);
    }

    public void renderGravity(SpriteBatch sb) {
        Texture img;
        if (gravity == GravityDirection.UP) img = new Texture(UP_IMG);
        else if (gravity == GravityDirection.DOWN) img = new Texture(DOWN_IMG);
        else img = new Texture(NONE_IMG);
        TextureRegion region = new TextureRegion(img, img.getWidth(), img.getHeight());
        sb.setColor(Color.WHITE);
        sb.draw(region, gravityX - region.getRegionWidth() * 0.5F, gravityY - region.getRegionWidth() * 0.5F, 0, 0, region.getRegionWidth(), region.getRegionHeight(), 1.0F, 1.0F, 1.0F);
        if (gravityFlash > 0.0F) {
            renderFlash(sb, region, gravityX, gravityY, gravityFlash);
            gravityFlash -= Gdx.graphics.getDeltaTime();
        }
    }

    public void renderCalcium(SpriteBatch sb) {
        Texture img = new Texture(CA_IMG);
        TextureRegion region = new TextureRegion(img, img.getWidth(), img.getHeight());
        sb.setColor(Color.WHITE);
        sb.draw(region, calciumX - region.getRegionWidth() * 0.5F, calciumY - region.getRegionWidth() * 0.5F, 0, 0, region.getRegionWidth(), region.getRegionHeight(), 1.0F, 1.0F, 1.0F);        sb.draw(img, calciumX - img.getWidth() * 0.5F, calciumY - img.getHeight() * 0.5F);
        FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, Integer.toString(calciumNum), calciumX + region.getRegionWidth() * 0.5F, calciumY - region.getRegionHeight() * 0.5F, Color.LIGHT_GRAY);
        if (calciumFlash > 0.0F) {
            renderFlash(sb, region, calciumX, calciumY, calciumFlash);
            calciumFlash -= Gdx.graphics.getDeltaTime();
        }
    }

    public void render(SpriteBatch sb) {
        renderGravity(sb);
        renderCalcium(sb);
        if (!AbstractDungeon.isScreenUp) {
            gravityHb.update();
            calciumHb.update();
            if (gravityHb.hovered) {
                ArrayList<PowerTip> tips = new ArrayList<>();
                if (gravity == GravityDirection.NONE)
                    tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:gravity")), BaseMod.getKeywordDescription("rhinemod:gravity")));
                else if (gravity == GravityDirection.UP)
                    tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:gravityup")), BaseMod.getKeywordDescription("rhinemod:gravityup")));
                else
                    tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:gravitydown")), BaseMod.getKeywordDescription("rhinemod:gravitydown")));
                TipHelper.queuePowerTips(gravityHb.cX + gravityHb.width / 2.0F, gravityHb.cY + TipHelper.calculateAdditionalOffset(tips, gravityHb.cY), tips);
            }
            if (calciumHb.hovered) {
                ArrayList<PowerTip> tips = new ArrayList<>();
                tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:calcium")), BaseMod.getKeywordDescription("rhinemod:calcium")));
                TipHelper.queuePowerTips(calciumHb.cX + calciumHb.width / 2.0F, calciumHb.cY + TipHelper.calculateAdditionalOffset(tips, calciumHb.cY), tips);
            }
        }
    }

    public void atStartOfCombat() {
        calciumNum = 0;
    }

    public void atStartOfTurn() {
        calciumNum -= calciumReduceNum;
        if (calciumNum < 0) calciumNum = 0;
        gravity = GravityDirection.NONE;
        AbstractDungeon.onModifyPower();
    }
}
