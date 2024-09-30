package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class DependentVariable extends AbstractRhineCard {
    public static final String ID = "rhinemod:DependentVariable";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/SHAFT.png";
    public static final int COST = 1;
    public static final int EXTRA_LEVEL = 3;
    public DependentVariable() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 0;
        baseMagicNumber = magicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
        }
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        calcName(false);
    }

    public void onMoveToDiscard() {
        calcName(false);
    }

    @Override
    public void applyPowers() {
        baseDamage = getResearchProgress();
        super.applyPowers();
        calcName(true);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = getResearchProgress();
        super.calculateCardDamage(mo);
        calcName(true);
    }

    private int getResearchProgress() {
        int amount = 0;
        if (AbstractDungeon.player.hasPower(ResearchProgress.POWER_ID)) {
            amount = AbstractDungeon.player.getPower(ResearchProgress.POWER_ID).amount;
        }
        if (upgraded) {
            amount += magicNumber;
        }
        return amount;
    }

    private void calcName(boolean needExtend) {
        if (!upgraded) {
            rawDescription = DESCRIPTION;
        }
        else {
            rawDescription = UPGRADE_DESCRIPTION;
        }
        if (needExtend) {
            rawDescription += EXTENDED_DESCRIPTION[0];
        }
        initializeDescription();
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(EXTRA_LEVEL);
                    rawDescription = UPGRADE_DESCRIPTION;
                    initializeDescription();
                }
            });
        }};
    }
}
