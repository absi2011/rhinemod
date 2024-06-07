package rhinemod.cards;

import com.badlogic.gdx.Gdx;
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
    private double rotationTimer;
    private int previewIndex;
    ArrayList<AbstractCard> cards;
    public EcologicalInteraction() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        realBranch = 3;
        cards = RhineMod.getPlantCards();
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

    public void update() {
        super.update();
        if ((cards == null) || (cards.isEmpty())) {
            return;
        }
        if (this.hb.hovered) {
            if (this.rotationTimer <= 0.0F) {
                this.rotationTimer = 2.0F;
                this.cardsToPreview = this.cards.get(this.previewIndex);
                if (this.previewIndex == this.cards.size() - 1) {
                    this.previewIndex = 0;
                } else {
                    this.previewIndex++;
                }
            } else {
                this.rotationTimer -= Gdx.graphics.getDeltaTime();
            }
        }
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
