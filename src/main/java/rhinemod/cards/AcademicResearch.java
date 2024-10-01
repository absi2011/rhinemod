package rhinemod.cards;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.RhineMod;
import rhinemod.actions.AddCalciumAction;
import rhinemod.actions.ChooseSpecificCardAction;
import rhinemod.actions.RemoveCardAction;
import rhinemod.cards.special.BipolarNebula;
import rhinemod.cards.special.GiantRing;
import rhinemod.cards.special.StellarRing;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AcademicResearch extends AbstractRhineCard {
    public static final String ID = "rhinemod:AcademicResearch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/AcademicResearch.png";
    public static final String IMG_S = "resources/rhinemod/images/cards/AcademicResearchS.png";
    public static final int COST = 1;
    public static final int[] UPGRADE_COST = {0, 0, 2, 1};
    public static final int S_BLK = 4;
    public static final int S_DMG = 4;
    public static final int S_CA = 4;
    public static final int K_RP = 10;
    public static final int K_TH = 14;
    private double rotationTimer;
    private int previewIndex;
    ArrayList<AbstractCard> cards = new ArrayList<>();
    public AcademicResearch() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (chosenBranch) {
            case 0:
                addToBot(new DiscoveryAction());
                break;
            case 1:
                addToBot(new RemoveCardAction(uuid));
                addToBot(new GainBlockAction(p, block));
                addToBot(new DamageAction(m, new DamageInfo(p, damage)));
                addToBot(new AddCalciumAction(magicNumber));
                break;
            case 2:
                addToBot(new ApplyPowerAction(p, p, new ResearchProgress(p, magicNumber)));
                int cur = p.hasPower(ResearchProgress.POWER_ID)? p.getPower(ResearchProgress.POWER_ID).amount : 0;
                cur += magicNumber;
                if (cur >= secondMagicNumber) {
                    ArrayList<AbstractCard> list = new ArrayList<>();
                    list.add(new GiantRing());
                    list.add(new BipolarNebula());
                    list.add(new StellarRing());
                    addToBot(new ChooseSpecificCardAction(list));
                }
                break;
            case 3:
                ArrayList<AbstractCard> list = RhineMod.getPlantCards();
                Collections.shuffle(list, new Random(AbstractDungeon.cardRng.randomLong()));
                addToBot(new ChooseSpecificCardAction(new ArrayList<>(list.subList(0, 3))));
                break;
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
                    upgradeBaseCost(UPGRADE_COST[0]);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(1);
                    upgradeBaseCost(UPGRADE_COST[1]);
                    block = baseBlock = S_BLK;
                    damage = baseDamage = S_DMG;
                    magicNumber = baseMagicNumber = S_CA;
                    target = CardTarget.ENEMY;
                    type = CardType.ATTACK;
                    textureImg = IMG_S;
                    loadCardImage(textureImg);
                    name = EXTENDED_DESCRIPTION[0];
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    initializeTitle();
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2);
                    upgradeBaseCost(UPGRADE_COST[2]);
                    name = EXTENDED_DESCRIPTION[2];
                    rawDescription = EXTENDED_DESCRIPTION[3];
                    magicNumber = baseMagicNumber = K_RP;
                    secondMagicNumber = baseSecondMagicNumber = K_TH;
                    exhaust = false;
                    cards.add(new GiantRing());
                    cards.add(new BipolarNebula());
                    cards.add(new StellarRing());
                    initializeTitle();
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    upgradeBaseCost(UPGRADE_COST[3]);
                    name = EXTENDED_DESCRIPTION[4];
                    rawDescription = EXTENDED_DESCRIPTION[5];
                    exhaust = false;
                    cards = RhineMod.getPlantCards();
                    initializeTitle();
                    initializeDescription();
                }
            });
        }};
    }
}
