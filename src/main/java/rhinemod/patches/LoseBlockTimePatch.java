package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class LoseBlockTimePatch {
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class CheckScorePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(GameActionManager _inst) {
            AbstractPlayer p = AbstractDungeon.player;
            if (!p.hasPower("Barricade") && !p.hasPower("Blur")) {
                if (!p.hasRelic("Calipers")) {
                    _inst.addToBottom(new LoseBlockAction(p, p, p.currentBlock));
                } else {
                    _inst.addToBottom(new LoseBlockAction(p, p, 15));
                }
            }

            if (!AbstractDungeon.getCurrRoom().isBattleOver) {
                _inst.addToBottom(new DrawCardAction(null, p.gameHandSize, true));
                p.applyStartOfTurnPostDrawRelics();
                p.applyStartOfTurnPostDrawPowers();
                _inst.addToBottom(new EnableEndTurnButtonAction());
            }
            return SpireReturn.Return();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasPower");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }
}
