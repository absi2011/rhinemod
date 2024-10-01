package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DreadnoughtProtocolPower extends AbstractPower {
    public static final String POWER_ID = "rhinemod:DreadnoughtProtocolPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public boolean isTriggered;
    public DreadnoughtProtocolPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/DreadnoughtProtocolPower 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/DreadnoughtProtocolPower 32.png"), 0, 0, 32, 32);
        priority = 100;
        isTriggered = false;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onVictory() {
        if (!isTriggered && owner.currentHealth > 0) owner.heal(amount);
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (damageAmount >= owner.currentHealth) {
            isTriggered = true;
            return owner.currentHealth - 1;
        }
        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        if (isTriggered) addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
