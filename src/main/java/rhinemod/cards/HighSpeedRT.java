package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.HighSpeedRTPower;
import rhinemod.interfaces.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class HighSpeedRT extends AbstractRhineCard {
    public static final String ID = "rhinemod:HighSpeedResonatingTroubleshooter";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/HighSpeedResonatingTroubleshooter.png";
    public static final int COST = 1;
    public HighSpeedRT() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new HighSpeedRTPower(m, p)));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    rawDescription = UPGRADE_DESCRIPTION;
                    exhaust = false;
                    initializeDescription();
                }
            });
        }};
    }
}
