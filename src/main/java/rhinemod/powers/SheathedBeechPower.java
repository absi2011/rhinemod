package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;

public class SheathedBeechPower extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:SheathedBeechPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int secondAmount;
    public SheathedBeechPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 32.png"), 0, 0, 32, 32);
        this.amount = amount;
        this.secondAmount = 1;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        amount += stackAmount;
        secondAmount++;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + secondAmount + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.type == AbstractCard.CardType.SKILL) {
            addToTop(new ApplyPowerAction(owner, owner, new DexterityPower(owner, secondAmount)));
            addToTop(new ApplyPowerAction(owner, owner, new LoseDexterityPower(owner, secondAmount)));
            addToTop(new GainBlockAction(owner, amount));
        }
    }
}
