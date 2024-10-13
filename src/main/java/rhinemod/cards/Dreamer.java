package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.util.GlobalAttributes;

import java.util.ArrayList;
import java.util.List;

public class Dreamer extends AbstractRhineCard {
    public static final String ID = "rhinemod:Dreamer";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/Dreamer.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 8;
    public static final int EXTRA_DMG = 1;
    public static final int UPGRADE_PLUS_DMG = 3;
    public static final int KRISTEN_PLUS_DAMAGE = 2;
    public static final int KRISTEN_EXTRA_DMG = 3;
    public static final int MUELSYSE_PLUS_DAMAGE = -3;
    public static final int MUELSYSE_EXTRA_DMG = 10;
    public Dreamer() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = EXTRA_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
    }

    @Override
    public void applyPowers() {
        if (chosenBranch == 0) {
            baseDamage += magicNumber * AbstractDungeon.player.exhaustPile.size();
        } else if (chosenBranch == 1) {
            baseDamage += magicNumber * GlobalAttributes.gravityChanges;
        }
        super.applyPowers();
        if (chosenBranch == 0) {
            baseDamage -= magicNumber * AbstractDungeon.player.exhaustPile.size();
        } else if (chosenBranch == 1) {
            baseDamage -= magicNumber * GlobalAttributes.gravityChanges;
        }
        isDamageModified = (baseDamage != damage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (chosenBranch == 0) {
            baseDamage += magicNumber * AbstractDungeon.player.exhaustPile.size();
        } else if (chosenBranch == 1) {
            baseDamage += magicNumber * GlobalAttributes.gravityChanges;
        }
        super.calculateCardDamage(mo);
        if (chosenBranch == 0) {
            baseDamage -= magicNumber * AbstractDungeon.player.exhaustPile.size();
        } else if (chosenBranch == 1) {
            baseDamage -= magicNumber * GlobalAttributes.gravityChanges;
        }
        isDamageModified = (baseDamage != damage);
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
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    upgradeMagicNumber(0); // Make it Green
                    magicNumber = baseMagicNumber = KRISTEN_EXTRA_DMG;
                    rawDescription = EXTENDED_DESCRIPTION[0];
                    upgradeDamage(KRISTEN_PLUS_DAMAGE);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    upgradeMagicNumber(0); // Make it Green
                    magicNumber = baseMagicNumber = MUELSYSE_EXTRA_DMG;
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    upgradeDamage(MUELSYSE_PLUS_DAMAGE);
                    initializeDescription();
                }
            });
        }};
    }
}
