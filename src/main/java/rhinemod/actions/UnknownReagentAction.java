package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.characters.RhineLab;
import rhinemod.monsters.*;
import rhinemod.powers.SolidifyPower;

public class UnknownReagentAction extends AbstractGameAction {
    public UnknownReagentAction(int amount) {
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
    }

    @Override
    public void update() {
        for (int i = 0; i < 4; i++) {
            int damage = amount;
            AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true);
            if (m == null) {
                break;
            }
            if (m instanceof Awaken_Monster || m instanceof R11AssaultPowerArmor || m instanceof R31HeavyPowerArmor ||
                    m instanceof ExperimentalPowerArmor || m instanceof Crossroads || m instanceof Perpetrator || m instanceof Turnpike) {
                damage *= 2;
            }
            m.damage(new DamageInfo(null, damage, DamageInfo.DamageType.THORNS));
        }
        isDone = true;
    }
}
