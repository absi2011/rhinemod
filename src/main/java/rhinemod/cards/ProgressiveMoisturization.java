package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddFlowingShapeAction;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.WaterDamage;
import rhinemod.interfaces.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class ProgressiveMoisturization extends AbstractRhineCard {
    public static final String ID = "rhinemod:ProgressiveMoisturization";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "rhinemod/images/cards/ProgressiveMoisturization.png";
    public static final int COST = 1;
    public static final int WATER_AMT = 4;
    public static final int UPGRADE_PLUS_WATER = 2;
    public static final int FLOW_AMT = 1;
    public static final int UPGRADE_PLUS_FLOW = 1;
    public ProgressiveMoisturization() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = WATER_AMT;
        secondMagicNumber = baseSecondMagicNumber = FLOW_AMT;
        realBranch = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new WaterDamage(m, magicNumber)));
        addToBot(new AddFlowingShapeAction(secondMagicNumber));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_PLUS_WATER);
                    upgradeSecondMagicNumber(UPGRADE_PLUS_FLOW);
                    initializeDescription();
                }
            });
        }};
    }
}
