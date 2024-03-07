package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
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
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(String.class.getName()) && m.getMethodName().equals("equals")) {
                        m.replace("$_ = ($0.equals(\"rhinemod:TheSky\") || $proceed($$));");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = DungeonMap.class, method = "calculateMapSize")
    public static class CalculateMapSizePatch {
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

    @SpirePatch(clz = AbstractDungeon.class, method = "populatePathTaken")
    public static class PopularPathTakenPatch {
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

    @SpirePatch(clz = ProceedButton.class, method = "goToVictoryRoomOrTheDoor")
    public static class GoToVictoryRoomOrTheDoorPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(ProceedButton _inst) {
            if (AbstractDungeon.player.hasRelic(LoneTrail.ID)) {
                CardCrawlGame.music.fadeOutBGM();
                CardCrawlGame.music.fadeOutTempBGM();
                MapRoomNode node = new MapRoomNode(-1, 15);
                node.room = new VictoryRoom(SKY);
                AbstractDungeon.nextRoom = node;
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.nextRoomTransitionStart();
                _inst.hide();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = VictoryRoom.class, method = "onPlayerEntry")
    public static class OnPlayerEntryPatch {
        @SpirePostfixPatch
        public static void Postfix(VictoryRoom _inst) {
            if (_inst.eType == SKY) {
                _inst.event = new SkyEvent();
                _inst.event.onEnterRoom();
            }
        }
    }
}
