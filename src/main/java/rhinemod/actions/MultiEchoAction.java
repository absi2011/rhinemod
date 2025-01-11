package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import rhinemod.cards.MultiEcho;
import rhinemod.characters.RhineLab;

import java.util.ArrayList;

public class MultiEchoAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rhinemod:MultiEchoAction");
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String[] EXTENDED = CardCrawlGame.languagePack.getCardStrings(MultiEcho.ID).EXTENDED_DESCRIPTION;
    public final AbstractPlayer p = AbstractDungeon.player;
    public MultiEchoAction() {
        actionType = ActionType.SPECIAL;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    public final ArrayList<AbstractCard> cannotChoose = new ArrayList<>();
    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            for (AbstractCard c : p.hand.group)
                if (c instanceof MultiEcho) {
                    cannotChoose.add(c);
                }
            if (cannotChoose.size() == p.hand.group.size()) {
                isDone = true;
                return;
            }
            p.hand.group.removeAll(cannotChoose);
            if (p.hand.group.size() == 1) {
                AbstractCard card = p.hand.getTopCard();
                card.superFlash();
                card.applyPowers();
                for (AbstractCard c : cannotChoose)
                    p.hand.addToTop(c);
                p.hand.refreshHandLayout();
                useMultiEcho(card);
                isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            AbstractCard card = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);
            card.superFlash();
            card.applyPowers();
            p.hand.addToTop(card);
            for (AbstractCard c : cannotChoose)
                p.hand.addToTop(c);
            p.hand.refreshHandLayout();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            useMultiEcho(card);
            isDone = true;
        }
    }

    void useMultiEcho(AbstractCard card) {
        ArrayList<AbstractGameAction> actions = new ArrayList<>();
        for (AbstractCard c: p.hand.group) {
            if (c != card) {
                actions.add(0, new DiscardSpecificCardAction(c));
            }
        }
        if (p instanceof RhineLab) {
            int cnt = ((RhineLab) p).globalAttributes.flowspNum;
            if (cnt >= 9) {
                cnt = 9;
            }
            actions.add(0, new AddFlowingShapeAction(-cnt));
            for (int i = 0; i < cnt; i ++) {
                AbstractCard c = card.makeStatEquivalentCopy();
                c.selfRetain = true;
                c.exhaust = true;
                if (!c.name.startsWith(EXTENDED[0])) {
                    c.name = EXTENDED[0] + c.name;
                    c.rawDescription += EXTENDED[1];
                }
                c.initializeDescription();
                actions.add(0, new MakeTempCardInHandAction(c));
            }
        }
        for (AbstractGameAction a : actions) {
            addToTop(a);
        }
    }
}
