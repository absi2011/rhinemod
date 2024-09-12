package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DamageOutPower extends AbstractPower {
    public static final String POWER_ID = "rhinemod:DamageOutPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    int decAmount;
    public DamageOutPower(AbstractCreature owner, int amount,int decAmount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.decAmount = decAmount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 32.png"), 0, 0, 32, 32);
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= 100) {
            amount = 100;
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + decAmount + DESCRIPTIONS[2];
    }

    @Override
    public void atStartOfTurn() {

    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        return damage * (100 - amount) / 100;
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            this.flash();
            this.addToBot(new ReducePowerAction(this.owner, this.owner, "rhinemod:DamageOutPower", decAmount));
        }
    }


}
