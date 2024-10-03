package rhinemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.characters.RhineLab;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class Galaxy extends AbstractRhineCard {
    public static final String ID = "rhinemod:Galaxy";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/Galaxy.png";
    public static final int COST = 4;
    public static final int ATTACK_DMG = 6;
    public static final int UPGRADE_PLUS_DMG = 2;

    public Galaxy() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        realBranch = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        int cnt = starRingCnt();
        costForTurn = Math.max(0, cost - cnt);
        isCostModifiedForTurn = (costForTurn != cost);
        return super.canUse(p, m);
    }

    @Override
    public void applyPowers() {
        int cnt = starRingCnt();
        costForTurn = Math.max(0, cost - cnt);
        isCostModifiedForTurn = (costForTurn != cost);
        baseDamage <<= cnt;
        super.applyPowers();
        baseDamage >>= cnt;
        isDamageModified = (baseDamage != damage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int cnt = starRingCnt();
        costForTurn = Math.max(0, cost - cnt);
        isCostModifiedForTurn = (costForTurn != cost);
        baseDamage <<= cnt;
        super.calculateCardDamage(mo);
        baseDamage >>= cnt;
        isDamageModified = (baseDamage != damage);
    }

    int starRingCnt() {
        if (AbstractDungeon.player instanceof RhineLab) {
            return ((RhineLab) AbstractDungeon.player).currentRings.size();
        } else {
            return 0;
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeDamage(UPGRADE_PLUS_DMG);
                    initializeDescription();
                }
            });
        }};
    }
}
