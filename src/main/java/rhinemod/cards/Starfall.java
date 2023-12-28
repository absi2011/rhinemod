package rhinemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.ChangeGravityAction;
import rhinemod.patches.AbstractCardEnum;
import rs.lazymankits.interfaces.cards.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class Starfall extends AbstractRhineCard {
    public static final String ID = "rhinemod:Starfall";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/Starfall.png";
    public static final int COST = 2;
    public static final int ATTACK_DMG = 13;
    public static final int UPGRADE_PLUS_DMG = 3;
    public Starfall() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        damage = baseDamage = ATTACK_DMG;
        realBranch = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new ChangeGravityAction());
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
