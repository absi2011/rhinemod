package rhinemod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.SummonStarRingAction;
import rhinemod.cards.special.Unscrupulous;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class RedShiftExperience extends AbstractRhineCard {
    public static final String ID = "rhinemod:RedShiftExperience";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/Starfall.png";
    public static final int COST = 1;
    public static final int STAR_HEALTH = 20;
    public static final int EXPLODE_STATUS = 2;
    public static final int UPGRADE_EXPLODE_STATUS = -1;

    public RedShiftExperience() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = STAR_HEALTH;
        secondMagicNumber = baseSecondMagicNumber = EXPLODE_STATUS;
        cardsToPreview = new Unscrupulous();
        realBranch = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonStarRingAction(magicNumber, 0, 0, secondMagicNumber));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeSecondMagicNumber(UPGRADE_EXPLODE_STATUS);
                    initializeDescription();
                }
            });
        }};
    }
}
