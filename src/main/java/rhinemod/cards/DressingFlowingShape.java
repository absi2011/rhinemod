package rhinemod.cards;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddFlowingShapeAction;
import rhinemod.actions.ReduceCalciumAction;
import rhinemod.characters.RhineLab;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.WaterDamage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DressingFlowingShape extends AbstractRhineCard {
    public static final String ID = "rhinemod:DressingFlowingShape";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/PaleFir.png";
    public static final int COST = 1;
    public static final int BASIC_BLOCK = 8;
    public static final int UPGRADE_BLOCK = 3;
    public static final int CARDS_DRAW = 2;
    public static final int FLOWSP = 2;
    public static final int WATER_DAMAGE = 4;
    public static final int EXTRA_BLOCK = 5;
    public int currentState = 0;
    private double rotationTimer;
    private int previewIndex;
    ArrayList<AbstractCard> cards = new ArrayList<>();
    public DressingFlowingShape() {
        this(0);
    }
    public DressingFlowingShape(int state) {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.COMMON, CardTarget.SELF);
        realBranch = 3;
        block = baseBlock = BASIC_BLOCK;
        currentState = state;
        rawDescription = EXTENDED_DESCRIPTION[currentState];
        initializeDescription();
        if (state == 0) {
            cards.add(new DressingFlowingShape(1));
            cards.add(new DressingFlowingShape(2));
            cards.add(new DressingFlowingShape(3));
            cards.add(new DressingFlowingShape(4));
        }
        else if (state == 1) {
            magicNumber = baseMagicNumber = CARDS_DRAW;
        }
        else if (state == 2) {
            magicNumber = baseMagicNumber = WATER_DAMAGE;
            target = CardTarget.ENEMY;
        }
        else if (state == 3) {
            magicNumber = baseMagicNumber = FLOWSP;
        }
        else if (state == 4) {
            magicNumber = baseMagicNumber = EXTRA_BLOCK;
        }
    }

    @Override
    public void applyPowers() {
        if (currentState == 4) {
            int temp = baseBlock;
            baseBlock = baseMagicNumber;
            super.applyPowers();
            magicNumber = block;
            isMagicNumberModified = (magicNumber != baseMagicNumber);
            baseBlock = temp;
            super.applyPowers();
        }
        else {
            super.applyPowers();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (currentState == 1) {
            addToBot(new DrawCardAction(magicNumber));
        }
        else if (currentState == 2) {
            addToBot(new ApplyPowerAction(m, p, new WaterDamage(m, magicNumber)));
        }
        else if (currentState == 3) {
            addToBot(new AddFlowingShapeAction(magicNumber));
        }
        else if (currentState == 4) {
            addToBot(new GainBlockAction(p, magicNumber));
        }
    }

    @Override
    public void triggerWhenDrawn() {
        int lastState = currentState;
        for (;currentState == lastState;) {
            currentState = AbstractDungeon.cardRng.random(1, 4);
        }
        rawDescription = EXTENDED_DESCRIPTION[currentState];
        initializeDescription();
        if (currentState == 1) {
            magicNumber = baseMagicNumber = CARDS_DRAW;
            target = CardTarget.SELF;
        }
        else if (currentState == 2) {
            magicNumber = baseMagicNumber = WATER_DAMAGE;
            target = CardTarget.ENEMY;
        }
        else if (currentState == 3) {
            magicNumber = baseMagicNumber = FLOWSP;
            target = CardTarget.SELF;
        }
        else if (currentState == 4) {
            magicNumber = baseMagicNumber = EXTRA_BLOCK;
            target = CardTarget.SELF;
        }
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
                    upgradeBlock(UPGRADE_BLOCK);
                    initializeDescription();
                }
            });
        }};
    }

    @Override
    public AbstractCard makeCopy() {
        return new DressingFlowingShape(currentState);
    }
}
