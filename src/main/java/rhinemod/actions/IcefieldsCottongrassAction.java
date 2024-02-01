package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class IcefieldsCottongrassAction extends AbstractGameAction {
    public final AbstractCard card;
    public IcefieldsCottongrassAction(AbstractCard card) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.card = card;
    }

    @Override
    public void update() {
        int place = AbstractDungeon.shuffleRng.random(2);
        if (place == 0) addToTop(new MakeTempCardInHandAction(card));
        else if (place == 1) addToTop(new MakeTempCardInDiscardAction(card, 1));
        else addToTop(new MakeTempCardInDrawPileAction(card, 1, true, true));
        isDone = true;
    }
}
