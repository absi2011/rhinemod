package rhinemod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RhineDefend extends AbstractRhineCard {
    public static final String ID = "rhinemod:RhineDefend";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/rhineDefend.png";
    public static final int COST = 1;
    public static final int BLOCK_AMT = 5;
    public RhineDefend() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
        }
    }
}
