package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

import static rhinemod.RhineMod.applyEnemyPowersOnly;

public class DamageAllAction extends AbstractGameAction {
    public final ArrayList<AbstractCreature> aimList = new ArrayList<>();
    public int[] dmgList;
    public DamageAllAction(int amount, AbstractCreature source, DamageInfo.DamageType type) {
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        this.source = source;
        this.damageType = type;
        attackEffect = AttackEffect.BLUNT_LIGHT;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if (!m.isDeadOrEscaped())
                    aimList.add(m);
            aimList.add(AbstractDungeon.player);
            dmgList = new int[aimList.size()];
            for (int i = 0; i < aimList.size(); i++)
                dmgList[i] = amount;


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
        isDone = true;
    }
}
