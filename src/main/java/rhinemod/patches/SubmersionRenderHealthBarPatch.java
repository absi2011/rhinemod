package rhinemod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import javassist.CtBehavior;
import rhinemod.powers.Submersion;

public class SubmersionRenderHealthBarPatch {
    private static final float HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
    private static final float HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;

    @SpirePatch(clz = AbstractCreature.class, method = SpirePatch.CLASS)
    public static class OptFields {
        public static SpireField<Color> skyHbBarColor = new SpireField<>(() -> new Color(0.5F, 0.8F, 0.9F, 0.0F));
    }
    @SpirePatch(clz = AbstractCreature.class, method = "renderRedHealthBar")
    public static class RenderPatch {
        public static void drawBar(SpriteBatch sb, float x, float y, float realWidth) {
            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, realWidth, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_R, x + realWidth, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        }

        @SpireInsertPatch(rloc = 0, localvars = {"targetHealthBarWidth", "greenHbBarColor", "blueHbBarColor", "redHbBarColor"})
        public static SpireReturn<?> prefix(AbstractCreature _inst, SpriteBatch sb, float x, float y, float targetHealthBarWidth, Color greenHbBarColor, Color blueHbBarColor, Color redHbBarColor) {
            float realWidth = targetHealthBarWidth;
            int resHealth = _inst.currentHealth;
            if (_inst.hasPower(Submersion.POWER_ID) && _inst.getPower(Submersion.POWER_ID).amount > 0 && resHealth > 0) {
                sb.setColor(OptFields.skyHbBarColor.get(_inst));
                drawBar(sb, x, y, realWidth);
                int dec = Submersion.DAMAGE_TAKE[_inst.getPower(Submersion.POWER_ID).amount];
                if (_inst.hasPower("Intangible") && dec > 0) dec = 1;
                realWidth *= 1 - dec * 1.F / resHealth;
                resHealth -= dec;
            }
            if (_inst.hasPower("Poison") && resHealth > 0) {
                sb.setColor(greenHbBarColor);
                drawBar(sb, x, y, realWidth);
                int dec = _inst.getPower("Poison").amount;
                if (_inst.hasPower("Intangible") && dec > 0) dec = 1;
                realWidth *= 1 - dec * 1.F / resHealth;
                resHealth -= dec;
            }
            if (resHealth > 0) {
                if (_inst.currentBlock > 0) sb.setColor(blueHbBarColor);
                else sb.setColor(redHbBarColor);
                drawBar(sb, x, y, realWidth);
            }
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "updateHbAlpha")
    public static class UpdateHbAlphaPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractCreature _inst) {
            OptFields.skyHbBarColor.get(_inst).a =  _inst.hbAlpha;
        }


        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(AbstractCreature.class,
                        "redHbBarColor");
                return LineFinder.findAllInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
