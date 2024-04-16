package rhinemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.RemoveCardAction;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.Stunned;

import java.util.ArrayList;
import java.util.List;

public class CrackedSkill extends AbstractRhineCard {
    public static final String ID = "rhinemod:CrackedSkill";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/PureWaterIsLife.png";
    public static final int COST = 1;
    public static final int BLOCK_GAIN = 10;
    public static final int UPGRADE_COST = 0;
    public CrackedSkill() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.SELF);
        realBranch = 1;
        block = baseBlock = BLOCK_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        if (upgraded) {
            addToBot(new RemoveCardAction(uuid));
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeBaseCost(UPGRADE_COST);
                    rawDescription = UPGRADE_DESCRIPTION;
                    exhaust = true;
                    name = EXTENDED_DESCRIPTION[0];
                    initializeDescription();
                }
            });
        }};
    }
}
