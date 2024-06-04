package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddFlowingShapeAction;
import rhinemod.characters.RhineLab;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class ResourceEconomization extends AbstractRhineCard {
    public static final String ID = "rhinemod:ResourceEconomization";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/ResourceEconomization.png";
    public static final int COST = 0;
    public ResourceEconomization() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
        realBranch = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p instanceof RhineLab) {
            int cnt = ((RhineLab) p).globalAttributes.flowspNum;
            addToBot(new AddFlowingShapeAction(-cnt));
            addToBot(new GainEnergyAction(cnt));
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    rawDescription = UPGRADE_DESCRIPTION;
                    exhaust = false;
                    initializeDescription();
                }
            });
        }};
    }
}
