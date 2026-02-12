package rhinemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rhinemod.monsters.SleepingR31;

public class FeedingPower extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:FeedingPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FeedingPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        loadRegion("curiosity");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onSpecificTrigger() {
        flash();
        amount --;
        if (amount == 0) {
            ((SleepingR31)owner).Awaken();
        }
    }
}
