package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rhinemod.actions.AddCalciumAction;

public class RhineChargeSuit extends CustomRelic {

    public static final String ID = "rhinemod:RhineChargeSuit";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/RhineChargeSuit.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/RhineChargeSuit_p.png");
    public static final int ADD_CA = 2;
    public RhineChargeSuit() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStartPostDraw() {
        addToBot(new AddCalciumAction(ADD_CA));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RhineChargeSuit();
    }
}
