package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.ChangeGravityAction;
import rhinemod.util.GlobalAttributes;

public class GravityNone extends CustomCard {
    public static final String ID = "rhinemod:GravityNone";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/GravityNone.png";

    public GravityNone() {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION,
                CardType.POWER, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.NONE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        addToBot(new ChangeGravityAction(GlobalAttributes.GravityDirection.NONE));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GravityNone();
    }

    @Override
    public void upgrade() {}
}
