package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.characters.RhineLab;
import rhinemod.characters.StarRing;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.CriticalPointPower;

import java.util.ArrayList;
import java.util.List;

public class CriticalPoint extends AbstractRhineCard {
    public static final String ID = "rhinemod:CriticalPoint";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/CriticalPoint.png";
    public static final int COST = 1;
    public static final int MULTI = 1;
    public static final int EXTRA_MULTI = 1;
    public CriticalPoint() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.ENEMY);
        realBranch = 2;
        isTargetStarRing = true;
        magicNumber = baseMagicNumber = MULTI;
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (!super.canPlay(card)) return false;
        if (card instanceof CriticalPoint)
            return AbstractDungeon.player instanceof RhineLab && !((RhineLab) AbstractDungeon.player).currentRings.isEmpty();
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!(m instanceof StarRing) || m.isDead) return;
        addToBot(new ApplyPowerAction(m, p, new CriticalPointPower(m, magicNumber)));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(EXTRA_MULTI);
                    initializeDescription();
                }
            });
        }};
    }
}
