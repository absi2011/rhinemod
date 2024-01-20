package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.WaterDamage;
import rhinemod.interfaces.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class PureWaterIsLife extends AbstractRhineCard {
    public static final String ID = "rhinemod:PureWaterIsLife";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "images/cards/PureWaterIsLife.png";
    public static final int COST = 1;
    public PureWaterIsLife() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        realBranch = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            baseBlock = 0;
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
                if (!mo.isDeadOrEscaped() && mo.hasPower(WaterDamage.POWER_ID))
                    baseBlock += mo.getPower(WaterDamage.POWER_ID).amount;
        } else {
            if (m.hasPower(WaterDamage.POWER_ID)) baseBlock = m.getPower(WaterDamage.POWER_ID).amount;
            else baseBlock = 0;
        }
        applyPowersToBlock();
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    rawDescription = UPGRADE_DESCRIPTION;
                    target = CardTarget.SELF;
                    initializeDescription();
                }
            });
        }};
    }
}
