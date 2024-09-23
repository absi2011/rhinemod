package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ExhaustStatusAction extends AbstractGameAction {
    public final AbstractPlayer p;
    public ExhaustStatusAction(int amount) {
        actionType = ActionType.EXHAUST;
        duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
        p = AbstractDungeon.player;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : p.drawPile.group)
            if (c.type == AbstractCard.CardType.STATUS)
                list.add(c);
        for (AbstractCard c : p.hand.group)
            if (c.type == AbstractCard.CardType.STATUS)
                list.add(c);
        for (AbstractCard c : p.discardPile.group)
            if (c.type == AbstractCard.CardType.STATUS)
                list.add(c);
        if ((list.size() <= amount) || (amount == -1)) {
            for (AbstractCard c : list) exhaustCard(c);
        } else {
            Collections.shuffle(list, new Random(AbstractDungeon.cardRng.randomLong()));
            for (int i = 0; i < amount; i++) exhaustCard(list.get(i));
        }
        isDone = true;
    }

    public void exhaustCard(AbstractCard c) {
        if (p.hand.contains(c)) p.hand.moveToExhaustPile(c);
        else if (p.discardPile.contains(c)) p.discardPile.moveToExhaustPile(c);
        else p.drawPile.moveToExhaustPile(c);
    }
}
