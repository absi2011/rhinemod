package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.PlanetaryDebrisPower;
import rhinemod.interfaces.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class PlanetaryDebris extends AbstractRhineCard {
    public static final String ID = "rhinemod:PlanetaryDebris";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/PlanetaryDebris.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 4;
    public static final int UPGRADE_PLUS_DMG = 2;
    public PlanetaryDebris() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = ATTACK_DMG;
        realBranch = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PlanetaryDebrisPower(p, magicNumber)));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_PLUS_DMG);
                    initializeDescription();
                }
            });
        }};
    }
}
