package rhinemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class LikeMindPowerResearch extends LikeMindPower {
    public static final String POWER_ID = "rhinemod:LikeMindPowerResearch";
    public LikeMindPowerResearch(AbstractCreature owner, int amount) {
        super(owner, amount, 2);
        this.ID = POWER_ID;
    }
}
