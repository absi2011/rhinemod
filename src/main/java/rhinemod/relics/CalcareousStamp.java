package rhinemod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.characters.RhineLab;

public class CalcareousStamp extends CustomRelic implements ClickableRelic {

    public static final String ID = "rhinemod:CalcareousStamp";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/PittsAssortedFruits.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/PittsAssortedFruits_p.png");
    public int status; // 0: costCa; 1:

    public CalcareousStamp() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
        status = 0;
        tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:Calcium")), BaseMod.getKeywordDescription("rhinemod:Calcium")));
        updateTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[status];
    }

    private void updateTips() {
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:Calcium")), BaseMod.getKeywordDescription("rhinemod:Calcium")));
        initializeTips();
    }

    @Override
    public void onRightClick() {
        status = 1 - status;
        description = getUpdatedDescription();
        updateTips();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CalcareousStamp();
    }

    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class HasEnoughEnergyPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(AbstractCard _inst) {
            if (AbstractDungeon.player.hasRelic(CalcareousStamp.ID) && ((CalcareousStamp) AbstractDungeon.player.getRelic(CalcareousStamp.ID)).status == 0 &&
                    _inst instanceof AbstractRhineCard && ((AbstractRhineCard) _inst).realBranch == 1) {
                int calciumNum = 0;
                if (AbstractDungeon.player instanceof RhineLab) {
                    calciumNum = ((RhineLab) AbstractDungeon.player).globalAttributes.calciumNum;
                }
                if (calciumNum >= _inst.costForTurn || _inst.freeToPlay() || _inst.isInAutoplay) {
                    return SpireReturn.Return(true);
                }
                _inst.cantUseMessage = DESCRIPTIONS[2];
                return SpireReturn.Return(false);
            } else {
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(EnergyPanel.class, "totalCount");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}