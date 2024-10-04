package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rhinemod.vfx.InvisibleSmokeEffect;

public class InvisiblePower extends AbstractPower {
    public static final String POWER_ID = "rhinemod:InvisiblePower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private InvisibleSmokeEffect effect;
    public InvisiblePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        effect = new InvisibleSmokeEffect(owner.hb.cX, owner.hb.cY);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    @Override
    public void onRemove() {
        if (effect != null) effect.endEffect();
    }
}
