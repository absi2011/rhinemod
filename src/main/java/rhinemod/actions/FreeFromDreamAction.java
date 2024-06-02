package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import rhinemod.powers.Stunned;

public class FreeFromDreamAction extends AbstractGameAction {
    int damage;
    int vul;
    public FreeFromDreamAction(AbstractMonster m, int damage, int vul) {
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
        this.damage = damage;
        this.vul = vul;
        this.target = m;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        target.damage(new DamageInfo(p, damage));
        int damage_take = target.lastDamageTaken;
        if (damage_take == 0) {
            isDone = true;
            return;
        }
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if ((m.getIntentBaseDmg() == -1) || (m.getIntentDmg() < damage_take))
            {
                addToBot(new ApplyPowerAction(target, p, new Stunned(target)));
                addToBot(new ApplyPowerAction(target, p, new VulnerablePower(target, vul, false)));
            }
        }
        isDone = true;
    }
}
