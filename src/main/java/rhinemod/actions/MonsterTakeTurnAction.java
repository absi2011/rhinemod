package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MonsterTakeTurnAction extends AbstractGameAction {
    AbstractMonster m;
    public MonsterTakeTurnAction(AbstractMonster m) {
        actionType = ActionType.SPECIAL;
        duration = startDuration = Settings.ACTION_DUR_FAST;
        this.m = m;
    }

    @Override
    public void update() {
        m.takeTurn();
        isDone = true;
    }
}
