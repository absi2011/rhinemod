package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.IcefieldsAdventureMuAction;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class IcefieldsAdventure extends AbstractRhineCard {
    public static final String ID = "rhinemod:IcefieldsAdventure";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/IcefieldsAdventure.png";
    public static final int COST = 1;
    public static final int BLOCK_AMT = 7;
    public static final int RESEARCH_AMT = 1;
    public static final int[] UPGRADE_PLUS_BLOCK = {2, 2};
    public static final int UPGRADE_PLUS_RESEARCH = 1;
    public static final int UPGRADE_HP_LOSS_AMT = 4;
    public IcefieldsAdventure() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = RESEARCH_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (chosenBranch == 0) {
            addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
        } else {
            addToBot(new IcefieldsAdventureMuAction(magicNumber));
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeBlock(UPGRADE_PLUS_BLOCK[0]);
                    upgradeMagicNumber(UPGRADE_PLUS_RESEARCH);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    rawDescription = UPGRADE_DESCRIPTION;
                    upgradeBlock(UPGRADE_PLUS_BLOCK[1]);
                    magicNumber = baseMagicNumber = UPGRADE_HP_LOSS_AMT;
                    initializeDescription();
                }
            });
        }};
    }
}
