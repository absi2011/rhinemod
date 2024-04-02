package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.powers.AIODPower;
import rhinemod.powers.PioneerPower;
import rhinemod.powers.ResearchProgress;

public class AttackInsteadOfDefend extends CustomCard {
    public static final String ID = "rhinemod:AttackInsteadOfDefend";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "resources/rhinemod/images/cards/Unscrupulous.png";
    private static final int COST = 0;
    private static final int MIN_DAMAGE = 9;
    private static final int UPGRADE_PLUS_MIN_DAMAGE = 2;

    public AttackInsteadOfDefend() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, CardColor.COLORLESS,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = MIN_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AIODPower(p, magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AttackInsteadOfDefend();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MIN_DAMAGE);
        }
    }
}
