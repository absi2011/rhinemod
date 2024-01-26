package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ThreeDimensionArtDisplay extends CustomRelic {

    public static final String ID = "rhinemod:ThreeDimensionArtDisplay";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/3DArtDisplay.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/3DArtDisplay.png");
    public static final int IMMEDIATE_HEAL = 14;
    public static final int BATTLE_HEAL = 2;
    public ThreeDimensionArtDisplay() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SHOP, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.heal(IMMEDIATE_HEAL);
    }

    @Override
    public void onVictory() {
        AbstractPlayer p = AbstractDungeon.player;
        addToTop(new RelicAboveCreatureAction(p, this));
        if (p.currentHealth > 0) p.heal(BATTLE_HEAL);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ThreeDimensionArtDisplay();
    }
}
