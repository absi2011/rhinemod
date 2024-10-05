package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rhinemod.cards.AbstractRhineCard;

import java.util.logging.Logger;

public class IdealistFormPower extends AbstractPower {
    public static final String POWER_ID = "rhinemod:IdealistFormPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int played;

    public IdealistFormPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 32.png"), 0, 0, 32, 32);
        this.amount = amount;
        played = 0;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[7];
        boolean first = true;
        if ((played & 1) != 0) {
            if (!first) description += DESCRIPTIONS[6];
            first = false;
            description += DESCRIPTIONS[2];
        }
        if ((played & 2) != 0) {
            if (!first) description += DESCRIPTIONS[6];
            first = false;
            description += DESCRIPTIONS[3];
        }
        if ((played & 4) != 0) {
            if (!first) description += DESCRIPTIONS[6];
            first = false;
            description += DESCRIPTIONS[4];
        }
        description += DESCRIPTIONS[5];
        if (first) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            if (played == 7) {
                flash();
                addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
            played = 0;
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (!(card instanceof AbstractRhineCard)) return;
        int branch = ((AbstractRhineCard) card).realBranch;
        if (branch == 0) return;
        played |= 1 << (branch - 1);
        updateDescription();
    }
}
