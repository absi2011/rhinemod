package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rhinemod.actions.AddCalciumAction;
import rhinemod.actions.AddFlowingShapeAction;

public class LikeMindPower extends AbstractPower implements OnReceivePowerPower {
    public static final String POWER_ID = "rhinemod:LikeMindPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int branch;
    public boolean used;
    public LikeMindPower(AbstractCreature owner, int amount, int branch) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.branch = branch;
        this.amount = amount;
        used = false;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Stunned 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Stunned 48.png"), 0, 0, 48, 48);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[4] + DESCRIPTIONS[branch] + amount + DESCRIPTIONS[5];
    }

    @Override
    public void atStartOfTurn() {
        used = false;
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (used) return true;
        if (power instanceof ResearchProgress || power instanceof InvisibleFlowShape || power instanceof InvisibleCalcium) {
            this.flash();
            used = true;
            int result = branch;
            if (result == 0) {
                result = AbstractDungeon.cardRandomRng.random(1, 3);
            }
            if (result == 1) {
                addToBot(new AddCalciumAction(amount));
            }
            else if (result == 2) {
                addToBot(new ApplyPowerAction(owner, owner, new ResearchProgress(owner, amount)));
            }
            else if (result == 3) {
                addToBot(new AddFlowingShapeAction(amount));
            }
        }
        return true;
    }

}
