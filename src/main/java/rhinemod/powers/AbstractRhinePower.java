package rhinemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AbstractRhinePower extends AbstractPower {
    public int onSmash(AbstractCreature target, int damageAmount) {
        return damageAmount;
    }

    public int onSmashed(int damageAmount) {
        return damageAmount;
    }
}
