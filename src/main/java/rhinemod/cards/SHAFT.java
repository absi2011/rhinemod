package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.SHAFTAction;
import rhinemod.cards.special.Traitor;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class SHAFT extends AbstractRhineCard {
    public static final String ID = "rhinemod:SHAFT";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "images/cards/SHAFT.png";
    public static final int COST = 0;
    public static final int KRISTEN_COST = 1;
    public static final int DRAW_AMT = 3;
    public static final int BURN_AMT = 2;
    public static final int UPGRADE_PLUS_BURN = -1;
    public SHAFT() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = DRAW_AMT;
        secondMagicNumber = baseSecondMagicNumber = BURN_AMT;
        cardsToPreview = new Burn();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new SHAFTAction(chosenBranch, secondMagicNumber));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeSecondMagicNumber(UPGRADE_PLUS_BURN);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    upgradeBaseCost(KRISTEN_COST);
                    rawDescription = UPGRADE_DESCRIPTION;
                    cardsToPreview = new Traitor();
                    initializeDescription();
                }
            });
        }};
    }
}
