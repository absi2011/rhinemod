package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rhinemod.util.GlobalAttributes;

public class WeaknessNonSmash extends AbstractPower {
    public static final String POWER_ID = "rhinemod:WeaknessNonSmash";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String NotTrigger = "rhinemod:Double This Damage!";
    public WeaknessNonSmash(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {

    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if ((damageAmount > 0) && (damageAmount < GlobalAttributes.smashThreshold || info.type != DamageInfo.DamageType.NORMAL) && ((info.name == null) || (!info.name.equals(NotTrigger)))) {
            this.flash();
            DamageInfo newInfo = new DamageInfo(owner, damageAmount, DamageInfo.DamageType.THORNS);
            newInfo.name = NotTrigger;
            addToBot(new DamageAction(owner, newInfo));
        }
        return  damageAmount;
    }


}
