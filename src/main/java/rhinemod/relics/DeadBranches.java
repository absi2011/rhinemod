package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DeadBranches extends CustomRelic {

    public static final String ID = "rhinemod:DeadBranches";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/DeadBranches.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/DeadBranches_p.png");
    public DeadBranches() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
        counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DeadBranches();
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
        AbstractDungeon.player.decreaseMaxHealth(15);
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }
}
