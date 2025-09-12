package rhinemod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rhinemod.monsters.Awaken_Monster;

import java.util.ArrayList;

import static rhinemod.RhineMod.applyEnemyPowersOnly;

public class AwakenAction extends AbstractGameAction {
    public final ArrayList<AbstractCreature> aimList = new ArrayList<>();
    public int[] dmgList;

    public AwakenAction(int amount, AbstractMonster source) {
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        this.source = source;
        this.damageType = DamageInfo.DamageType.NORMAL;
        attackEffect = AttackEffect.BLUNT_HEAVY;
    }

    public void applySourcePower() {
        float tmp = amount;
        for (AbstractPower p:source.powers) {
            tmp = p.atDamageGive(tmp, damageType);
        }

        for (AbstractPower p:source.powers) {
            tmp = p.atDamageFinalGive(tmp, damageType);
        }
        amount = MathUtils.floor(tmp);
    }
    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            applySourcePower();
            aimList.add(AbstractDungeon.player);
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if ((!m.isDeadOrEscaped()) && (m != source))
                    aimList.add(m);
            if (aimList.isEmpty()) {
                isDone = true;
                return;
            }
            dmgList = new int[aimList.size()];
            int baseDmg = amount / aimList.size();
            for (int i = 0; i < aimList.size(); i++)
                dmgList[i] = baseDmg;
            int res = amount % aimList.size();
            for (int i = 0; i < res; i++)
                dmgList[i]++;

            boolean playedMusic = false;
            for (AbstractCreature m : aimList) {
                if (playedMusic) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, attackEffect, true));
                } else {
                    playedMusic = true;
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, attackEffect));
                }
            }
            tickDuration();
            return;
        }
        for (int i = 0; i < aimList.size(); i++) {
            DamageInfo info = new DamageInfo(source, dmgList[i], damageType);
            applyEnemyPowersOnly(info, aimList.get(i));
            aimList.get(i).damage(info);
        }
        if (source instanceof Awaken_Monster) {
            ((AbstractMonster)source).rollMove();
        }
        isDone = true;
    }
}
