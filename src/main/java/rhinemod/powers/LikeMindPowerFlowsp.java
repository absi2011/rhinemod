package rhinemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class LikeMindPowerFlowsp extends LikeMindPower {
    public static final String POWER_ID = "rhinemod:LikeMindPowerFlowsp";
    public LikeMindPowerFlowsp(AbstractCreature owner, int amount) {
        super(owner, amount, 3);
        this.ID = POWER_ID;
    }
}
