package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.characters.RhineLab;
import rhinemod.powers.Stunned;

public class ChangeGravityAction extends AbstractGameAction {
    public ChangeGravityAction() {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        ((RhineLab)AbstractDungeon.player).globalAttributes.changeGravity();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
            if (!m.isDeadOrEscaped())
                if (m.hasPower(Stunned.POWER_ID))
                    ((Stunned)m.getPower(Stunned.POWER_ID)).onGravityChange();
        isDone = true;
    }
}
