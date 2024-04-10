package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.ChangeGravityAction;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.Stunned;
import rhinemod.powers.WaterDamage;
import rhinemod.interfaces.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class RhineStrike extends AbstractRhineCard {
    public static final String ID = "rhinemod:RhineStrike";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/RhineStrike.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 6;
    public static final int[] UPGRADE_PLUS_DMG = {3, 4, 3, 2};
    public static final int UPGRADE_PLUS_WATER = 2;
    public RhineStrike() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.BASIC, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        tags.add(CardTags.STRIKE);
        tags.add(CardTags.STARTER_STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        if (upgraded) {
            switch (chosenBranch) {
                case 2:
                    if (m.hasPower(Stunned.POWER_ID)) {
                        addToBot(new ChangeGravityAction());
                    }
                    break;
                case 3:
                    addToBot(new ApplyPowerAction(m, p, new WaterDamage(m, magicNumber)));
                    break;
            }
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeDamage(UPGRADE_PLUS_DMG[0]);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(1);
                    upgradeDamage(UPGRADE_PLUS_DMG[1]);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    rawDescription = EXTENDED_DESCRIPTION[0];
                    upgradeDamage(UPGRADE_PLUS_DMG[2]);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    upgradeDamage(UPGRADE_PLUS_DMG[3]);
                    magicNumber = baseMagicNumber = UPGRADE_PLUS_WATER;
                    initializeDescription();
                }
            });
        }};
    }
}
