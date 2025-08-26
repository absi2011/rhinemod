package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class AddStunTag extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:AddStunTag";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int counter = 0;
    public AddStunTag(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = DESCRIPTIONS[7 - amount];
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.counter = amount;
        this.amount = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Armor 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Armor 32.png"), 0, 0, 32, 32);
        updateDescription();
        priority = 99999999;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + counter + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        amount --;
        if (amount == 0) {
            flash();
            addToBot(new MakeTempCardInDiscardAction(new Dazed(), 1));
            amount = counter;
        }
        updateDescription();
    }
}
