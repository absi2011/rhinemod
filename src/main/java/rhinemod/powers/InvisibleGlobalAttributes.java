package rhinemod.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.characters.RhineLab;
import rhinemod.util.GlobalAttributes;

public class InvisibleGlobalAttributes extends AbstractPower {
    public static final String POWER_ID = "rhinemod:InvisibleGlobalAttributes";
    public InvisibleGlobalAttributes() {
        ID = POWER_ID;
        type = PowerType.BUFF;
        this.priority = -100;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = "";
    }

    public float calcDmgScale() {
        AbstractPlayer p = AbstractDungeon.player;
        float scale = 1.0F;
        if (((RhineLab)p).globalAttributes.gravity == GlobalAttributes.GravityDirection.UP)
            scale *= 1.3F;
        else if (((RhineLab)p).globalAttributes.gravity == GlobalAttributes.GravityDirection.DOWN)
            scale /= 1.3F;
        return scale;
    }

    public float calcCalcium(AbstractCard c) {
        AbstractPlayer p = AbstractDungeon.player;
        if (!(p instanceof RhineLab) || !(c instanceof AbstractRhineCard)) return 0;
        if (((AbstractRhineCard)c).realBranch == 1)
            return ((RhineLab)p).globalAttributes.calciumNum;
        else
            return 0;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard c) {
        return damage + calcCalcium(c);
    }

    @Override
    public float modifyBlock(float block, AbstractCard c) {
        return block + calcCalcium(c);
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) damage *= calcDmgScale();
        return damage;
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) damage *= calcDmgScale();
        return damage;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        AbstractPlayer p = AbstractDungeon.player;
        if (!(p instanceof RhineLab) || !(card instanceof AbstractRhineCard)) return;
        if (((AbstractRhineCard)card).realBranch != 1 && ((AbstractRhineCard)card).realBranch != 2) return;
        int flowspNum = ((RhineLab)p).globalAttributes.flowspNum;
        if (flowspNum == 0) return;

        AbstractMonster m = action.target == null? null : (AbstractMonster)action.target;
        AbstractCard tmp = card.makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(tmp);

        tmp.current_x = card.current_x;
        tmp.current_y = card.current_y;
        tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        tmp.target_y = Settings.HEIGHT / 2.0F;

        float multi = flowspNum * 0.25F;
        tmp.baseDamage = MathUtils.floor(tmp.baseDamage * multi);
        tmp.baseBlock = MathUtils.floor(tmp.baseBlock * multi);
        if (m != null) tmp.calculateCardDamage(m);
        tmp.purgeOnUse = true;
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
        ((RhineLab)p).globalAttributes.clearFlowsp();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount >= GlobalAttributes.smashThreshold) {
            addToTop(new ApplyPowerAction(target, owner, new Stunned(target)));
        }
    }
}
