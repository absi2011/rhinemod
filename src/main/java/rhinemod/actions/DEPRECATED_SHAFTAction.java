package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import rhinemod.cards.special.Traitor;
import rhinemod.cards.special.Unscrupulous;

public class DEPRECATED_SHAFTAction extends AbstractGameAction {
    public final int chosenBranch;
    public DEPRECATED_SHAFTAction(int chosenBranch, int amount) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
        this.chosenBranch = chosenBranch;
        this.amount = amount;
    }

    @Override
    public void update() {
        boolean isTypeSame = true;
        if (DrawCardAction.drawnCards.size() != 0) {
            AbstractCard.CardType type = DrawCardAction.drawnCards.get(0).type;
            for (AbstractCard c : DrawCardAction.drawnCards)
                if (c.type != type) {
                    isTypeSame = false;
                    break;
                }
        }
        if (chosenBranch == 0 && !isTypeSame) addToTop(new MakeTempCardInDrawPileAction(new Unscrupulous(), amount, true, true));
        if (chosenBranch == 1 && isTypeSame) addToTop(new MakeTempCardInHandAction(new Traitor()));
        isDone = true;
    }
}
