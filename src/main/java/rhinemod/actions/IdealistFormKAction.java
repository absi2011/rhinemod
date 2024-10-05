package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.cards.LoneLight;
import rhinemod.cards.special.Unscrupulous;

import java.util.ArrayList;

import static java.util.Collections.reverse;

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
