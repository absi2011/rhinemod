package rhinemod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Imperishable extends CustomRelic implements ClickableRelic {

    public static final String ID = "rhinemod:Imperishable";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/Imperishable.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/Imperishable_p.png");
    public int status; // 0: double; 1: half

    public Imperishable() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
        status = 0;
        tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:ExperimentError")), BaseMod.getKeywordDescription("rhinemod:ExperimentError")));
        updateTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[status];
    }

    private void updateTips() {
        tips.clear();
        tips.add(new PowerTip(name, description));
        if (status == 0) {
            tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:ExperimentError")), BaseMod.getKeywordDescription("rhinemod:ExperimentError")));
        } else {
            tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("rhinemod:CriticalPoint")), BaseMod.getKeywordDescription("rhinemod:CriticalPoint")));
        }
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
        return new Imperishable();
    }
}
