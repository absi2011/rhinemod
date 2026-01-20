package rhinemod.cards;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import rhinemod.RhineMod;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.relics.Melt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractRhineCard extends CustomCard {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rhinemod:AbstractRhineCard");
    public static final String[] TEXT = uiStrings.TEXT;
    public int baseSecondMagicNumber;
    public int secondMagicNumber;
    public boolean isSecondMagicNumberModified;
    public boolean upgradedSecondMagicNumber;
    public int chosenBranch = 0;
    public int realBranch = 0;
    public boolean isTargetStarRing = false;

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

    @Override
    public boolean canPlay(AbstractCard card) {
        if (RhineMod.getBranch(card) == 3) {
            if ((AbstractDungeon.player.hasRelic(Melt.ID)) && (AbstractDungeon.player.getRelic(Melt.ID).counter > 0)) {
                return false;
            }
        }
        return super.canPlay(card);
    }

    public abstract List<UpgradeBranch> possibleBranches();

    @Override
    public void upgrade() {
        possibleBranches().get(chosenBranch).upgrade();
    }

    public void randomUpgrade() {
        chosenBranch = AbstractDungeon.cardRng.random(possibleBranches().size() - 1);
        upgrade();
    }

    protected void upgradeName(int branchIndex) {
        timesUpgraded++;
        upgraded = true;
        if (realBranch == 0)
            realBranch = branchIndex;
        this.name = this.name + TEXT[branchIndex];
        this.initializeTitle();
    }

    protected void upgradeName(int branchIndex, String newName) {
        timesUpgraded++;
        upgraded = true;
        if (realBranch == 0)
            realBranch = branchIndex;
        this.name = newName + TEXT[branchIndex];
        this.initializeTitle();
    }

    static public void copyStat(AbstractRhineCard s, AbstractRhineCard t) {
        t.chosenBranch = s.chosenBranch;
        t.realBranch = s.realBranch;
        t.name = s.name;
        t.target = s.target;
        t.upgraded = s.upgraded;
        t.timesUpgraded = s.timesUpgraded;
        t.baseDamage = s.baseDamage;
        t.baseBlock = s.baseBlock;
        t.baseMagicNumber = s.baseMagicNumber;
        t.baseSecondMagicNumber = s.baseSecondMagicNumber;
        t.damage = s.damage;
        t.block = s.block;
        t.magicNumber = s.magicNumber;
        t.secondMagicNumber = s.secondMagicNumber;
        t.cost = s.cost;
        t.costForTurn = s.costForTurn;
        t.isCostModified = s.isCostModified;
        t.isCostModifiedForTurn = s.isCostModifiedForTurn;
        t.inBottleLightning = s.inBottleLightning;
        t.inBottleFlame = s.inBottleFlame;
        t.inBottleTornado = s.inBottleTornado;
        t.isSeen = s.isSeen;
        t.isLocked = s.isLocked;
        t.misc = s.misc;
        t.freeToPlayOnce = s.freeToPlayOnce;
        t.exhaust = s.exhaust;
        t.isEthereal = s.isEthereal;
        t.retain = s.retain;
        t.selfRetain = s.selfRetain;
        t.isInnate = s.isInnate;
        t.returnToHand = s.returnToHand;
        t.shuffleBackIntoDrawPile = s.shuffleBackIntoDrawPile;
        t.cardsToPreview = s.cardsToPreview;
        t.rawDescription = s.rawDescription;
        t.baseDraw = s.baseDraw;
        t.isInAutoplay = s.isInAutoplay;
        t.type = s.type;
        t.tags.clear();
        t.tags.addAll(s.tags);
        t.isMultiDamage = s.isMultiDamage;
        if (!t.textureImg.equals(s.textureImg)) {
            t.textureImg = s.textureImg;
            t.loadCardImage(t.textureImg);
        }
        t.initializeDescription();
    }

    @Override
    public AbstractRhineCard makeStatEquivalentCopy() {
        AbstractRhineCard card = (AbstractRhineCard) this.makeCopy();

        copyStat(this, card);
        return card;
    }

    @Override
    public AbstractRhineCard makeSameInstanceOf() {
        AbstractRhineCard card = this.makeStatEquivalentCopy();
        card.uuid = this.uuid;
        return card;
    }

    public void resetUpgrade() {
        AbstractRhineCard card = (AbstractRhineCard) this.makeCopy();
        card.inBottleLightning = inBottleLightning;
        card.inBottleFlame = inBottleFlame;
        card.inBottleTornado = inBottleTornado;
        copyStat(card, this);
    }

    public void swapBranch(int branch) {
        resetUpgrade();
        chosenBranch = branch;
        upgrade();
    }

    @Override
    public String getMetricID() {
        String id = cardID;
        if (upgraded) id = id + "*" + chosenBranch;
        return id;
    }

    public void updateKeywords() {
        Logger.getLogger(AbstractRhineCard.class.getName()).info("Start updating keywords of card: " + name);
        for (String keyword : keywords) {
            Logger.getLogger(AbstractRhineCard.class.getName()).info("keyword: " + keyword);
        }
        if (keywords.contains("rhinemod:Water_Stain") && !keywords.contains("rhinemod:Submersion")) {
            keywords.add("rhinemod:Submersion");
        }
        Logger.getLogger(AbstractRhineCard.class.getName()).info("contains 水渍？" + keywords.contains("rhinemod:水渍"));
        Logger.getLogger(AbstractRhineCard.class.getName()).info("contains 浸没？" + keywords.contains("rhinemod:浸没"));

        if (keywords.contains("rhinemod:水渍") && !keywords.contains("rhinemod:浸没")) {
            keywords.add("rhinemod:浸没");
        }
        if (keywords.contains("rhinemod:Research_Progress") && !keywords.contains("rhinemod:Perturbation")) {
            keywords.add("rhinemod:Perturbation");
        }
        if (keywords.contains("rhinemod:科研进度") && !keywords.contains("rhinemod:波骇")) {
            keywords.add("rhinemod:波骇");
        }
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
        updateKeywords();
    }

    @Override
    public void initializeDescriptionCN() {
        super.initializeDescriptionCN();
        updateKeywords();
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