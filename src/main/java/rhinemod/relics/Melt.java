package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Melt extends CustomRelic {

    public static final String ID = "rhinemod:Melt";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/Melt.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/Melt_p.png");
    public Melt() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        counter = 3;
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        if (counter > 0) {
            return DESCRIPTIONS[0] + counter + (counter == 1? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
        } else {
            return DESCRIPTIONS[3];
        }
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass playerClass) {
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void onVictory() {
        counter --;
        if (counter <= 0) {
            counter = -1;
        }
        updateDescription(AbstractDungeon.player.chosenClass);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Melt();
    }
}
