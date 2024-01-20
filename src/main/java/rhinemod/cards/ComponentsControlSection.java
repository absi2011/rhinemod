package rhinemod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.ChangeGravityAction;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.interfaces.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class ComponentsControlSection extends AbstractRhineCard {
    public static final String ID = "rhinemod:ComponentsControlSection";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/ComponentsControlSection.png";
    public static final int COST = 1;
    public static final int UPGRADE_COST = 0;
    public ComponentsControlSection() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.BASIC, CardTarget.SELF);
        realBranch = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChangeGravityAction());
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
