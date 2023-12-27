package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.WaterDamage;
import rs.lazymankits.interfaces.cards.UpgradeBranch;

import java.util.ArrayList;
import java.util.List;

public class EcologicalSection extends AbstractRhineCard {
    public static final String ID = "rhinemod:EcologicalSection";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/EcologicalSection.png";
    public static final int COST = 0;
    public static final int WATER_DMG = 8;
    public static final int UPGRADE_PLUS_WATER = 3;
    public EcologicalSection() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE,
                CardRarity.BASIC, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = WATER_DMG;
        realBranch = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new WaterDamage(m, magicNumber)));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    upgradeMagicNumber(UPGRADE_PLUS_WATER);
                    initializeDescription();
                }
            });
        }};
    }
}
