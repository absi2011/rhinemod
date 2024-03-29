package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HighSpeedRTPower extends AbstractPower {
    public static final String POWER_ID = "rhinemod:HighSpeedResonatingTroubleshooter";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public AbstractPlayer source;
    public HighSpeedRTPower(AbstractCreature owner, AbstractPlayer source) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        this.source = source;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/HighSpeedResonatingTroubleshooter 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/HighSpeedResonatingTroubleshooter 32.png"), 0, 0, 32, 32);
        this.amount = 0;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        addToBot(new DamageAllEnemiesAction(source, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void onDeath() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        addToBot(new DamageAllEnemiesAction(source, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void wasHPLost(DamageInfo info, int amount) {
        this.amount += amount;
        updateDescription();
    }
}
