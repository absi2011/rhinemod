package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddCalciumAction;
import rhinemod.actions.AddFlowingShapeAction;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class RhineDefend extends AbstractRhineCard {
    public static final String ID = "rhinemod:RhineDefend";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/RhineDefend.png";
    public static final int COST = 1;
    public static final int BLOCK_AMT = 5;
    public static final int[] UPGRADE_PLUS_BLOCK = {3, 3, 3, 0};
    public static final int UPGRADE_PLUS_CALCIUM = 1;
    public static final int UPGRADE_PLUS_RESEARCH = 1;
    public static final int UPGRADE_PLUS_FLOWSP = 2;
    public RhineDefend() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.BASIC, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = 0;
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        switch (chosenBranch) {
            case 1:
                addToBot(new AddCalciumAction(magicNumber));
                break;
            case 2:
                addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
                break;
            case 3:
                addToBot(new AddFlowingShapeAction(magicNumber));
                break;
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeBlock(UPGRADE_PLUS_BLOCK[0]);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(1);
                    rawDescription = EXTENDED_DESCRIPTION[0];
                    upgradeBlock(UPGRADE_PLUS_BLOCK[1]);
                    upgradeMagicNumber(UPGRADE_PLUS_CALCIUM);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    upgradeBlock(UPGRADE_PLUS_BLOCK[2]);
                    upgradeMagicNumber(UPGRADE_PLUS_RESEARCH);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    rawDescription = EXTENDED_DESCRIPTION[2];
                    upgradeMagicNumber(UPGRADE_PLUS_FLOWSP);
                    initializeDescription();
                }
            });
        }};
    }
}
