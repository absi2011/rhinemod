package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rhinemod.RhineMod;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.cards.Dreamer;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveData {
    private static final Logger logger = LogManager.getLogger(SaveData.class);

    @SpirePatch(clz = CardGroup.class, method = "getCardDeck")
    public static class GetCardDeckPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(CardGroup _inst) {
            ArrayList<CardSave> retVal = new ArrayList<>();
            for (AbstractCard card : _inst.group) {
                CardSave cs;
                if (card instanceof AbstractRhineCard && card.upgraded) {
                    cs = new CardSave(card.cardID, -((AbstractRhineCard) card).chosenBranch - 1, card.misc);
                } else {
                    cs = new CardSave(card.cardID, card.timesUpgraded, card.misc);
                }
                retVal.add(cs);
            }
            return SpireReturn.Return(retVal);
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "loadPlayerSave")
    public static class LoadPlayerSavePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CardCrawlGame _inst, AbstractPlayer p, SaveFile ___saveFile) {
            p.masterDeck.clear();
            for (CardSave s : ___saveFile.cards) {
                AbstractCard c;
                if (s.id.startsWith("rhinemod:") && s.upgrades < 0) {
                    c = CardLibrary.getCopy(s.id, 0, s.misc);
                    ((AbstractRhineCard) c).chosenBranch = -(s.upgrades + 1);
                    c.upgrade();
                    if (c instanceof Dreamer && ((Dreamer) c).chosenBranch == 3) {
                        c.baseDamage = c.damage = c.misc = s.misc;
                        c.initializeDescription();
                    }
                } else {
                    c = CardLibrary.getCopy(s.id, s.upgrades, s.misc);
                }
                p.masterDeck.addToTop(c);
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(Settings.class, "isEndless");
                return LineFinder.findInOrder(ctMethodToPatch, fieldAccessMatcher);
            }
        }
    }

    public static class ExtraData {
        public int tagLevel = 0;
    }

    public static ExtraData exSaveData = new ExtraData();

    @SpirePatch(clz = SaveFile.class, method = "<ctor>", paramtypez = {SaveFile.SaveType.class})
    public static class SaveTheSaveData {
        @SpirePostfixPatch
        public static void Postfix(SaveFile _inst, SaveFile.SaveType type) {
            exSaveData.tagLevel = RhineMod.tagLevel;
            logger.info("Extra Data Saved!");
        }
    }

    @SpirePatch(clz = SaveAndContinue.class, method = "save", paramtypez = {SaveFile.class})
    public static class SaveDataToFile {
        @SpireInsertPatch(locator = Locator.class, localvars = {"params"})
        public static void Insert(SaveFile save, HashMap<Object, Object> params) {
            params.put("tagLevel", exSaveData.tagLevel);

        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(GsonBuilder.class, "create");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = SaveAndContinue.class, method = "loadSaveFile", paramtypez = {String.class})
    public static class LoadDataFromFile {
        @SpireInsertPatch(locator = Locator.class, localvars = {"gson", "savestr"})
        public static void Insert(String path, Gson gson, String savestr) {
            try {
                exSaveData = gson.fromJson(savestr, ExtraData.class);
            } catch (Exception e) {
                logger.error("Fail to load rhinemod save data.");
                e.printStackTrace();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(Gson.class, "fromJson");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "loadSave")
    public static class loadSavePatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon _inst, SaveFile file) {
            RhineMod.tagLevel = exSaveData.tagLevel;
            logger.info("Save loaded.");
        }
    }
}
