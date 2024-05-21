package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddCalciumAction;
import rhinemod.actions.AddFlowingShapeAction;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class DancingInThrees extends AbstractRhineCard {
    public static final String ID = "rhinemod:DancingInThrees";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/Calcification.png";
    public static final int COST = 0;
    public static final int ATTR_ADD = 6;
    public static final int UPGRADE_PLUS_ATTR = 2;
    public DancingInThrees() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = ATTR_ADD;
    }

    @Override
    public boolean extraTriggered() {
        boolean[] used = new boolean[4];
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn)
            if (c instanceof AbstractRhineCard) {
                if (((AbstractRhineCard) c).realBranch != -1)
                {
                    used[((AbstractRhineCard) c).realBranch] = true;
                }
            }
            else {
                used[0] = true;
            }
        return used[0] && used[1] && used[2] && used[3];
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (extraTriggered()) {
            addToBot(new AddCalciumAction(magicNumber));
            addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
            addToBot(new AddFlowingShapeAction(magicNumber));
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_PLUS_ATTR);
                    initializeDescription();
                }
            });
        }};
    }
}
