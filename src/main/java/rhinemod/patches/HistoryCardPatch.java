package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.stats.RunData;
import rhinemod.cards.AbstractRhineCard;

public class HistoryCardPatch {
    private static AbstractCard checkRhineCard(String cardID) {

        if (cardID.startsWith("rhinemod")) {
            String libraryLookupName = cardID;
            int upgradeBranch;
            if (libraryLookupName.charAt(libraryLookupName.length() - 2) == '*') {
                upgradeBranch = libraryLookupName.charAt(libraryLookupName.length() - 1) - '0';
                libraryLookupName = libraryLookupName.substring(0, libraryLookupName.length() - 2);
            } else {
                return null;
            }
            AbstractCard card = CardLibrary.getCard(libraryLookupName);
            if (!(card instanceof AbstractRhineCard)) return null;
            card = card.makeSameInstanceOf();
            ((AbstractRhineCard) card).chosenBranch = upgradeBranch;
            card.upgrade();
            return card;
        } else {
            return null;
        }
    }

    @SpirePatch(clz = RunHistoryScreen.class, method = "cardForName")
    public static class CardForNamePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(RunHistoryScreen _inst, RunData runData, String cardID) {
            AbstractCard c = checkRhineCard(cardID);
            if (c == null) return SpireReturn.Continue();
            else return SpireReturn.Return(c);
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCardNameFromMetricID")
    public static class GetCardNameFromMetricIDPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(String metricID) {
            AbstractCard c = checkRhineCard(metricID);
            if (c == null) return SpireReturn.Continue();
            else return SpireReturn.Return(c.name);
        }
    }
}
