package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.*;

import java.util.ArrayList;
import java.util.List;

public class LikeMind extends AbstractRhineCard {
    public static final String ID = "rhinemod:LikeMind";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/RhineDefend.png";
    public static final int COST = 2;
    public static final int BASIC_NUM = 2;
    public static final int UPGRADE_PLUS_NONE = 1;
    public LikeMind() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = BASIC_NUM;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (chosenBranch) {
            case 0:
                addToBot(new ApplyPowerAction(p, p, new LikeMindPowerRandom(p, magicNumber)));
                break;
            case 1:
                addToBot(new ApplyPowerAction(p, p, new LikeMindPowerCalcium(p, magicNumber)));
                break;
            case 2:
                addToBot(new ApplyPowerAction(p, p, new LikeMindPowerResearch(p, magicNumber)));
                break;
            case 3:
                addToBot(new ApplyPowerAction(p, p, new LikeMindPowerFlowsp(p, magicNumber)));
                break;
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_PLUS_NONE);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(1);
                    rawDescription = EXTENDED_DESCRIPTION[0];
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    rawDescription = EXTENDED_DESCRIPTION[2];
                    initializeDescription();
                }
            });
        }};
    }
}
