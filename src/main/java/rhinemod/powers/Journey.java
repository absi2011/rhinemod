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
import rhinemod.RhineMod;

public class Journey extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:Journey";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    int usedThisTurn = 1;
    public Journey(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.owner = owner;
        if (RhineMod.tagLevel >= 3) {
            usedThisTurn = 2;
        }

        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Journey 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Journey 48.png"), 0, 0, 48, 48);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (RhineMod.tagLevel >= 3) {
            description = DESCRIPTIONS[4] + amount + DESCRIPTIONS[5] + DESCRIPTIONS[8 - usedThisTurn];
        }
        else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
            if (usedThisTurn == 0) {
                description += DESCRIPTIONS[2];
            } else {
                description += DESCRIPTIONS[3];
            }
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if ((damageAmount > 0) && (usedThisTurn > 0)) {
            usedThisTurn --;
            updateDescription();
            this.flash();
            AbstractCreature p = AbstractDungeon.player;
            this.addToTop(new DamageAction(p, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
        }

        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        usedThisTurn = 1;
        if (RhineMod.tagLevel >= 3) {
            usedThisTurn = 2;
        }
        updateDescription();
    }
}
