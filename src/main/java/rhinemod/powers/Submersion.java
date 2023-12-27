package rhinemod.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Submersion extends AbstractPower {
    public static final String POWER_ID = "rhinemod:Submersion";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int[] DAMAGE_TAKE = {0, 15, 30, 60, 999};
    public static final int[] REDUCE_ATK = {0, 25, 50, 75, 100};
    public Submersion(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        this.amount = amount;
        this.loadRegion("curiosity");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + DAMAGE_TAKE[amount] + DESCRIPTIONS[1] + REDUCE_ATK[amount] + DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
    }

    public void updateLevel(int amount) {
        this.amount = amount;
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
        updateDescription();
        if (this.amount >= 4) {
            if (owner.hasPower("Buffer")) addToTop(new RemoveSpecificPowerAction(owner, owner, "Buffer"));
            if (owner.hasPower("Invincible")) addToTop(new RemoveSpecificPowerAction(owner, owner, "Invincible"));
            if (owner.hasPower("Intangible")) addToTop(new RemoveSpecificPowerAction(owner, owner, "Intangible"));
            addToBot(new DamageAction(owner, new DamageInfo(owner, 999, DamageInfo.DamageType.HP_LOSS)));
        }
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new DamageAction(owner, new DamageInfo(null, DAMAGE_TAKE[amount], DamageInfo.DamageType.THORNS)));
        if (!owner.hasPower(Stunned.POWER_ID)) {
            addToBot(new ReducePowerAction(owner, owner, WaterDamage.POWER_ID, 10));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return damage * (100 - REDUCE_ATK[amount]) / 100.0F;
    }
}
