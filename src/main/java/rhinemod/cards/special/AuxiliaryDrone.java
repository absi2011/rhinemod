package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.powers.AuxiliaryDronePower;

public class AuxiliaryDrone extends CustomCard {
    public static final String ID = "rhinemod:AuxiliaryDrone";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/rhinemod/images/cards/AuxiliaryDrone.png";
    private static final int COST = 1;
    private static final int DAMAGE_OUT = 1;
    private static final int UPGRADE_DAMAGE_OUT = 1;

    public AuxiliaryDrone() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = DAMAGE_OUT;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AuxiliaryDronePower(p, magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AuxiliaryDrone();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            upgradeMagicNumber(UPGRADE_DAMAGE_OUT);
        }
    }
}
