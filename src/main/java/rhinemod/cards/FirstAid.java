package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddCalciumAction;
import rhinemod.actions.AddFlowingShapeAction;
import rhinemod.actions.ReduceCalciumAction;
import rhinemod.actions.SummonStarRingAction;
import rhinemod.characters.RhineLab;
import rhinemod.characters.StarRing;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class FirstAid extends AbstractRhineCard {
    public static final String ID = "rhinemod:FirstAid";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/FirstAid.png";
    public static final int COST = 1;
    public static final int BLOCK_AMT = 7;
    public static final int[] UPGRADE_PLUS_BLOCK = {3, 3, 0, 3};
    public static final int COST_CALCIUM = 1;
    public static final int STAR_HEALTH = 7;
    public static final int UPGRADE_PLUS_FLOWSP = 2;
    public FirstAid() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (chosenBranch == 2)
        {
            addToBot(new SummonStarRingAction(magicNumber, 0, block));
        }
        addToBot(new GainBlockAction(p, block));
        if ((chosenBranch == 0) || (chosenBranch == 2))
        {
            if (p instanceof RhineLab) {
                for (StarRing star : ((RhineLab) p).currentRings) {
                    addToBot(new GainBlockAction(star, block));
                }
            }
        }
        if (chosenBranch == 1) {
            if ((p instanceof RhineLab) && (((RhineLab) p).globalAttributes.calciumNum >= magicNumber)) {
                addToBot(new ReduceCalciumAction(magicNumber));
                addToBot(new GainBlockAction(p, block));
            }
        }
        if (chosenBranch == 3) {
            addToBot(new AddFlowingShapeAction(magicNumber));
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
                    upgradeMagicNumber(COST_CALCIUM);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    // upgradeBlock(UPGRADE_PLUS_BLOCK[2]);
                    upgradeMagicNumber(STAR_HEALTH);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    rawDescription = EXTENDED_DESCRIPTION[2];
                    upgradeBlock(UPGRADE_PLUS_BLOCK[3]);
                    upgradeMagicNumber(UPGRADE_PLUS_FLOWSP);
                    initializeDescription();
                }
            });
        }};
    }
}
