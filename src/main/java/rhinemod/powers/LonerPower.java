package rhinemod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rhinemod.cards.special.Lean;

public class LonerPower extends AbstractDescriptionPower {
    public static final String POWER_ID = "rhinemod:LonerPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public LonerPower(AbstractCreature owner) {
        super("Lo");
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new MakeTempCardInHandAction(new Lean()));
    }
}
