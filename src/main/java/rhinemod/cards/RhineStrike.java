package rhinemod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RhineStrike extends AbstractRhineCard {
    public static final String ID = "rhinemod:RhineStrike";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/rhineStrike.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 6;
    public RhineStrike() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        tags.add(CardTags.STRIKE);
        tags.add(CardTags.STARTER_STRIKE);
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
