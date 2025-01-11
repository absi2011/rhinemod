package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.Settings;
import rhinemod.cards.LoneLight;

public class IdealistFormKAction extends AbstractGameAction {
    public IdealistFormKAction(int amount) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
    }
    @Override
    public void update() {
        for (int i = 0; i < amount; i++)
            GameActionManager.queueExtraCard(new LoneLight(), null);
        isDone = true;
    }
}
