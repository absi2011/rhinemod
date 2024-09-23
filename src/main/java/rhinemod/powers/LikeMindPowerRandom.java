package rhinemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class LikeMindPowerRandom extends LikeMindPower {
    public static final String POWER_ID = "rhinemod:LikeMindPowerRandom";
    public LikeMindPowerRandom(AbstractCreature owner, int amount) {
        super(owner, amount, 0);
        this.ID = POWER_ID;
    }
}
