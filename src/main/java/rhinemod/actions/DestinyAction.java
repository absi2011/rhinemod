package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import rhinemod.cards.AbstractRhineCard;

import java.util.ArrayList;

public class DestinyAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rhinemod:DestinyAction");
    public static final String[] TEXT = uiStrings.TEXT;
    public final boolean upgraded;
    public final AbstractPlayer p = AbstractDungeon.player;
    public final ArrayList<AbstractCard> cannotUpgrade = new ArrayList<>();
    public DestinyAction(boolean upgraded) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            for (AbstractCard c : p.hand.group)
                if (!c.canUpgrade()) {
                    if (upgraded && c instanceof AbstractRhineCard && ((AbstractRhineCard) c).possibleBranches().size() > 1)
                        continue;
                    cannotUpgrade.add(c);
                }
            if (cannotUpgrade.size() == p.hand.group.size()) {
                isDone = true;
                return;
            }
            p.hand.group.removeAll(cannotUpgrade);
            if (p.hand.group.size() == 1 && !upgraded) {
                AbstractCard card = p.hand.getTopCard();
                if (card instanceof AbstractRhineCard)
                    ((AbstractRhineCard) card).randomUpgrade();
                else
                    card.upgrade();
                card.superFlash();
                card.applyPowers();
                for (AbstractCard c : cannotUpgrade)
                    p.hand.addToTop(c);
                p.hand.refreshHandLayout();
                isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, upgraded);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (c instanceof AbstractRhineCard) {
                    if (!upgraded) {
                        ((AbstractRhineCard) c).randomUpgrade();
                    } else if (c.upgraded) {
                        ((AbstractRhineCard) c).swapBranch(((AbstractRhineCard) c).chosenBranch);
                    } else {
                        c.upgrade();
                    }
                } else {
                    c.upgrade();
                }
                c.superFlash();
                c.applyPowers();
                p.hand.addToTop(c);
            }
            for (AbstractCard c : cannotUpgrade)
                p.hand.addToTop(c);
            p.hand.refreshHandLayout();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            isDone = true;
        }
    }
}
