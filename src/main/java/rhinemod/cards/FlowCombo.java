package rhinemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddFlowingShapeAction;
import rhinemod.characters.RhineLab;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class FlowCombo extends AbstractRhineCard {
    public static final String ID = "rhinemod:FlowCombo";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/FlowCombo.png";
    public static final int COST = 1;
    public static final int DAMAGE = 6;
    public static final int UPGRADE_DAMAGE = 2;
    public FlowCombo() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = DAMAGE;
        realBranch = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p instanceof RhineLab) {
            int cnt = ((RhineLab) p).globalAttributes.flowspNum;
            addToBot(new AddFlowingShapeAction(-cnt));
            for (int i = 0; i < cnt; i++) {
                addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.POISON));
            }
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
        }};
    }
}
