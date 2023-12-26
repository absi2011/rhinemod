package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.characters.RhineLab;
import rhinemod.patches.AbstractCardEnum;
import rs.lazymankits.interfaces.cards.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class RhineDefend extends AbstractRhineCard {
    public static final String ID = "rhinemod:RhineDefend";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "images/cards/RhineDefend.png";
    public static final int COST = 1;
    public static final int BLOCK_AMT = 5;
    public static final int[] UPGRADE_PLUS_BLOCK = {3, 3, 3, 2};
    public static final int UPGRADE_PLUS_CALCIUM = 2;
    public RhineDefend() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE,
                CardRarity.BASIC, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        switch (chosenBranch()) {
            case 1:
                ((RhineLab) p).globalAttributes.addCalcium(magicNumber);
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
                    upgradeDamage(UPGRADE_PLUS_BLOCK[1]);
                    magicNumber = baseMagicNumber = UPGRADE_PLUS_CALCIUM;
                    initializeDescription();
                }
            });
        }};
    }
}
