
package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.CampfireChoice;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import javassist.CtBehavior;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.relics.Future;
import rhinemod.util.ChangeBranchOption;
import rhinemod.vfx.CampfireChangeBranchEffect;

import java.util.ArrayList;
import java.util.logging.Logger;

public class CampfireOptionPatch {
    public static final String TEXT_RECAST_OPTION = ChangeBranchOption.TEXT[4];
    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class AddChangeBranchOption {
        @SpireInsertPatch(locator = Locator.class, localvars = "buttons")
        public static SpireReturn<?> Insert(CampfireUI _inst, ArrayList<AbstractCampfireOption> buttons) {
            boolean valid = false;
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
                if (c instanceof AbstractRhineCard && c.upgraded && ((AbstractRhineCard) c).possibleBranches().size() > 1) {
                    valid = true;
                    break;
                }
            AbstractCampfireOption option = new ChangeBranchOption(valid);
            if (AbstractDungeon.player.hasRelic(Future.ID)) {
                option.usable = false;
            }
            buttons.add(option);
            CampfireChangeBranchEffect.ids.clear();

            boolean cannotProceed = false;
            for (AbstractCampfireOption opt : buttons)
                if (!(opt instanceof ChangeBranchOption) && opt.usable) {
                    cannotProceed = true;
                    break;
                }
            if (Settings.isFinalActAvailable && !Settings.hasRubyKey) {
                cannotProceed = true;
            }
            if (!cannotProceed) {
                AbstractRoom.waitTimer = 0.0F;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(Settings.class, "isFinalActAvailable");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = MetricData.class, method = "addCampfireChoiceData", paramtypez = {String.class, String.class})
    public static class AddCampfireChoiceDataPatch {
        @SpirePrefixPatch
        public static void Prefix(MetricData _inst, String choiceKey, @ByRef String[] data) {
            if (!CampfireChangeBranchEffect.ids.isEmpty()) {
                data[0] = data[0] + "^" + CampfireChangeBranchEffect.ids.get(0) + "^" + CampfireChangeBranchEffect.ids.get(1);
            }
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = "getTipDescriptionText")
    public static class GetTipDescriptionTextPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"campfireChoice", "sb"})
        public static void Insert(RunPathElement _inst, CampfireChoice campfireChoice, StringBuilder sb) {
            if (campfireChoice.data.contains("^")) {
                String[] ids = campfireChoice.data.split("\\^");
                if (ids.length < 2) return;
                sb.append(String.format(TEXT_RECAST_OPTION,
                        CardLibrary.getCardNameFromMetricID(ids[ids.length - 2]),
                        CardLibrary.getCardNameFromMetricID(ids[ids.length - 1])));
                sb.append(" NL ");
                campfireChoice.data = ids[0];
            } else {
                Logger.getLogger(CampfireOptionPatch.class.getName()).info("History Tip Patch: ids not found");
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(CampfireChoice.class, "key");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
