package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rhinemod.powers.WaterDamage;

public class IcefieldsAdventureMuAction extends AbstractGameAction {
    public final AbstractPlayer p = AbstractDungeon.player;
    public IcefieldsAdventureMuAction(int amount) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        this.attackEffect = AttackEffect.LIGHTNING; // TODO: water damage effect
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            boolean playedMusic = false;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if (!m.isDeadOrEscaped() && m.hasPower(WaterDamage.POWER_ID)) {
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, this.attackEffect, true));
                    } else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, this.attackEffect));
                    }
                }
            tickDuration();
            return;
        }
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
            if (!m.isDeadOrEscaped() && m.hasPower(WaterDamage.POWER_ID))
                m.damage(new DamageInfo(p, amount, DamageInfo.DamageType.HP_LOSS));
    }
}
