package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rhinemod.RhineMod;

public class BlockDamage extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:BlockDamage";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public BlockDamage(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Armor 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Armor 32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (RhineMod.tagLevel >= 1) description = DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
        else description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner == AbstractDungeon.player) {
            if (damageAmount >= amount) {
                flash();
                damageAmount = amount;
            }
            if ((RhineMod.tagLevel >= 1) && (damageAmount > 0)) {
                flash();
                addToTop(new ApplyPowerAction(owner, owner, new BlockDamage(owner, 1)));
                addToTop(new ApplyPowerAction(owner, owner, new DreamBreakPower(owner, amount)));
            }
        }
        return damageAmount;
    }
}
