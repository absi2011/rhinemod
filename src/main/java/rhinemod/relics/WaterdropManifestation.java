package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rhinemod.patches.RhineTags;

public class WaterdropManifestation extends CustomRelic implements ClickableRelic {

    public static final String ID = "rhinemod:WaterdropManifestation";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/PittsAssortedFruits.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/PittsAssortedFruits_p.png");
    public int status; // 0: energy; 1: draw

    public WaterdropManifestation() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
        counter = 0;
        status = 0;
        updateTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[status];
    }

    private void updateTips() {
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void onRightClick() {
        status = 1 - status;
        description = getUpdatedDescription();
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (targetCard.hasTag(RhineTags.IS_PLANT)) {
            counter++;
            if (counter >= 4) {
                flash();
                counter = 0;
                if (status == 0) addToBot(new GainEnergyAction(1));
                else addToBot(new DrawCardAction(2));
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WaterdropManifestation();
    }
}
