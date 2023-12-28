package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.SolidifyPower;
import rs.lazymankits.interfaces.cards.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class Solidify extends AbstractRhineCard {
    public static final String ID = "rhinemod:Solidify";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/Solidify.png";
    public static final int COST = 2;
    public static final int UPGRADE_COST = 1;
    public Solidify() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.RHINE,
                CardRarity.RARE, CardTarget.SELF);
        realBranch = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SolidifyPower(p)));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeBaseCost(UPGRADE_COST);
                    initializeDescription();
                }
            });
        }};
    }
}
