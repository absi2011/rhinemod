package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.patches.RhineTags;
import rhinemod.powers.Stunned;
import rhinemod.powers.WaterDamage;

public class Sarracenia extends CustomCard {
    public static final String ID = "rhinemod:Sarracenia";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "resources/rhinemod/images/cards/Sarracenia.png";
    private static final int COST = 0;
    private static final int WATER_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 2;

    public Sarracenia() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = WATER_DMG;
        tags.add(RhineTags.IS_PLANT);
        tags.add(RhineTags.APPLY_WATER);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new WaterDamage(m, magicNumber)));
        addToBot(new ApplyPowerAction(m, p, new Stunned(m)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Sarracenia();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DMG);
        }
    }
}
