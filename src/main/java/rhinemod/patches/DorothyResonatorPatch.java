package rhinemod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import rhinemod.RhineMod;

public class DorothyResonatorPatch {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class OptFields {
        public static SpireField<Boolean> trapped = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard _inst, SpriteBatch sb, boolean ___darken, Color ___renderColor) {
            if (!___darken && !_inst.isLocked && OptFields.trapped.get(_inst)) {
                sb.setColor(___renderColor);
                sb.draw(RhineMod.resonatorImg,
                        _inst.current_x - 256.0F, _inst.current_y - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F,
                        _inst.drawScale * Settings.scale, _inst.drawScale * Settings.scale, _inst.angle);
            }
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = "<ctor>", paramtypez = {AbstractCard.class, AbstractCreature.class})
    public static class UseCardPatch {
        @SpirePostfixPatch
        public static void Postfix(UseCardAction _inst, AbstractCard card, AbstractCreature target) {
            if (OptFields.trapped.get(card)) {
                OptFields.trapped.set(card, false);
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), 1));
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "draw", paramtypez = {int.class})
    public static class DrawPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = "c")
        public static void Insert(AbstractPlayer _inst, int numCards, AbstractCard c) {
            if (RhineMod.tagLevel >= 3 && OptFields.trapped.get(c)) {
                for (AbstractCard c2 : _inst.hand.group)
                    if (OptFields.trapped.get(c2)) {
                        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
                        break;
                    }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "triggerWhenDrawn");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }
}
