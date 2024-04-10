package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.HeavyBlade;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddCalciumAction;
import rhinemod.actions.ReduceCalciumAction;
import rhinemod.characters.RhineLab;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class FullBlow extends AbstractRhineCard {
    public static final String ID = "rhinemod:FullBlow";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/UnusedBoxingGloves.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 8;
    public static final int EXTRA_TIMES = 4;
    public static final int UPGRADE_PLUS_TIMES = 2;
    public static final int EXTRA_DMG = 2;
    public FullBlow() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = EXTRA_TIMES;
        realBranch = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        if (p instanceof RhineLab) {
            addToBot(new ReduceCalciumAction(((RhineLab) AbstractDungeon.player).globalAttributes.calciumNum));
        }
    }

    @Override
    public void applyPowers() {
        int calcNum = 0;
        if (AbstractDungeon.player instanceof  RhineLab) {
            calcNum = ((RhineLab) AbstractDungeon.player).globalAttributes.calciumNum;
        }
        baseDamage += calcNum * magicNumber;
        super.applyPowers();
        baseDamage -= calcNum * magicNumber;
        isDamageModified = (baseDamage != damage);
    }


    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int calcNum = 0;
        if (AbstractDungeon.player instanceof  RhineLab) {
            calcNum = ((RhineLab) AbstractDungeon.player).globalAttributes.calciumNum;
        }
        baseDamage += calcNum * magicNumber;
        super.calculateCardDamage(mo);
        baseDamage -= calcNum * magicNumber;
        isDamageModified = (baseDamage != damage);
    }
    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeDamage(EXTRA_DMG);
                    upgradeMagicNumber(UPGRADE_PLUS_TIMES);
                    initializeDescription();
                }
            });
        }};
    }
}
