package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rhinemod.actions.AverageDamageAllAction;

public class Awaken extends CustomRelic {

    public static final String ID = "rhinemod:Awaken";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/Awaken.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/Awaken_p.png");
    public static final int TOTAL_DMG = 20;
    public Awaken() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.HEAVY);
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayerEndTurn() {
        counter++;
        if (counter == 3) {
            counter = 0;
            flash();
            addToBot(new AverageDamageAllAction(TOTAL_DMG, null, DamageInfo.DamageType.THORNS));
        }
    }

    @Override
    public void atBattleStart() {
        counter = 0;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Awaken();
    }
}
