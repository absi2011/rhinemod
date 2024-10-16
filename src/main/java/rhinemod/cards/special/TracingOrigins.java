package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.ExhaustStatusAction;
import rhinemod.powers.ResearchProgress;
import rhinemod.powers.TracingOriginsPower;

public class TracingOrigins extends CustomCard {
    public static final String ID = "rhinemod:TracingOrigins";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/rhinemod/images/cards/TracingOrigins.png";
    private static final int COST = 0;
    private static final int PROGRESS = 4;
    private static final int UPGRADE_ADD_PROGRESS = 2;

    public TracingOrigins() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        magicNumber = baseMagicNumber = PROGRESS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
        addToBot(new ExhaustStatusAction(-1));
        addToBot(new ApplyPowerAction(p, p, new TracingOriginsPower(p)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new TracingOrigins();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            upgradeMagicNumber(UPGRADE_ADD_PROGRESS);
        }
    }
}
