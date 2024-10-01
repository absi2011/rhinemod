package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.cards.special.Traitor;
import rhinemod.cards.special.Unscrupulous;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.SHAFTPower;

import java.util.ArrayList;
import java.util.List;

public class SHAFT extends AbstractRhineCard {
    public static final String ID = "rhinemod:SHAFT";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/SHAFT.png";
    public static final int COST = 1;
    public static final int DRAW_AMT = 3;
    public static final int UNSCRUPTULOUS_AMT = 2;
    public SHAFT() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = DRAW_AMT;
        secondMagicNumber = baseSecondMagicNumber = UNSCRUPTULOUS_AMT;
        cardsToPreview = new Unscrupulous();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (chosenBranch == 0) {
            addToBot(new DrawCardAction(1));
        }
        if (extraTriggered() || chosenBranch != 0) {
            addToBot(new GainEnergyAction(4));
        }
        else {
            addToBot(new ApplyPowerAction(p, p, new SHAFTPower(p, 1)));
        }
        if (chosenBranch != 0) {
            addToBot(new MakeTempCardInDrawPileAction(new Unscrupulous(), 4, true, true));
        }
    }

    @Override
    public void applyPowers() {
        if (extraTriggered() && upgraded && chosenBranch == 0) {
            costForTurn = 0;
            isCostModifiedForTurn = true;
        }
        super.applyPowers();
    }

    @Override
    public boolean extraTriggered() {
        if (chosenBranch == 0 && AbstractDungeon.player.hasPower(SHAFTPower.POWER_ID) && AbstractDungeon.player.getPower(SHAFTPower.POWER_ID).amount >= 3)
        {
            return true;
        }
        return false;
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    rawDescription = cardStrings.UPGRADE_DESCRIPTION;
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    rawDescription = EXTENDED_DESCRIPTION[0];
                    cardsToPreview = new Unscrupulous();
                    exhaust = true;
                    initializeDescription();
                }
            });
        }};
    }
}
