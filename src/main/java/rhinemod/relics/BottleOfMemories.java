package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import rhinemod.cards.Memory;

public class BottleOfMemories extends CustomRelic {

    public static final String ID = "rhinemod:BottleOfMemories";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/BottleOfMemories.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/BottleOfMemories_p.png");
    public BottleOfMemories() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SHOP, LandingSound.MAGICAL);
        counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BottleOfMemories();
    }

    @Override
    public void atTurnStartPostDraw() {
        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(new Memory(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }
}
