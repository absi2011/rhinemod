package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Journey extends AbstractPower {
    public static final String POWER_ID = "rhinemod:Journey";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    boolean usedThisTurn = false;
    public Journey(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.owner = owner;

        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/BionicDevice 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/BionicDevice 32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        if (usedThisTurn) {
            description += DESCRIPTIONS[2];
        }
        else {
            description += DESCRIPTIONS[3];
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if ((damageAmount > 0) && (!usedThisTurn)) {
            usedThisTurn = true;
            updateDescription();
            this.flash();
            AbstractCreature p = AbstractDungeon.player;
            this.addToTop(new DamageAction(p, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
        }

        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        usedThisTurn = false;
        updateDescription();
    }


}
