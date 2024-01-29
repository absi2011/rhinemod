package rhinemod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import javassist.CtBehavior;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.characters.RhineLab;
import rhinemod.characters.StarRing;

import java.lang.reflect.Field;

public class StarRingTargetPatch {
    // TODO: keyboard input
    @SpirePatch(clz = AbstractPlayer.class, method = "updateSingleTargetInput")
    public static class UpdateSingleTargetInputPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractPlayer _inst) {
            if (!_inst.isInKeyboardMode) {
                if (_inst instanceof RhineLab && _inst.hoveredCard instanceof AbstractRhineCard && ((AbstractRhineCard) _inst.hoveredCard).isTargetStarRing) {
                    AbstractMonster m = null;
                    for (StarRing r : ((RhineLab) _inst).currentRings) {
                        r.hb.update();
                        if (!r.isDead && r.currentHealth > 0 && r.hb.hovered) {
                            m = r;
                            break;
                        }
                    }
                    try {
                        Field field = AbstractPlayer.class.getDeclaredField("hoveredMonster");
                        field.setAccessible(true);
                        field.set(_inst, m);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(MonsterGroup.class,
                        "areMonstersBasicallyDead");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
    @SpirePatch(clz = AbstractMonster.class, method = "renderName")
    public static class RenderMonsterNamePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(AbstractMonster _inst, SpriteBatch sb) {
            if (AbstractDungeon.player.hoveredCard instanceof AbstractRhineCard && ((AbstractRhineCard) AbstractDungeon.player.hoveredCard).isTargetStarRing)
                return SpireReturn.Return();
            return SpireReturn.Continue();
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class,
                        "isDraggingCard");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
