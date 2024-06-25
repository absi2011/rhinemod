package rhinemod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rhinemod.cards.special.Unscrupulous;

public class PioneerPower extends AbstractDescriptionPower {
    public static final String POWER_ID = "rhinemod:PioneerPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public PioneerPower(AbstractCreature owner) {
        super("Pi");
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
    public void onExhaust(AbstractCard card) {
        if (card instanceof Unscrupulous) {
            flash();
            addToBot(new DrawCardAction(1));
        }
    }
}
