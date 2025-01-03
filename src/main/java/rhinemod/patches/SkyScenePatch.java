package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import rhinemod.events.SkyEvent;
import rhinemod.relics.LoneTrail;
import rhinemod.util.TheSky;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.currMapNode;

public class SkyScenePatch {
    @SpireEnum public static VictoryRoom.EventType SKY;

    @SpirePatch(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = {String.class, AbstractPlayer.class})
    public static class GetDungeonPatch1 {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(CardCrawlGame _inst, String key, AbstractPlayer p) {
            if (key.equals(TheSky.ID)) {
                return SpireReturn.Return(new TheSky(p, AbstractDungeon.specialOneTimeEventList));
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = {String.class, AbstractPlayer.class, SaveFile.class})
    public static class GetDungeonPatch2 {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(CardCrawlGame _inst, String key, AbstractPlayer p, SaveFile saveFile) {
            if (key.equals(TheSky.ID)) {
                return SpireReturn.Return(new TheSky(p, saveFile));
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = DungeonMap.class, method = "update")
    public static class UpdatePatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getClassName().equals(Settings.class.getName()) && m.getFieldName().equals("isDebug"))
                        m.replace("$_ = " + UpdatePatch.class.getName() + ".specialCheck() || $proceed($$);");
                }
            };
        }

        public static boolean specialCheck() {
            return AbstractDungeon.id.equals("rhinemod:TheSky") && AbstractDungeon.getCurrMapNode().y == 3;
        }
    }

    @SpirePatch(clz = DungeonMap.class, method = "calculateMapSize")
    public static class CalculateMapSizePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix() {
            if (AbstractDungeon.id.equals("rhinemod:TheSky"))
                return SpireReturn.Return(Settings.MAP_DST_Y * 5.0F - 1380.0F * Settings.scale);
            else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = DungeonMap.class, method = "render")
    public static class RenderPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(String.class.getName()) && m.getMethodName().equals("equals")) {
                        m.replace("$_ = ($0.equals(\"rhinemod:TheSky\") || $proceed($$));");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = DungeonMap.class, method = "renderMapBlender")
    public static class RenderMapBlenderPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(String.class.getName()) && m.getMethodName().equals("equals")) {
                        m.replace("$_ = ($0.equals(\"rhinemod:TheSky\") || $proceed($$));");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = ProceedButton.class, method = "update")
    public static class ProceedButtonPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(ProceedButton _inst) {
            if (AbstractDungeon.id.equals("rhinemod:TheSky")) {
                CardCrawlGame.music.fadeOutBGM();
                MapRoomNode node = new MapRoomNode(3, 5);
                node.room = new TrueVictoryRoom();
                AbstractDungeon.nextRoom = node;
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.nextRoomTransitionStart();
                _inst.hide();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "id");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "populatePathTaken")
    public static class PopulatePathTakenPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon _inst, SaveFile saveFile) {
            if (currMapNode.room instanceof VictoryRoom && AbstractDungeon.player.hasRelic(LoneTrail.ID))
                CardCrawlGame.stopClock = false;
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "onFinalBossVictoryLogic")
    public static class OnFinalBossVictoryLogicPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractMonster _inst) {
            if (AbstractDungeon.player.hasRelic(LoneTrail.ID))
                CardCrawlGame.stopClock = false;
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(CardCrawlGame.class, "playtime");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = VictoryRoom.class, method = "onPlayerEntry")
    public static class OnPlayerEntryPatch {
        @SpirePrefixPatch
        public static void Prefix(VictoryRoom _inst) {
            if (AbstractDungeon.player.hasRelic(LoneTrail.ID)) {
                _inst.eType = SKY;
                AbstractDungeon.overlayMenu.proceedButton.hide();
                _inst.event = new SkyEvent();
                _inst.event.onEnterRoom();
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "setBoss")
    public static class SetBossPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon _inst, String key) {
            if (key.equals("The Sky")) {
                DungeonMap.boss = ImageMaster.loadImage("resources/rhinemod/images/ui/starpod.png");
                DungeonMap.bossOutline = ImageMaster.loadImage("resources/rhinemod/images/ui/starpod_outline.png");
            }
        }
    }

    @SpirePatch(clz = DungeonMapScreen.class, method = "open")
    public static class OpenDungeonMapPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"mapScrollUpperLimit"})
        public static void Insert(DungeonMapScreen _inst, boolean doScrollingAnimation, @ByRef float[] mapScrollUpperLimit) {
            if (AbstractDungeon.id.equals("rhinemod:TheSky"))
                mapScrollUpperLimit[0] = -470.0F * Settings.scale;
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "releaseCard");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }
}
