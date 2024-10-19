package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.IdealistFormPower;
import rhinemod.powers.IdealistFormPowerK;
import rhinemod.powers.IdealistFormPowerM;
import rhinemod.powers.IdealistFormPowerS;

import java.util.ArrayList;
import java.util.List;

public class IdealistForm extends AbstractRhineCard {
    public static final String ID = "rhinemod:IdealistForm";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/IdealistForm.png";
    public static final int COST = 3;
    public static final int ATTACK_DMG = 60;
    public static final int UPGRADE_PLUS_DMG = 15;
    public static final int CALCIUM = 3;
    public static final int FLOWSP = 5;
    public IdealistForm() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.NONE);
        magicNumber = baseMagicNumber = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (chosenBranch) {
            case 0:
                addToBot(new ApplyPowerAction(p, p, new IdealistFormPower(p, magicNumber)));
                break;
            case 1:
                addToBot(new ApplyPowerAction(p, p, new IdealistFormPowerS(p, magicNumber)));
                break;
            case 2:
                addToBot(new ApplyPowerAction(p, p, new IdealistFormPowerK(p, 1)));
                break;
            case 3:
                addToBot(new ApplyPowerAction(p, p, new IdealistFormPowerM(p, magicNumber)));
                break;
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_PLUS_DMG);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(1);
                    magicNumber = baseMagicNumber = CALCIUM;
                    rawDescription = EXTENDED_DESCRIPTION[0];
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    cardsToPreview = new LoneLight();
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    rawDescription = EXTENDED_DESCRIPTION[2];
                    magicNumber = baseMagicNumber = FLOWSP;
                    initializeDescription();
                }
            });
        }};
    }
}
