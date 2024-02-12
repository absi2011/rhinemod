package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PaleFir extends CustomCard {
    public static final String ID = "rhinemod:PaleFir";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/PaleFir.png";
    private static final int COST = 0;
    private static final int BLOCK_AMT = 3;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int EXTRA_BLOCK = 2;

    public PaleFir() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        exhaust = true;
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = EXTRA_BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int extraBlock = (p.maxHealth - p.currentHealth) / 5 * magicNumber;
        baseBlock += extraBlock;
        applyPowersToBlock();
        addToBot(new GainBlockAction(p, block));
        baseBlock -= extraBlock;
        applyPowersToBlock();
    }

    @Override
    public AbstractCard makeCopy() {
        return new PaleFir();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
