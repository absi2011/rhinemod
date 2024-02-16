package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.IcefieldsCottongrassAction;
import rhinemod.patches.RhineTags;

public class IcefieldsCottongrass extends CustomCard {
    public static final String ID = "rhinemod:IcefieldsCottongrass";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/IcefieldsCottongrass.png";
    private static final int COST = 0;
    private static final int BLOCK_AMT = 4;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public IcefieldsCottongrass() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        tags.add(RhineTags.IS_PLANT);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(new DrawCardAction(1));
        addToBot(new IcefieldsCottongrassAction(this.makeStatEquivalentCopy()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new IcefieldsCottongrass();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
