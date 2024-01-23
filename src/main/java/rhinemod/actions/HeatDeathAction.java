package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HeatDeathAction extends AbstractGameAction {
    public HeatDeathAction() {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        int totalHealth = 0;
        int monsterNum = 0;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
            if (!m.isDeadOrEscaped()) {
                monsterNum++;
                totalHealth += m.currentHealth;
            }
        if (monsterNum > 0) {
            int averageHealth = totalHealth / monsterNum;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if (!m.isDeadOrEscaped()) {
                    m.currentHealth = Math.min(averageHealth, m.currentHealth);
                    m.healthBarUpdatedEvent();
                }
        }
        isDone = true;
    }
}
