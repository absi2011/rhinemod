package rhinemod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rhinemod.characters.RhineLab;

public class GlobalAttributesPatch {
    @SpirePatch(clz = AbstractCreature.class, method = "renderPowerIcons")
    public static class RenderPowerIconsPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y, Color ___hbTextColor) {
            if (!(__instance instanceof RhineLab)) return SpireReturn.Continue();
            float offset = 10.0F * Settings.scale;
            for (AbstractPower p : __instance.powers)
                if (!p.ID.equals("rhinemod:InvisibleGlobalAttributes")) {
                    p.renderIcons(sb, x + offset, y - 48.0F * Settings.scale, ___hbTextColor);
                    offset += 48.0F * Settings.scale;
                }

            offset = 0.0F * Settings.scale;
            for (AbstractPower p : __instance.powers)
                if (!p.ID.equals("rhinemod:InvisibleGlobalAttributes")) {
                    p.renderAmount(sb, x + offset + 32.0F * Settings.scale, y - 66.0F * Settings.scale, ___hbTextColor);
                    offset += 48.0F * Settings.scale;
                }
            return SpireReturn.Return();
        }
    }
}
