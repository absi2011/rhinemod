package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.ExhaustUnscrupulousAction;
import rhinemod.monsters.StarPod;
import rhinemod.powers.Order;
import rhinemod.powers.ResearchProgress;
import rhinemod.util.TheSky;

public class CreateMiracle extends CustomCard {
    public static final String ID = "rhinemod:CreateMiracle";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/rhinemod/images/cards/CreateMiracle.png";
    private static final int COST = 0;
    private static final int DAMAGE = 5;
    private static final int DOUBLE_CONDITION = 80;
    private static final int UPG_DOUBLE_CONDITION = -20;

    public CreateMiracle() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.ENEMY);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DOUBLE_CONDITION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (mo instanceof StarPod) {
            damage *= 4;
            isDamageModified = (damage != baseDamage);
            return;
        }
        int times = mo.maxHealth / magicNumber;
        for (int i = 0; i < times; i++) {
            damage *= 2;
            if (damage > 99999999) {
                damage = 99999999;
            }
        }
        isDamageModified = (damage != baseDamage);
    }

    @Override
    public AbstractCard makeCopy() {
        return new CreateMiracle();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_DOUBLE_CONDITION);
            initializeDescription();
        }
    }
}
