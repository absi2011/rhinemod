package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.QuickwaterArea;

import java.util.ArrayList;
import java.util.List;

public class QuicksandGeneration extends AbstractRhineCard {
    public static final String ID = "rhinemod:QuicksandGeneration";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/QuicksandGeneration.png";
    public static final int COST = 1;
    public static final int BLOCK_AMT = 7;
    public static final int UPGRADE_PLUS_BLOCK = 3;
    public static final int WEAKNESS_AMT = 2;
    public static final int UPGRADE_PLUS_WK = 1;
    public static final int WATER_DMG = 10;
    public QuicksandGeneration() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = WEAKNESS_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (chosenBranch == 0) {
            ArrayList<AbstractMonster> list = new ArrayList<>();
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
                if (!mo.isDeadOrEscaped() && mo.getIntentBaseDmg() > 0)
                    list.add(mo);
            if (list.isEmpty()) {
                return;
            }
            int avg = magicNumber / list.size();
            int fir = magicNumber % list.size();
            for (int i = 0; i < list.size(); i++) {
                if (avg + (i < fir ? 1 : 0) > 0) {
                    addToBot(new ApplyPowerAction(list.get(i), p, new WeakPower(list.get(i), avg + (i < fir ? 1 : 0), false)));
                }
            }
        } else {
            addToBot(new ApplyPowerAction(p, p, new QuickwaterArea(p, magicNumber)));
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeBlock(UPGRADE_PLUS_BLOCK);
                    upgradeMagicNumber(UPGRADE_PLUS_WK);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3, EXTENDED_DESCRIPTION[0]);
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    upgradeMagicNumber(0); // Make it green.
                    magicNumber = baseMagicNumber = WATER_DMG;
                    initializeDescription();
                }
            });
        }};
    }
}
