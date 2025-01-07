package rhinemod.patches;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class WaterAttackEffectPatch {
    @SpireEnum public static AbstractGameAction.AttackEffect WATER;
    private static boolean firstUse = true;
    private static Texture WATER_IMG;
    private static TextureAtlas.AtlasRegion WATER_REGION;

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
                if (firstUse) {
                    firstUse = false;
                    WATER_IMG = ImageMaster.loadImage("resources/rhinemod/images/ui/water.png");
                    WATER_REGION = new TextureAtlas.AtlasRegion(WATER_IMG, 0, 0, WATER_IMG.getWidth(), WATER_IMG.getHeight());
                }
                return SpireReturn.Return(WATER_REGION);
            }
            return SpireReturn.Continue();
        }
    }
}
