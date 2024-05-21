package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

public class StarRingBlastAction extends AbstractGameAction {
    public ArrayList<AbstractCreature> aimList = new ArrayList<>();
    public final boolean includeSelf;
    public StarRingBlastAction(int amount, boolean includeSelf) {
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        this.includeSelf = includeSelf;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if (!m.isDeadOrEscaped())
                    aimList.add(m);
            if (AbstractDungeon.player.currentHealth > 0 && includeSelf) aimList.add(AbstractDungeon.player);
            if (!aimList.isEmpty()) {
                boolean playedMusic = false;
                for (AbstractCreature m : aimList) {
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.BLUNT_LIGHT, true));
                    } else {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.BLUNT_LIGHT));
                        playedMusic = true;
                    }
                }
            }
            tickDuration();
            return;
        }
        for (AbstractCreature m : aimList) {
            DamageInfo info = new DamageInfo(null, amount, DamageInfo.DamageType.THORNS);
            info.name = "StarRing";
            m.damage(info);
        }
        isDone = true;
    }
}
