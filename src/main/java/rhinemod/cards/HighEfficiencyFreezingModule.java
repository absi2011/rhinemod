package rhinemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.WaterDamage;

import java.util.ArrayList;
import java.util.List;

public class HighEfficiencyFreezingModule extends AbstractRhineCard {
    public static final String ID = "rhinemod:HighEfficiencyFreezingModule";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/HighEfficiencyFreezingModule.png";
    public static final int COST = 1;
    public static final int DAMAGE_AMT = 7;
    public static final int EXTRA_DMG = 10;
    public static final int UPGRADE_PLUS_DMG = 3;
    public HighEfficiencyFreezingModule() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.ALL_ENEMY);
        damage = baseDamage = DAMAGE_AMT;
        magicNumber = baseMagicNumber = EXTRA_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int[] dmg = new int[AbstractDungeon.getMonsters().monsters.size()];
        for (int i = 0; i < dmg.length; i++) {
            AbstractMonster mo = AbstractDungeon.getMonsters().monsters.get(i);
            DamageInfo info = new DamageInfo(p, mo.hasPower(WaterDamage.POWER_ID)? baseMagicNumber : baseDamage);
            info.applyPowers(p, mo);
        }
        addToBot(new DamageAllEnemiesAction(p, dmg, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void applyPowers() {
        int tmp = baseDamage;
        baseDamage = baseMagicNumber;
        super.applyPowers();
        magicNumber = damage;
        isMagicNumberModified = isDamageModified;
        baseDamage = tmp;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int tmp = baseDamage;
        baseDamage = baseMagicNumber;
        super.calculateCardDamage(mo);
        magicNumber = damage;
        isMagicNumberModified = isDamageModified;
        baseDamage = tmp;
        super.calculateCardDamage(mo);

    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeDamage(UPGRADE_PLUS_DMG);
                    upgradeMagicNumber(UPGRADE_PLUS_DMG);
                    initializeDescription();
                }
            });
        }};
    }
}
