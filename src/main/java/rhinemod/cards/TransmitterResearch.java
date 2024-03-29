package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.cards.special.Unscrupulous;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class TransmitterResearch extends AbstractRhineCard {
    public static final String ID = "rhinemod:TransmitterResearch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "rhinemod/images/cards/TransmitterResearch.png";
    public static final int COST = 1;
    public static final int BASIC_RESEARCH = 7;
    public static final int BASIC_CARDS = 2;
    public static final int UPGRADE_RESEARCH = 5;
    public static final int[] UPGRADE_CARDS = {-1, 0, 1, 0};
    public TransmitterResearch() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.SELF);
        cardsToPreview = new Unscrupulous();
        baseMagicNumber = magicNumber = BASIC_RESEARCH;
        baseSecondMagicNumber = secondMagicNumber = BASIC_CARDS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
        addToBot(new MakeTempCardInDiscardAction(cardsToPreview, secondMagicNumber));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeSecondMagicNumber(UPGRADE_CARDS[0]);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    upgradeMagicNumber(UPGRADE_RESEARCH);
                    upgradeSecondMagicNumber(UPGRADE_CARDS[2]);
                    initializeDescription();
                }
            });
        }};
    }
}
