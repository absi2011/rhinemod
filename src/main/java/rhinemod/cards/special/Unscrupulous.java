package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.powers.PioneerPower;
import rhinemod.powers.ResearchProgress;

public class Unscrupulous extends CustomCard {
    public static final String ID = "rhinemod:Unscrupulous";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/Unscrupulous.png";
    private static final int COST = 0;
    private static final int ADD_RESEARCH_PROGRESS = -2;

    public Unscrupulous() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.STATUS, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        magicNumber = baseMagicNumber = ADD_RESEARCH_PROGRESS;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.hasPower(PioneerPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, baseMagicNumber)));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Unscrupulous();
    }

    @Override
    public void upgrade() {
    }
}
