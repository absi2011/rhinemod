package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddCalciumAction;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class UnusedBoxingGloves extends AbstractRhineCard {
    public static final String ID = "rhinemod:UnusedBoxingGloves";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/UnusedBoxingGloves.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 2;
    public static final int ATTACK_TIMES = 3;
    public static final int UPGRADE_PLUS_TIMES = 1;
    public static final int CAL_GAIN = 6;
    public UnusedBoxingGloves() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = ATTACK_TIMES;
        secondMagicNumber = baseSecondMagicNumber = CAL_GAIN;
        realBranch = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        addToBot(new AddCalciumAction(secondMagicNumber));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_PLUS_TIMES);
                    initializeDescription();
                }
            });
        }};
    }
}
