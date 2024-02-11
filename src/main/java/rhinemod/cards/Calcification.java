package rhinemod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddCalciumAction;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.interfaces.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class Calcification extends AbstractRhineCard {
    public static final String ID = "rhinemod:Calcification";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/Calcification.png";
    public static final int COST = 3;
    public static final int CA_ADD = 6;
    public static final int UPGRADE_PLUS_CA = 2;
    public Calcification() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = CA_ADD;
        realBranch = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AddCalciumAction(magicNumber));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_PLUS_CA);
                    initializeDescription();
                }
            });
        }};
    }
}
