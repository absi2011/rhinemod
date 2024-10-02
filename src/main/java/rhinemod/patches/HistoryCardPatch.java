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
    @SpirePatch(clz = RunHistoryScreen.class, method = "cardForName")
    public static class CardForNamePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(RunHistoryScreen _inst, RunData runData, String cardID) {
            if (cardID.startsWith("rhinemod")) {
                String libraryLookupName = cardID;
                int upgradeBranch = -1;
                if (libraryLookupName.charAt(libraryLookupName.length() - 2) == '+') {
                    upgradeBranch = libraryLookupName.charAt(libraryLookupName.length() - 1) - '0';
                    libraryLookupName = libraryLookupName.substring(0, libraryLookupName.length() - 2);
                }
                AbstractCard card = CardLibrary.getCard(libraryLookupName);
                if (!(card instanceof AbstractRhineCard)) return SpireReturn.Continue();
                card = card.makeSameInstanceOf();
                if (upgradeBranch != -1) {
                    ((AbstractRhineCard) card).chosenBranch = upgradeBranch;
                    card.upgrade();
                }
                return SpireReturn.Return(card);
            } else {
                return SpireReturn.Continue();
            }
        }
    }
}
