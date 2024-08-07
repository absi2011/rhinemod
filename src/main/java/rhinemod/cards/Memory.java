package rhinemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.cards.special.Unscrupulous;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class Memory extends AbstractRhineCard {
    public static final String ID = "rhinemod:Memory";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/Memory.png";
    public static final int COST = 0;
    public static final int ATTACK_DMG = 5;
    public static final int UPGRADE_PLUS_DMG = 2;
    public Memory() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        exhaust = true;
        cardsToPreview = new Unscrupulous();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cnt = calc();
        DamageInfo info = new DamageInfo(p, baseDamage * cnt);
        info.applyPowers(p, m);
        addToBot(new DamageAction(m, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        int cnt = calc();
        int temp = baseDamage;
        baseDamage *= cnt;
        super.applyPowers();
        magicNumber = damage;
        isMagicNumberModified = true;
        baseDamage = temp;
        isDamageModified = false;
        rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int cnt = calc();
        int temp = baseDamage;
        baseDamage *= cnt;
        super.calculateCardDamage(mo);
        magicNumber = damage;
        isMagicNumberModified = true;
        baseDamage = temp;
        isDamageModified = false;
        rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    public int calc()
    {
        int cnt = 0;
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group)
            if (c instanceof Unscrupulous)
                cnt++;
        return cnt;
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
