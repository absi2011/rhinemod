package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rhinemod.actions.AddFlowingShapeAction;

public class IdealistFormPowerM extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:IdealistFormPowerM";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int drawAmount;

    public IdealistFormPowerM(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/IdealistForm 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/IdealistForm 32.png"), 0, 0, 32, 32);
        this.amount = amount;
        drawAmount = 1;
        priority = 0;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        drawAmount++;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + drawAmount + (drawAmount == 1? DESCRIPTIONS[1] : DESCRIPTIONS[2]) + amount + DESCRIPTIONS[3];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        addToBot(new DrawCardAction(drawAmount));
        addToBot(new AddFlowingShapeAction(amount));
    }
}
