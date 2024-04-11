package rhinemod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rhinemod.cards.AbstractRhineCard;

public class LonerPower extends AbstractDescriptionPower {
    public static final String POWER_ID = "rhinemod:LonerPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public LonerPower(AbstractCreature owner, int amount) {
        super("Lo");
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        for (AbstractCard c : AbstractDungeon.player.hand.group)
            if (!(c instanceof AbstractRhineCard) || ((AbstractRhineCard) c).realBranch != 2)
                return;
        addToBot(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }
}
