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
import rhinemod.actions.ReduceCalciumAction;

import java.util.ArrayList;

public class GlobalAttributes {
    public static final Texture UP_IMG = new Texture("resources/rhinemod/images/ui/gravityUp.png");
    public static final TextureRegion UP_REGION = new TextureRegion(UP_IMG, UP_IMG.getWidth(), UP_IMG.getHeight());
    public static final Texture DOWN_IMG = new Texture("resources/rhinemod/images/ui/gravityDown.png");
    public static final TextureRegion DOWN_REGION = new TextureRegion(DOWN_IMG, DOWN_IMG.getWidth(), DOWN_IMG.getHeight());
    public static final Texture NONE_IMG = new Texture("resources/rhinemod/images/ui/gravityNone.png");
    public static final TextureRegion NONE_REGION = new TextureRegion(NONE_IMG, NONE_IMG.getWidth(), NONE_IMG.getHeight());
    public static final Texture CA_IMG = new Texture("resources/rhinemod/images/ui/calcium.png");
    public static final TextureRegion CA_REGION = new TextureRegion(CA_IMG, CA_IMG.getWidth(), CA_IMG.getHeight());
    public static final Texture FS_IMG = new Texture("resources/rhinemod/images/ui/flowingShape.png");
    public static final TextureRegion FS_REGION = new TextureRegion(FS_IMG, FS_IMG.getWidth(), FS_IMG.getHeight());
    public enum GravityDirection {
        UP, DOWN, NONE, UNKNOWN
    }
    public int flowspNum;
    public GravityDirection gravity;
    public int calciumNum;
    public static int gravityChanges = 0;
    public static int calciumReduceNum;
    public static int smashThreshold;
    public float flowspX;
    public float flowspY;
    public float gravityX;
    public float gravityY;
    public float calciumX;
    public float calciumY;
    public Hitbox flowspHb;
    public Hitbox gravityHb;
    public Hitbox calciumHb;
    public float flowspFlash;
    public float gravityFlash;
    public float calciumFlash;
    public float hbr;

    public GlobalAttributes() {
        gravity = GravityDirection.NONE;
        calciumReduceNum = 1;
        smashThreshold = 15;
        calciumNum = 0;
        hbr = 33.0F * Settings.scale;
        flowspY = gravityY = calciumY = 840.0F * Settings.scale;
        gravityX = 480.0F * Settings.scale;
        updateHitbox();
    }

    public void updateHitbox() {
        flowspX = gravityX - 100.0F * Settings.scale;
        calciumX = gravityX + 100.0F * Settings.scale;
        flowspHb = new Hitbox(flowspX - hbr, flowspY - hbr, hbr * 2, hbr * 2);
        gravityHb = new Hitbox(gravityX - hbr, gravityY - hbr, hbr * 2, hbr * 2);
        calciumHb = new Hitbox(calciumX - hbr, calciumY - hbr, hbr * 2, hbr * 2);
    }

    public void addFlowsp(int amount) {
        flowspNum += amount;
        flowspFlash = 2.0F;
    }

    public void clearFlowsp() {
        flowspNum = 0;
        flowspFlash = 2.0F;
    }

    public void changeGravity(GravityDirection aimGravity) {
        if (aimGravity == GravityDirection.UNKNOWN) {
            if (gravity == GravityDirection.UP) aimGravity = GravityDirection.DOWN;
            else aimGravity = GravityDirection.UP;
        }
        if (aimGravity == gravity) return;
        gravity = aimGravity;
        gravityFlash = 2.0F;
        gravityChanges++;
        AbstractDungeon.onModifyPower();
    }

    public void addCalcium(int amount) {
        calciumNum += amount;
        calciumFlash = 2.0F;
        AbstractDungeon.onModifyPower();
    }

    public void renderSingleFlash(SpriteBatch sb, TextureRegion region, float x, float y, float scale) {
        float w = 48.0F * Settings.scale;
        float h = 48.0F * Settings.scale;
        sb.draw(region, x - w * scale * 0.5F, y - h * scale * 0.5F, 0, 0, w, h, scale, scale, 0.0F);
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

    public void renderFlowsp(SpriteBatch sb) {
        TextureRegion region = FS_REGION;
        sb.setColor(Color.WHITE);
        sb.draw(region, flowspX - 24.0F * Settings.scale, flowspY - 24.0F * Settings.scale, 0, 0, 48.0F * Settings.scale, 48.0F * Settings.scale, 1.0F, 1.0F, 0.0F);
        FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, Integer.toString(flowspNum), flowspX + 24.0F * Settings.scale, flowspY - 24.0F * Settings.scale, Color.LIGHT_GRAY);
        if (flowspFlash > 0.0F) {
            renderFlash(sb, region, flowspX, flowspY, flowspFlash);
            flowspFlash -= Gdx.graphics.getDeltaTime();
        }
    }

    public void renderGravity(SpriteBatch sb) {
        TextureRegion region;
        if (gravity == GravityDirection.UP) region = UP_REGION;
        else if (gravity == GravityDirection.DOWN) region = DOWN_REGION;
        else region = NONE_REGION;
        sb.setColor(Color.WHITE);
        sb.draw(region, gravityX - 24.0F * Settings.scale, gravityY - 24.0F * Settings.scale, 0, 0, 48.0F * Settings.scale, 48.0F * Settings.scale, 1.0F, 1.0F, 0.0F);
        if (gravityFlash > 0.0F) {
            renderFlash(sb, region, gravityX, gravityY, gravityFlash);
            gravityFlash -= Gdx.graphics.getDeltaTime();
        }
    }

    public void renderCalcium(SpriteBatch sb) {
        TextureRegion region = CA_REGION;
        sb.setColor(Color.WHITE);
        sb.draw(region, calciumX - 24.0F * Settings.scale, calciumY - 24.0F * Settings.scale, 0, 0, 48.0F * Settings.scale, 48.0F * Settings.scale, 1.0F, 1.0F, 0.0F);
        FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, Integer.toString(calciumNum), calciumX + 24.0F * Settings.scale, calciumY - 24.0F * Settings.scale, Color.LIGHT_GRAY);
        if (calciumFlash > 0.0F) {
            renderFlash(sb, region, calciumX, calciumY, calciumFlash);
            calciumFlash -= Gdx.graphics.getDeltaTime();
        }
    }

    public void render(SpriteBatch sb) {
        renderFlowsp(sb);
        renderGravity(sb);
        renderCalcium(sb);
        if (!AbstractDungeon.isScreenUp) {
            flowspHb.update();
            gravityHb.update();
            calciumHb.update();
            if (gravityHb.hovered) {
                ArrayList<PowerTip> tips = new ArrayList<>();
                if (gravity == GravityDirection.NONE)
                    tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:Gravity")), BaseMod.getKeywordDescription("rhinemod:Gravity")));
                else if (gravity == GravityDirection.UP)
                    tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:GravityUp")), BaseMod.getKeywordDescription("rhinemod:GravityUp")));
                else
                    tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:GravityDown")), BaseMod.getKeywordDescription("rhinemod:GravityDown")));
                TipHelper.queuePowerTips(gravityHb.cX + gravityHb.width / 2.0F, gravityHb.cY + TipHelper.calculateAdditionalOffset(tips, gravityHb.cY), tips);
            }
            if (calciumHb.hovered) {
                ArrayList<PowerTip> tips = new ArrayList<>();
                tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:Calcium")), BaseMod.getKeywordDescription("rhinemod:Calcium")));
                TipHelper.queuePowerTips(calciumHb.cX + calciumHb.width / 2.0F, calciumHb.cY + TipHelper.calculateAdditionalOffset(tips, calciumHb.cY), tips);
            }
            if (flowspHb.hovered) {
                ArrayList<PowerTip> tips = new ArrayList<>();
                tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:FlowingShape")), BaseMod.getKeywordDescription("rhinemod:FlowingShape")));
                TipHelper.queuePowerTips(flowspHb.cX + flowspHb.width / 2.0F, flowspHb.cY + TipHelper.calculateAdditionalOffset(tips, flowspHb.cY), tips);
            }
        }
    }

    public void atStartOfCombat() {
        calciumNum = 0;
        flowspNum = 0;
        changeGravity(GravityDirection.NONE);
        gravityChanges = 0;
    }

    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new ReduceCalciumAction(calciumReduceNum));
        changeGravity(GravityDirection.NONE);
        AbstractDungeon.onModifyPower();
    }
}
