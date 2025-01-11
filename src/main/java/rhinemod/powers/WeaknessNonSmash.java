package rhinemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class WeaknessNonSmash extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:WeaknessNonSmash";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String NotTrigger = "rhinemod:Double This Damage!";
    public WeaknessNonSmash(AbstractCreature owner) {
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
}
