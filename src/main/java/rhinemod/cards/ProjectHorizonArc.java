package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.ChangeGravityAction;
import rhinemod.actions.DestinyAction;
import rhinemod.actions.PHAAction;
import rhinemod.cards.special.Loner;
import rhinemod.cards.special.Unscrupulous;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class ProjectHorizonArc extends AbstractRhineCard {
    public static final String ID = "rhinemod:ProjectHorizonArc";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/Destiny.png";
    public static final int COST = 1;
    public static final int DAMAGE = 14;
    public static final int UPGRADE_DAMAGE = 4;
    public static final int KRISTEN_DAMAGE = -2;
    public ProjectHorizonArc() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = DAMAGE;
        cardsToPreview = new Unscrupulous();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (chosenBranch != 1)
        {
            addToBot(new DamageAction(m, new DamageInfo(p, damage)));
            addToBot(new MakeTempCardInDrawPileAction(new Unscrupulous(), 1, false, true));
        }
        else
        {
            addToBot(new PHAAction(p, m, damage));
            addToBot(new ChangeGravityAction());
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeDamage(UPGRADE_DAMAGE);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(1);
                    upgradeDamage(KRISTEN_DAMAGE);
                    rawDescription = EXTENDED_DESCRIPTION[0];
                    cardsToPreview = new Loner();
                    initializeDescription();
                }
            });
        }};
    }
}
