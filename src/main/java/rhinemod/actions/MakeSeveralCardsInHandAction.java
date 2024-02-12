package rhinemod.actions;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class MakeSeveralCardsInHandAction extends AbstractGameAction {
    private final ArrayList<AbstractCard> list;
    public MakeSeveralCardsInHandAction(ArrayList<AbstractCard> list) {
        this.list = list;
        for (AbstractCard c : list)
            UnlockTracker.markCardAsSeen(c.cardID);
        amount = list.size();
        actionType = ActionType.CARD_MANIPULATION;
        if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
            for (AbstractCard c : list) {
                if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS)
                    c.upgrade();
            }
        }
    }

    public void update() {
        if (amount != 0) {
            int handAmount = amount;
            if (amount + AbstractDungeon.player.hand.size() > BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.player.createHandIsFullDialog();
                handAmount = BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size();
            }
            for (int i = 0; i < handAmount; i++)
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(list.get(i).makeStatEquivalentCopy(), MathUtils.random((float) Settings.WIDTH * 0.2F, (float) Settings.WIDTH * 0.8F), MathUtils.random((float) Settings.HEIGHT * 0.3F, (float) Settings.HEIGHT * 0.7F)));
            for (int i = handAmount; i < amount; i++)
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(list.get(i).makeStatEquivalentCopy(), MathUtils.random((float) Settings.WIDTH * 0.2F, (float) Settings.WIDTH * 0.8F), MathUtils.random((float) Settings.HEIGHT * 0.3F, (float) Settings.HEIGHT * 0.7F)));
            addToTop(new WaitAction(0.8F));
        }
        isDone = true;
    }
}
