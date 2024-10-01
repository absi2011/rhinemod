package rhinemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AverageDamageAllAction;
import rhinemod.actions.RemoveCardAction;
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
    public static final String IMG = "resources/rhinemod/images/cards/ShatteredVision.png";
    public static final String IMG_M = "resources/rhinemod/images/cards/ShatteredVisionM.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 13;
    public static final int UPGRADE_PLUS_DMG = 4;
    public static final int SARIA_PLUS_DMG = 2;
    public static final int WATER_TOT = 18;
    public ShatteredVision() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.ALL_ENEMY);
        damage = baseDamage = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (chosenBranch) {
            case 0:
                applyPowers();
                addToBot(new AverageDamageAllAction(damage, p, DamageInfo.DamageType.NORMAL));
                break;
            case 1:
                addToBot(new RemoveCardAction(uuid));
                addToBot(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 2:
                ArrayList<AbstractCreature> aimList = new ArrayList<>();
                aimList.add(p);
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
                    for (int i = 0; i < aimList.size(); i++) {
                        if (aimList.get(i) instanceof AbstractPlayer) {
                            addToBot(new GainBlockAction(aimList.get(i), p, dmgList[i]));
                        } else {
                            addToBot(new ApplyPowerAction(aimList.get(i), p, new WaterDamage(aimList.get(i), dmgList[i])));
                        }
                    }
                }
                break;
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
                    type = CardType.SKILL;
                    textureImg = IMG_M;
                    loadCardImage(textureImg);
                    initializeDescription();
                }
            });
        }};
    }
}
