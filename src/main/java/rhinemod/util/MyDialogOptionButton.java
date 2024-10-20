package rhinemod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;

public class MyDialogOptionButton extends LargeDialogOptionButton {
    public final Texture BUTTON = ImageMaster.loadImage("resources/rhinemod/images/ui/enabledButtonShort.png");
    private final float realX;
    private final float realY;
    public AbstractRelic relic;
    public MyDialogOptionButton(int slot, String msg, boolean isDisabled, AbstractCard previewCard, AbstractRelic previewRelic) {
        super(slot, msg, isDisabled, previewCard, previewRelic);
        relic = previewRelic;
        realX = 450.0F * Settings.xScale;
        realY = Settings.OPTION_Y - 450.0F * Settings.yScale + (3 - slot) * 82.0F * Settings.yScale;
        hb = new Hitbox(560.0F * Settings.xScale, 80.0F * Settings.yScale);
        hb.move(realX, realY);
    }

    public void render(SpriteBatch sb) {
        float scale = Settings.scale;
        float xScale = Settings.xScale;
        if (this.hb.clickStarted) {
            scale *= 0.99F;
            xScale *= 0.99F;
        }

        sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        sb.draw(BUTTON, realX - 278.5F, realY - 38.5F, 278.5F, 38.5F, 557.0F, 77.0F, xScale, scale, 0.0F, 0, 0, 557, 77, false, false);
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.15F));
        sb.draw(BUTTON, realX - 278.5F, realY - 38.5F, 278.5F, 38.5F, 557.0F, 77.0F, xScale, scale, 0.0F, 0, 0, 557, 77, false, false);
        sb.setBlendFunction(770, 771);

        if (FontHelper.getSmartWidth(FontHelper.largeDialogOptionFont, this.msg, (float)Settings.WIDTH, 0.0F) > 800.0F * Settings.xScale) {
            FontHelper.renderSmartText(sb, FontHelper.smallDialogOptionFont, this.msg, realX - 230.0F * Settings.xScale, realY + 10.0F * Settings.yScale, Settings.WIDTH, 0.0F, Color.WHITE.cpy());
        } else {
            FontHelper.renderSmartText(sb, FontHelper.largeDialogOptionFont, this.msg, realX - 230.0F * Settings.xScale, realY + 10.0F * Settings.yScale, Settings.WIDTH, 0.0F, Color.WHITE.cpy());
        }

        this.hb.render(sb);
    }

    @Override
    public void renderRelicPreview(SpriteBatch sb) {
        if (!Settings.isControllerMode && relic != null && this.hb.hovered) {
            TipHelper.queuePowerTips(700.0F * Settings.scale, InputHelper.mY + TipHelper.calculateToAvoidOffscreen(relic.tips, InputHelper.mY), relic.tips);
        }
    }

    @SpirePatch(clz = LargeDialogOptionButton.class, method = "calculateY")
    public static class CalculateYPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(LargeDialogOptionButton _inst, int size) {
            if (_inst instanceof MyDialogOptionButton) {
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }
}
