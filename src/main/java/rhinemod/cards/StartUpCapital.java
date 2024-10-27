package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.cards.special.Unscrupulous;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.patches.RhineTags;

import java.util.ArrayList;
import java.util.List;

public class StartUpCapital extends AbstractRhineCard {
    public static final String ID = "rhinemod:StartUpCapital";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/StartUpCapital.png";
    public static final int COST = 0;
    public static final int CARDS = 3;
    public static final int EXTRA_CARDS = 3;
    public static final int SHUFFLE = 5;
    public static final int ENERGY = 2;
    public StartUpCapital() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = CARDS;
        baseSecondMagicNumber = secondMagicNumber = SHUFFLE;
        exhaust = true;
        isInnate = true;
        cardsToPreview = new Unscrupulous();
        tags.add(RhineTags.UNSCRUPULOUS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(magicNumber));
        addToBot(new GainEnergyAction(ENERGY));
        addToBot(new MakeTempCardInDiscardAction(new Unscrupulous(), secondMagicNumber));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(EXTRA_CARDS);
                    initializeDescription();
                }
            });
        }};
    }
}
