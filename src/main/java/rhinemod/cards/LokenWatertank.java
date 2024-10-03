package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.DamageAllAction;
import rhinemod.cards.special.Unscrupulous;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class LokenWatertank extends AbstractRhineCard {
    public static final String ID = "rhinemod:LokenWatertank";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/LokenWatertank.png";
    public static final int COST = 1;
    public static final int UPGRADE_COST = 0;
    public static final int ALL_DAMAGE = 30;
    public static final int CARD_NUM = 6;
    public LokenWatertank() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.ALL_ENEMY);
        damage = baseDamage = ALL_DAMAGE;
        magicNumber = baseMagicNumber = CARD_NUM;
        cardsToPreview = new Unscrupulous();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardCrawlGame.sound.play("AS_YOU_WISH");
        addToBot(new DamageAllAction(damage, p, DamageInfo.DamageType.NORMAL));
        addToBot(new MakeTempCardInDrawPileAction(new Unscrupulous(), magicNumber, true, true));
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applyPowers();
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeBaseCost(UPGRADE_COST);
                    initializeDescription();
                }
            });
        }};
    }
}
