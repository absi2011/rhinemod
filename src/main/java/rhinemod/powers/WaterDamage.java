package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WaterDamage extends AbstractPower {
    public static final String POWER_ID = "rhinemod:WaterDamage";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int MAX_AMOUNT = 20;
    public WaterDamage(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.DEBUFF;
        this.owner = owner;
        this.amount = amount;
        updateSubmersion();
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/WaterDamage 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/WaterDamage 48.png"), 0, 0, 48, 48);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    public void updateSubmersion() {
        if (this.amount >= MAX_AMOUNT) {
            if (owner.hasPower(Submersion.POWER_ID)) {
                ((Submersion) owner.getPower(Submersion.POWER_ID)).updateLevel(this.amount / MAX_AMOUNT);
            } else {
                addToBot(new ApplyPowerAction(owner, owner, new Submersion(owner, this.amount / MAX_AMOUNT)));
            }
        }
    }

    @Override
    public void stackPower(int amount) {
        this.amount += amount;
        updateSubmersion();
        updateDescription();
    }

    @Override
    public void reducePower(int amount) {
        this.amount -= amount;
        if (this.amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            if (owner.hasPower(Submersion.POWER_ID)) {
                if (this.amount >= MAX_AMOUNT) {
                    ((Submersion) owner.getPower(Submersion.POWER_ID)).updateLevel(this.amount / MAX_AMOUNT);
                } else {
                    addToBot(new RemoveSpecificPowerAction(owner, owner, Submersion.POWER_ID));
                }
            }
        }
        updateDescription();
    }
}
