package rhinemod.cards;

import basemod.abstracts.DynamicVariable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import rs.lazymankits.abstracts.LMCustomCard;
import rs.lazymankits.interfaces.cards.BranchableUpgradeCard;
import rs.lazymankits.interfaces.cards.SwappableUpgBranchCard;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRhineCard extends LMCustomCard implements BranchableUpgradeCard, SwappableUpgBranchCard {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rhinemod:AbstractRhineCard");
    public static final String[] TEXT = uiStrings.TEXT;
    public int baseSecondMagicNumber;
    public int secondMagicNumber;
    public boolean isSecondMagicNumberModified;
    public boolean upgradedSecondMagicNumber;

    public AbstractRhineCard(String id, String name, String img, int cost, String rawDescription,
                             AbstractCard.CardType type, AbstractCard.CardColor color,
                             AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return new ArrayList<>();
    }

    public void upgradeSecondMagicNumber(int amount) {
        baseSecondMagicNumber += amount;
        secondMagicNumber = baseSecondMagicNumber;
        upgradedSecondMagicNumber = true;
    }

    public boolean extraTriggered() {
        return false;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (extraTriggered())
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        else
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    public static void addSpecificCardsToReward(AbstractCard card) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.add(card);
        addSpecificCardsToReward(cards);
    }

    public static void addSpecificCardsToReward(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            if (!card.canUpgrade()) continue;
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onPreviewObtainCard(card);
        }
        RewardItem item = new RewardItem();
        item.cards = cards;
        AbstractDungeon.getCurrRoom().addCardReward(item);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.exhaust = this.exhaust;
        card.isEthereal = this.isEthereal;
        card.rawDescription = this.rawDescription;
        card.initializeDescription();
        return card;
    }

    @Override
    public void upgrade() {
        possibleBranches().get(chosenBranch()).upgrade();
    }

    protected void upgradeName(int branchIndex) {
        timesUpgraded++;
        upgraded = true;
        this.name = this.name + TEXT[branchIndex];
        this.initializeTitle();
    }

    @Override
    public boolean allowBranchWhenUpgradeBy(int msg) {
        return true;
    }

    @Override
    public int branchForRandomUpgrading(int msg) {
        return 0;
    }

    public static class SecondMagicNumber extends DynamicVariable {
        @Override
        public String key() {
            return "rhinemod:M2";
        }

        @Override
        public boolean isModified(AbstractCard card) {
            if (card instanceof AbstractRhineCard) {
                return ((AbstractRhineCard)card).isSecondMagicNumberModified;
            } else {
                return false;
            }
        }

        @Override
        public void setIsModified(AbstractCard card, boolean v) {
            if (card instanceof AbstractRhineCard) {
                ((AbstractRhineCard)card).isSecondMagicNumberModified = v;
            }
        }

        @Override
        public int value(AbstractCard card) {
            if (card instanceof AbstractRhineCard) {
                return ((AbstractRhineCard) card).secondMagicNumber;
            } else {
                return 0;
            }
        }

        @Override
        public int baseValue(AbstractCard card) {
            if (card instanceof AbstractRhineCard) {
                return ((AbstractRhineCard) card).baseSecondMagicNumber;
            } else {
                return 0;
            }
        }

        @Override
        public boolean upgraded(AbstractCard card) {
            if (card instanceof AbstractRhineCard) {
                return ((AbstractRhineCard)card).upgradedSecondMagicNumber;
            } else {
                return false;
            }
        }
    }
}