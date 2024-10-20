package rhinemod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rhinemod.util.GlobalAttributes;

public class ExperimentalPowerArmorRelic extends CustomRelic {

    public static final String ID = "rhinemod:ExperimentalPowerArmorRelic";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/ExperimentalPowerArmorRelic.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/ExperimentalPowerArmorRelic_p.png");
    public ExperimentalPowerArmorRelic() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:smash")), BaseMod.getKeywordDescription("rhinemod:smash")));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount < GlobalAttributes.smashThreshold) {
            flash();
            return damageAmount / 2;
        } else {
            return damageAmount;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ExperimentalPowerArmorRelic();
    }
}
