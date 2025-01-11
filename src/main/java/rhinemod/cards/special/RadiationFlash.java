package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import rhinemod.powers.Stunned;

public class RadiationFlash extends CustomCard {
    public static final String ID = "rhinemod:RadiationFlash";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/rhinemod/images/cards/RadiationFlash.png";
    private static final int COST = 2;
    private static final int DEBUFF_NUM = 2;
    private static final int STRENGTH_LOSE = 3;
    private static final int UPGRADE_PLUS_DEBUFF_NUM = 1;
    private static final int UPGRADE_PLUS_STRENGTH_LOSE = 1;
    private int secondMagicNumber;

    public RadiationFlash() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = DEBUFF_NUM;
        secondMagicNumber = STRENGTH_LOSE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            int times = 1;
            if (mo.hasPower(Stunned.POWER_ID)) times = 2;
            for (int i = 0; i < times; i++) {
                addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, magicNumber, false)));
                addToBot(new ApplyPowerAction(mo, p, new Stunned(mo)));
                addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -secondMagicNumber)));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RadiationFlash();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DEBUFF_NUM);
            secondMagicNumber += UPGRADE_PLUS_STRENGTH_LOSE;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
