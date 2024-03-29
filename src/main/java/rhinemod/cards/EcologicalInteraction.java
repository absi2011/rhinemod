package rhinemod.cards;

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

import java.util.ArrayList;
import java.util.List;

public class EcologicalInteraction extends AbstractRhineCard {
    public static final String ID = "rhinemod:EcologicalInteraction";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/EcologicalInteraction.png";
    public static final int COST = -1;
    public EcologicalInteraction() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        realBranch = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cnt = energyOnUse;
        if (upgraded) cnt++;
        ArrayList<AbstractCard> plantCards = RhineMod.getPlantCards();
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            list.add(plantCards.get(AbstractDungeon.cardRng.random(plantCards.size() - 1)).makeCopy());
        }
        addToBot(new MakeSeveralCardsInHandAction(list));
        if (!freeToPlayOnce) p.energy.use(energyOnUse);
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    rawDescription = UPGRADE_DESCRIPTION;
                    initializeDescription();
                }
            });
        }};
    }
}
