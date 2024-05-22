package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.PDOFBPower;

import java.util.ArrayList;
import java.util.List;

public class PureDewOfFreshBlossoms extends AbstractRhineCard {
    public static final String ID = "rhinemod:PureDewOfFreshBlossoms";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/PureDewOfFreshBlossoms.png";
    public static final int COST = 1;
    public PureDewOfFreshBlossoms() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.SELF);
        realBranch = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PDOFBPower(p)));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    rawDescription = UPGRADE_DESCRIPTION;
                    selfRetain = true;
                    initializeDescription();
                }
            });
        }};
    }
}
