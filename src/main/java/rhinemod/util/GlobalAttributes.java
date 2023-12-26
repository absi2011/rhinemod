package rhinemod.util;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class GlobalAttributes {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rhinemod:GlobalAttributes");
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String UP_IMG = "images/ui/gravityUp.png";
    public static final String DOWN_IMG = "images/ui/gravityDown.png";
    public static final String NONE_IMG = "images/ui/gravityNone.png";
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

    public GlobalAttributes() {
        gravity = GravityDirection.NONE;
        calciumReduceNum = 3;
        calciumNum = 0;
        gravityX = 325.0F * Settings.scale;
        gravityY = 800.0F * Settings.scale;
        calciumX = 450.0F * Settings.scale;
        calciumY = 800.0F * Settings.scale;
        gravityHb = new Hitbox(gravityX, gravityY - 48.0F * Settings.scale, 65.0F * Settings.scale, 65.0F * Settings.scale);
        calciumHb = new Hitbox(calciumX - 5.0F * Settings.scale, calciumY - 45.0F * Settings.scale, 250.0F * Settings.scale, 50.0F * Settings.scale);
    }

    public void changeGravity() {
        if (gravity == GravityDirection.UP) gravity = GravityDirection.DOWN;
        else gravity = GravityDirection.UP;
    }

    public void addCalcium(int amount) {
        calciumNum += amount;
    }

    public void render(SpriteBatch sb) {
        Texture img;
        if (gravity == GravityDirection.UP) img = new Texture(UP_IMG);
        else if (gravity == GravityDirection.DOWN) img = new Texture(DOWN_IMG);
        else img = new Texture(NONE_IMG);
        sb.setColor(Color.WHITE);
        sb.draw(img, gravityX, gravityY - 45.0F * Settings.scale);
        FontHelper.renderFont(sb, FontHelper.panelNameFont, TEXT[0] + calciumNum, calciumX, calciumY, Color.LIGHT_GRAY);
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
    }
}
