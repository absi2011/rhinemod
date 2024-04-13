package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rhinemod.actions.AverageDamageAllAction;

public class Melt extends CustomRelic {

    public static final String ID = "rhinemod:Melt";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/Awaken.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/Awaken_p.png");
    public Melt() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        counter = 3;
        description = getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        if (counter > 0) {
            return DESCRIPTIONS[0] + counter + DESCRIPTIONS[1];
        }
        else {
            return DESCRIPTIONS[2];
        }
    }


    @Override
    public void atBattleStart() {
        counter --;
        if (counter < 0) {
            counter = -1;
        }
        description = getUpdatedDescription();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Melt();
    }
}
