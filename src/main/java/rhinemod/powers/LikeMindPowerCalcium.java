package rhinemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class LikeMindPowerCalcium extends LikeMindPower {
    public static final String POWER_ID = "rhinemod:LikeMindPowerCalcium";
    public LikeMindPowerCalcium(AbstractCreature owner, int amount) {
        super(owner, amount, 1);
        this.ID = POWER_ID;
    }
}
