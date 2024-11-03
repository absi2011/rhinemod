package rhinemod.powers;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rhinemod.actions.GainProgressByCostAction;

public class RefreshmentPower extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:RefreshmentPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int rest;
    public RefreshmentPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        rest = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/RefreshmentPower 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/RefreshmentPower 32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + rest + (rest <= 1? DESCRIPTIONS[2] : DESCRIPTIONS[3]);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        rest += stackAmount;
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        rest = amount;
        updateDescription();
    }

    @Override
    public void onGainedBlock(float blockAmount) {
        if ((blockAmount > 0.0F) && (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) && (rest > 0)) {
            this.flash();
            rest --;
            updateDescription();
            addToBot(new DrawCardAction(1, new GainProgressByCostAction(AbstractDungeon.player)));
        }
    }
}
