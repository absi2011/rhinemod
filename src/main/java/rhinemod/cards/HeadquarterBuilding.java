package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.RhineMod;
import rhinemod.actions.MakeSeveralCardsInHandAction;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class HeadquarterBuilding extends AbstractRhineCard {
    public static final String ID = "rhinemod:HeadquarterBuilding";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/HeadquarterBuilding.png";
    public static final int COST = 1;
    public static final int PROGRESS = 5;
    public static final int EXTRA_PROGRESS = 2;
    public static final int PLANT_AMT = 2;
    public static final int DRAW_AMT = 2;
    public HeadquarterBuilding() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = PROGRESS;
        exhaust = true;
        isInnate = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (chosenBranch == 0) {
            addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
        } else {
            ArrayList<AbstractCard> plantCards = RhineMod.getPlantCards();
            ArrayList<AbstractCard> list = new ArrayList<>();
            for (int i = 0; i < magicNumber; i++) {
                list.add(plantCards.get(AbstractDungeon.cardRng.random(plantCards.size() - 1)).makeCopy());
            }
            addToBot(new MakeSeveralCardsInHandAction(list));
            addToBot(new DrawCardAction(secondMagicNumber));
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(EXTRA_PROGRESS);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    magicNumber = baseMagicNumber = PLANT_AMT;
                    secondMagicNumber = baseSecondMagicNumber = DRAW_AMT;
                    name = EXTENDED_DESCRIPTION[0];
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    initializeTitle();
                    initializeDescription();
                }
            });
        }};
    }
}
