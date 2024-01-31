package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import javassist.CtBehavior;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.util.ChangeBranchOption;

import java.util.ArrayList;

public class CampfireOptionPatch {
    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class AddChangeBranchOption {
        @SpireInsertPatch(locator = Locator.class, localvars = "buttons")
        public static void Insert(CampfireUI _inst, ArrayList<AbstractCampfireOption> buttons) {
            boolean valid = false;
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
                if (c instanceof AbstractRhineCard && c.upgraded && ((AbstractRhineCard) c).possibleBranches().size() > 1) {
                    valid = true;
                    break;
                }
            buttons.add(new ChangeBranchOption(valid));
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(Settings.class, "isFinalActAvailable");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
