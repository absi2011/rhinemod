package rhinemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class WeaknessSmash extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:WeaknessSmash";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public WeaknessSmash(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        loadRegion("curiosity");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public int onSmashed(int damageAmount) {
        flash();
        return damageAmount * 2;
    }
}
