package rhinemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AverageDamageAllAction;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.WaterDamage;

import java.util.ArrayList;
import java.util.List;

public class ShatteredVision extends AbstractRhineCard {
    public static final String ID = "rhinemod:ShatteredVision";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "images/cards/RhineStrike.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 13;
    public static final int UPGRADE_PLUS_DMG = 4;
    public static final int SARIA_PLUS_DMG = 2;
    public static final int WATER_TOT = 22;
    public ShatteredVision() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.SELF);
        damage = baseDamage = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            switch (chosenBranch) {
                case 0:
                    addToBot(new AverageDamageAllAction(damage, p, DamageInfo.DamageType.NORMAL));
                    break;
                case 1:
                    addToBot(new DamageAllEnemiesAction(p, damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    for (AbstractCard c : p.masterDeck.group)
                        if (c.uuid == uuid) {
                            p.masterDeck.removeCard(c);
                            break;
                        }
                    break;
                case 2:
                    ArrayList<AbstractMonster> aimList = new ArrayList<>();
                    for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
                        if (!mo.isDeadOrEscaped())
                            aimList.add(mo);
                    if (!aimList.isEmpty()) {
                        int[] dmgList = new int[aimList.size()];
                        int baseDmg = magicNumber / aimList.size();
                        for (int i = 0; i < aimList.size(); i++)
                            dmgList[i] = baseDmg;
                        int res = magicNumber % aimList.size();
                        for (int i = 0; i < res; i++)
                            dmgList[i]++;
                        for (int i = 0; i < aimList.size(); i++)
                            addToBot(new ApplyPowerAction(aimList.get(i), p, new WaterDamage(aimList.get(i), dmgList[i])));
                    }
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
                    upgradeDamage(UPGRADE_PLUS_DMG);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(1);
                    exhaust = true;
                    rawDescription = EXTENDED_DESCRIPTION[0];
                    upgradeDamage(SARIA_PLUS_DMG);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    magicNumber = baseMagicNumber = WATER_TOT;
                    initializeDescription();
                }
            });
        }};
    }
}
