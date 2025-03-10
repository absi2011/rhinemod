package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rhinemod.actions.SubmersionLoseHpAction;
import rhinemod.cards.Dreamer;
import rhinemod.monsters.StarPod;
import rhinemod.patches.WaterAttackEffectPatch;

public class Submersion extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:Submersion";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int[] DAMAGE_TAKE = {0, 15, 30, 60, 999};
    public static final int[] REDUCE_ATK = {0, 25, 50, 75, 100};
    private boolean triggered;
    public Submersion(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        this.amount = Math.min(4, amount);
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Submersion 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Submersion 48.png"), 0, 0, 48, 48);
        priority = 100;
        triggered = false;
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        updateLevel(amount);
    }

    @Override
    public void stackPower(int amount) {
        super.stackPower(amount);
        this.amount = Math.min(4, this.amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + DAMAGE_TAKE[amount] + DESCRIPTIONS[1] + REDUCE_ATK[amount] + DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
    }

    public void updateLevel(int amount) {
        if (amount < this.amount) {
            addToTop(new ReducePowerAction(owner, owner, POWER_ID, this.amount - amount));
        } else if (amount > this.amount) {
            addToTop(new ApplyPowerAction(owner, owner, new Submersion(owner, amount - this.amount)));
        }
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
        updateDescription();
        if (amount >= 4) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(owner.hb.cX, owner.hb.cY, WaterAttackEffectPatch.WATER));
            updateDreamer(AbstractDungeon.player.masterDeck);
            updateDreamer(AbstractDungeon.player.hand);
            updateDreamer(AbstractDungeon.player.drawPile);
            updateDreamer(AbstractDungeon.player.discardPile);
            updateDreamer(AbstractDungeon.player.exhaustPile);
            if (owner instanceof StarPod) {
                addToBot(new SubmersionLoseHpAction(owner, AbstractDungeon.player, DAMAGE_TAKE[amount]));
            }
            else {
                addToBot(new InstantKillAction(owner));
            }
        }
    }

    void updateDreamer(CardGroup cardGroup) {
        for (AbstractCard c : cardGroup.group) {
            if ((c instanceof Dreamer) && (((Dreamer)c).chosenBranch == 3)) {
                c.misc += c.baseMagicNumber;
                if (cardGroup == AbstractDungeon.player.hand) {
                    c.applyPowers();
                }
                else {
                    c.baseDamage = c.misc;
                }
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new SubmersionLoseHpAction(owner, AbstractDungeon.player, DAMAGE_TAKE[amount]));
        triggered = true;
    }

    public void atEndOfRound() {
        if ((!owner.hasPower(Stunned.POWER_ID)) && triggered) {
            addToBot(new ReducePowerAction(owner, owner, WaterDamage.POWER_ID, 5));
            triggered = false;
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return damage * (100 - REDUCE_ATK[amount]) / 100.0F;
    }
}
