package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rhinemod.RhineMod;

public class WaterAttackEffectPatch {
    @SpireEnum public static AbstractGameAction.AttackEffect WATER;

    @SpirePatch(clz = FlashAtkImgEffect.class, method = "playSound")
    public static class PlaySoundPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(FlashAtkImgEffect _inst, AbstractGameAction.AttackEffect effect) {
            if (effect == WATER) {
                CardCrawlGame.sound.play("SUBMERSION_TRIG");
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = FlashAtkImgEffect.class, method = "loadImage")
    public static class LoadImagePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(FlashAtkImgEffect _inst, AbstractGameAction.AttackEffect ___effect) {
            if (___effect == WATER) {
                return SpireReturn.Return(RhineMod.WATER_REGION);
            }
            return SpireReturn.Continue();
        }
    }
}
