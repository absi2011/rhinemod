package rhinemod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rhinemod.characters.RhineLab;
import rhinemod.util.GlobalAttributes;

public class TraitorPower extends AbstractDescriptionPower {
    public static final String POWER_ID = "rhinemod:TraitorPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public TraitorPower(AbstractCreature owner) {
        super("Tr");
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
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        // TODO: patch 在 onAttackedToChangeDamage 之后
        if (AbstractDungeon.player instanceof RhineLab && info.type == DamageInfo.DamageType.NORMAL && damageAmount >= GlobalAttributes.smashThreshold) {
            flashWithoutSound();
            return damageAmount * 2;
        } else {
            return damageAmount;
        }
    }
}
