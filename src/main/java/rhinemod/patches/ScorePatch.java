package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.ScoreBonusStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import javassist.CtBehavior;
import rhinemod.relics.Deal;
import rhinemod.relics.Melt;
import rhinemod.util.TheSky;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ScorePatch {
    public static boolean IS_MELT = false;
    public static boolean VIOLATE_DEAL = false;
    public static boolean BEAT_STARPOD = false;
    public static final ScoreBonusStrings MELT = CardCrawlGame.languagePack.getScoreString("rhinemod:Melt");
    public static final ScoreBonusStrings VDEAL = CardCrawlGame.languagePack.getScoreString("rhinemod:ViolateDeal");
    public static final ScoreBonusStrings STARPOD = CardCrawlGame.languagePack.getScoreString("rhinemod:StarPod");
    @SpirePatch(clz = GameOverScreen.class, method = "checkScoreBonus")
    public static class CheckScorePatch {
        @SpireInsertPatch(locator = Locator.class, localvars = "points")
        public static void Insert(boolean victory, @ByRef int[] points) {
            if (AbstractDungeon.player.hasRelic(Melt.ID)) {
                IS_MELT = true;
                points[0] -= 25;
            }
            if (AbstractDungeon.player.hasRelic(Deal.ID) && AbstractDungeon.id.equals("TheBeyond") && victory) {
                VIOLATE_DEAL = true;
                points[0] -= 500;
            }
            if (AbstractDungeon.id.equals(TheSky.ID) && victory) {
                BEAT_STARPOD = true;
                points[0] += 350;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = GameOverScreen.class, method = "resetScoreChecks")
    public static class ResetScorePatch {
        @SpirePostfixPatch
        public static void Postfix() {
            IS_MELT = false;
            VIOLATE_DEAL = false;
            BEAT_STARPOD = false;
        }
    }

    private static void addStats(GameOverScreen _inst) {
        try {
            Field stats = GameOverScreen.class.getDeclaredField("stats");
            stats.setAccessible(true);
            if (IS_MELT) {
                ((ArrayList<GameOverStat>) stats.get(_inst)).add(new GameOverStat(MELT.NAME, MELT.DESCRIPTIONS[0], Integer.toString(-25)));
            }
            if (VIOLATE_DEAL) {
                ((ArrayList<GameOverStat>) stats.get(_inst)).add(new GameOverStat(VDEAL.NAME, VDEAL.DESCRIPTIONS[0], Integer.toString(-500)));
            }
            if (BEAT_STARPOD) {
                ((ArrayList<GameOverStat>) stats.get(_inst)).add(new GameOverStat(STARPOD.NAME, STARPOD.DESCRIPTIONS[0], Integer.toString(350)));
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to set game over stats.", e);
        }
    }

    @SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
    public static class VictoryStatsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(VictoryScreen _inst) {
            addStats(_inst);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(VictoryScreen.class, "IS_POOPY");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = DeathScreen.class, method = "createGameOverStats")
    public static class DeathStatsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(DeathScreen _inst) {
            addStats(_inst);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(DeathScreen.class, "IS_POOPY");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
