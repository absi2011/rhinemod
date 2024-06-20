package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.ExhaustUnscrupulousAction;
import rhinemod.actions.GainProgressByCostAction;
import rhinemod.actions.SummonStarRingAction;
import rhinemod.cards.special.GravityDown;
import rhinemod.cards.special.GravityNone;
import rhinemod.cards.special.GravityUp;
import rhinemod.cards.special.PaleFir;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class TechnologyRisingStar extends AbstractRhineCard {
    public static final String ID = "rhinemod:TechnologyRisingStar";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/TechnologyRisingStar.png";
    public static final int COST = 1;
    public static final int CARD_DRAW = 2;
    public static final int UPGRADE_PLUS_DRAW = 1;
    public static final int PROGRESS = 4;
    public static final int EXHAUST = 2;
    public static final int PROGRESS_M = 5;
    public TechnologyRisingStar() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = CARD_DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (chosenBranch) {
            case 0:
                addToBot(new DrawCardAction(magicNumber, new GainProgressByCostAction(p)));
                break;
            case 1:
                addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
                addToBot(new ExhaustUnscrupulousAction(secondMagicNumber));
                break;
            case 2:
                ArrayList<AbstractCard> list = new ArrayList<>();
                list.add(new GravityUp());
                list.add(new GravityNone());
                list.add(new GravityDown());
                addToBot(new ChooseOneAction(list));
                addToBot(new SummonStarRingAction(1));
                break;
            case 3:
                addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
                addToBot(new MakeTempCardInHandAction(new PaleFir()));
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_PLUS_DRAW);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(1);
                    magicNumber = baseMagicNumber = PROGRESS;
                    secondMagicNumber = baseSecondMagicNumber = EXHAUST;
                    rawDescription = EXTENDED_DESCRIPTION[0];
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    magicNumber = baseMagicNumber = PROGRESS_M;
                    cardsToPreview = new PaleFir();
                    rawDescription = EXTENDED_DESCRIPTION[2];
                    initializeDescription();
                }
            });
        }};
    }
}
