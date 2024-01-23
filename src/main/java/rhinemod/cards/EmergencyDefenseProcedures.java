package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.cards.special.Unscrupulous;
import rhinemod.characters.RhineLab;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class EmergencyDefenseProcedures extends AbstractRhineCard {
    public static final String ID = "rhinemod:EmergencyDefenseProcedures";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "images/cards/EmergencyDefenseProcedures.png";
    public static final int COST = 1;
    public static final int BLOCK_AMT = 10;
    public static final int UPGRADE_PLUS_BLOCK = 3;
    public static final int BLOCK_DEC = 2;
    public static final int CAL_AMT = 5;
    public static final int KRISTEN_PLUS_BLOCK = -2;
    public static final int KRISTEN_EXTRA_BLOCK = 4;
    public String baseDescription;
    public EmergencyDefenseProcedures() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = BLOCK_DEC;
        baseDescription = rawDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowersToBlock();
        addToBot(new GainBlockAction(p, p, secondMagicNumber));
        rawDescription = baseDescription;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        this.applyPowersToBlock();
    }

    @Override
    public void applyPowersToBlock() {
        rawDescription = baseDescription + EXTENDED_DESCRIPTION[2];
        initializeDescription();
        int tmp = baseBlock;
        int cnt = 0;
        AbstractPlayer p = AbstractDungeon.player;
        switch (chosenBranch) {
            case 0:
                for (AbstractCard c : p.hand.group)
                    if (c instanceof Unscrupulous)
                        cnt++;
                for (AbstractCard c : p.drawPile.group)
                    if (c instanceof Unscrupulous)
                        cnt++;
                for (AbstractCard c : p.discardPile.group)
                    if (c instanceof Unscrupulous)
                        cnt++;
                baseBlock -= baseMagicNumber * cnt;
                if (baseBlock < 0) baseBlock = 0;
                baseSecondMagicNumber = baseBlock;
                super.applyPowersToBlock();
                secondMagicNumber = block;
                if (secondMagicNumber != baseSecondMagicNumber) isSecondMagicNumberModified = true;
                baseBlock = block = tmp;
                break;
            case 1:
                baseBlock = 0;
                if (p instanceof RhineLab) {
                    baseBlock = ((RhineLab) p).globalAttributes.calciumNum + magicNumber * 2;
                }
                baseSecondMagicNumber = baseBlock;
                super.applyPowersToBlock();
                secondMagicNumber = block;
                if (secondMagicNumber != baseSecondMagicNumber) isSecondMagicNumberModified = true;
                break;
            case 2:
                if (p.hasPower(ResearchProgress.POWER_ID))
                    cnt = ((ResearchProgress) p.getPower(ResearchProgress.POWER_ID)).getLevel();
                baseBlock += baseMagicNumber * cnt;
                baseSecondMagicNumber = baseBlock;
                super.applyPowersToBlock();
                secondMagicNumber = block;
                if (secondMagicNumber != baseSecondMagicNumber) isSecondMagicNumberModified = true;
                baseBlock = block = tmp;
                break;
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeBlock(UPGRADE_PLUS_BLOCK);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(1);
                    magicNumber = baseMagicNumber = CAL_AMT;
                    baseDescription = rawDescription = EXTENDED_DESCRIPTION[0];
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    upgradeBlock(KRISTEN_PLUS_BLOCK);
                    magicNumber = baseMagicNumber = KRISTEN_EXTRA_BLOCK;
                    baseDescription = rawDescription = EXTENDED_DESCRIPTION[1];
                    initializeDescription();
                }
            });
        }};
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = baseDescription;
        initializeDescription();
    }
}
