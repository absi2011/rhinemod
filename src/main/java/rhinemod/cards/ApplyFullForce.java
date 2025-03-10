package rhinemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.Stunned;

import java.util.ArrayList;
import java.util.List;

public class ApplyFullForce extends AbstractRhineCard {
    public static final String ID = "rhinemod:ApplyFullForce";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/ApplyFullForce.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 7;
    public static final int UPGRADE_PLUS_DMG = 4;
    public ApplyFullForce() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        realBranch = 1;
        damage = baseDamage = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        if (m.hasPower(Stunned.POWER_ID)) addToBot(new DamageAction(m, new DamageInfo(p, damage * 2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        else addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
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
