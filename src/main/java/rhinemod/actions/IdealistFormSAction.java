package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.cards.special.Unscrupulous;

import java.util.ArrayList;

import static java.util.Collections.reverse;

public class IdealistFormSAction extends AbstractGameAction {
    public IdealistFormSAction(int amount) {
        actionType = ActionType.EXHAUST;
        duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
    }
    @Override
    public void update() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.hand.group)
            if (c instanceof Unscrupulous)
                list.add(c);
        int cnt = list.size();
        if (cnt == 0) {
            isDone = true;
            return;
        }
        addToTop(new AddCalciumAction(cnt * amount));
        reverse(list);
        for (AbstractCard c : list)
            addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
        isDone = true;
    }
}
