package rhinemod.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.characters.RhineLab;
import rhinemod.util.GlobalAttributes;

public class InvisibleGlobalAttributes extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:InvisibleGlobalAttributes";
    public final RhineLab p;
    public InvisibleGlobalAttributes() {
        ID = POWER_ID;
        type = PowerType.BUFF;
        this.priority = -100;
        updateDescription();
        p = (RhineLab) AbstractDungeon.player;
    }

    @Override
    public void updateDescription() {
        description = "";
    }

    public float calcDmgScale() {
        float scale = 1.0F;
        float gravityAffect = 0.3F;
        if (p.hasPower(LonerPower.POWER_ID)) gravityAffect = 0.5F;
        if (p.globalAttributes.gravity == GlobalAttributes.GravityDirection.UP)
            scale += gravityAffect;
        else if (p.globalAttributes.gravity == GlobalAttributes.GravityDirection.DOWN)
            scale -= gravityAffect;
        return scale;
    }

    public float calcCalcium(AbstractCard c) {
        if (!(c instanceof AbstractRhineCard)) return 0;
        if (((AbstractRhineCard)c).realBranch == 1)
            return p.globalAttributes.calciumNum;
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
        if (!(card instanceof AbstractRhineCard)) return;
        if (card.isInAutoplay) return;
        if (((AbstractRhineCard)card).realBranch != 1 && ((AbstractRhineCard)card).realBranch != 2) return;
        int flowspNum = p.globalAttributes.flowspNum;
        if (flowspNum == 0) return;

        AbstractMonster m = action.target == null? null : (AbstractMonster)action.target;
        AbstractRhineCard tmp = ((AbstractRhineCard) card).makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(tmp);

        tmp.current_x = card.current_x;
        tmp.current_y = card.current_y;
        tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        tmp.target_y = Settings.HEIGHT / 2.0F;

        float multi = flowspNum * 0.2F;
        float eps = 0.0001F;
        tmp.baseDamage = MathUtils.ceil(tmp.baseDamage * multi - eps);
        tmp.baseBlock = MathUtils.ceil(tmp.baseBlock * multi - eps);
        tmp.magicNumber = tmp.baseMagicNumber = MathUtils.ceil(tmp.baseMagicNumber * multi - eps);
        tmp.secondMagicNumber = tmp.baseSecondMagicNumber = MathUtils.ceil(tmp.baseSecondMagicNumber * multi - eps);
        tmp.realBranch = -1;
        if (m != null) tmp.calculateCardDamage(m);
        tmp.purgeOnUse = true;
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
        p.globalAttributes.clearFlowsp();
    }
}
