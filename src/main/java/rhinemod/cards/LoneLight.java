package rhinemod.cards;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.ChooseSpecificCardAction;
import rhinemod.cards.special.*;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class LoneLight extends AbstractRhineCard {
    public static final String ID = "rhinemod:LoneLight";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/LoneLight.png";
    public static final int COST = 2;
    public static final int UPGRADE_COST = 1;
    private double rotationTimer;
    private int previewIndex;
    ArrayList<AbstractCard> cards = new ArrayList<>();
    public LoneLight() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
        realBranch = 2;
        cards.add(new Egotist());
        cards.add(new Traitor());
        cards.add(new Seeker());
        cards.add(new Loner());
        cards.add(new Pioneer());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChooseSpecificCardAction(cards));
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
                    upgradeBaseCost(UPGRADE_COST);
                    initializeDescription();
                }
            });
        }};
    }
}
