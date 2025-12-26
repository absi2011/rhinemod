package rhinemod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;
import rhinemod.RhineMod;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.characters.RhineLab;

public class CardRenderPatch {
    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard _inst, SpriteBatch sb, boolean ___darken, Color ___renderColor) {
            if (!___darken && !_inst.isLocked && _inst.isSeen && _inst instanceof AbstractRhineCard) {
                int branch = ((AbstractRhineCard) _inst).realBranch;
                if (branch > 0) {
                    sb.setColor(___renderColor);
                    sb.draw(RhineMod.specialImg.get(branch - 1),
                            _inst.current_x - 256.0F, _inst.current_y - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F,
                            _inst.drawScale * Settings.scale, _inst.drawScale * Settings.scale, _inst.angle);
                }
            }
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCost")
    public static class SingleRenderPatch {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
            if (!___card.isLocked && ___card.isSeen && ___card instanceof AbstractRhineCard) {
                int branch = ((AbstractRhineCard) ___card).realBranch;
                if (branch > 0) {
                    sb.draw(RhineMod.specialImgLarge.get(branch - 1),
                            (float)Settings.WIDTH / 2.0F - 512.0F, (float)Settings.HEIGHT / 2.0F - 512.0F,
                            512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale,
                            0.0F, 0, 0, 1024, 1024, false, false);
                }
            }
        }
    }

//    private static final float IMG_HEIGHT = 420.0F * Settings.scale;
//    private static final float MY_DESC_OFFSET_Y = IMG_HEIGHT * 0.28F;
//
//    @SpirePatch(clz = AbstractCard.class, method = "renderDescription")
//    public static class RenderDescriptionPatch {
//        @SpireInsertPatch(locator = Locator.class, localvars = {"draw_y"})
//        public static void Insert(AbstractCard _inst, SpriteBatch sb, @ByRef float[] draw_y) {
//            if (!(AbstractDungeon.player instanceof RhineLab) || !RhineMod.useLoneTrail) return;
//            draw_y[0] = _inst.current_y - IMG_HEIGHT * _inst.drawScale / 2.0F + MY_DESC_OFFSET_Y * _inst.drawScale;
//        }
//
//        private static class Locator extends SpireInsertLocator {
//            @Override
//            public int[] Locate(CtBehavior ctBehavior) throws Exception {
//                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(BitmapFont.class, "getCapHeight");
//                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
//            }
//        }
//    }
//
//    @SpirePatch(clz = AbstractCard.class, method = "renderDescriptionCN")
//    public static class RenderDescriptionCNPatch {
//        @SpireInsertPatch(locator = Locator.class, localvars = {"draw_y"})
//        public static void Insert(AbstractCard _inst, SpriteBatch sb, @ByRef float[] draw_y) {
//            if (!(AbstractDungeon.player instanceof RhineLab) || !RhineMod.useLoneTrail) return;
//            draw_y[0] = _inst.current_y - IMG_HEIGHT * _inst.drawScale / 2.0F + MY_DESC_OFFSET_Y * _inst.drawScale;
//        }
//
//        private static class Locator extends SpireInsertLocator {
//            @Override
//            public int[] Locate(CtBehavior ctBehavior) throws Exception {
//                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(BitmapFont.class, "getCapHeight");
//                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
//            }
//        }
//    }
}
