package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OrangeStorm extends CustomRelic {

    public static final String ID = "rhinemod:OrangeStorm";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/OrangeStorm.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/OrangeStorm_p.png");
    public static final int MAX_HP = 3;
    public OrangeStorm() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(MAX_HP, true);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OrangeStorm();
    }
}
