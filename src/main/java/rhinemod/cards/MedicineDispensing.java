package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddFlowingShapeAction;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class MedicineDispensing extends AbstractRhineCard {
    public static final String ID = "rhinemod:MedicineDispensing";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/MedicineDispensing.png";
    public static final int COST = 2;
    public static final int BLOCK_AMT = 8;
    public static final int UPGRADE_PLUS_BLOCK = 3;
    public static final int SCI_FLW_AMT = 2;
    public static final int UPGRADE_PLUS_AMT = 1;
    public MedicineDispensing() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = SCI_FLW_AMT;
        realBranch = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(new AddFlowingShapeAction(magicNumber));
        addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeBlock(UPGRADE_PLUS_BLOCK);
                    upgradeMagicNumber(UPGRADE_PLUS_AMT);
                    initializeDescription();
                }
            });
        }};
    }
}
