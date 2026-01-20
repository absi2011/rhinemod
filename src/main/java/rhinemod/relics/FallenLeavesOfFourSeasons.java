package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class FallenLeavesOfFourSeasons extends CustomRelic {

    public static final String ID = "rhinemod:FallenLeavesOfFourSeasons";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/FallenLeavesOfFourSeasons.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/FallenLeavesOfFourSeasons_p.png");
    public FallenLeavesOfFourSeasons() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FallenLeavesOfFourSeasons();
    }
}
