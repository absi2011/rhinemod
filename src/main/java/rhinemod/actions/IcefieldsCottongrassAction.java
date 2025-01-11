package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

public class IcefieldsCottongrassAction extends AbstractGameAction {
    public final AbstractCard card;
    public IcefieldsCottongrassAction(AbstractCard card) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.card = card;
    }

    @Override
    public void update() {
        int place = AbstractDungeon.shuffleRng.random(3);
        if (place == 0) addToTop(new MakeTempCardInHandAction(card));
        else if (place == 1) addToTop(new MakeTempCardInDiscardAction(card, 1));
        else if (place == 2) addToTop(new MakeTempCardInDrawPileAction(card, 1, true, true));
        else {
            card.current_x = (float)Settings.WIDTH / 2.0F;
            card.current_y = (float)Settings.HEIGHT / 2.0F;
            AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
            AbstractDungeon.player.exhaustPile.addToTop(card);
        }
        isDone = true;
    }
}
