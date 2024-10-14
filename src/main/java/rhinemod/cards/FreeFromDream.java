package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.ExhaustUnscrupulousAction;
import rhinemod.actions.FreeFromDreamAction;
import rhinemod.cards.special.Pioneer;
import rhinemod.cards.special.Unscrupulous;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.patches.RhineTags;
import rhinemod.powers.Stunned;
import rhinemod.powers.WaterDamage;

import java.util.ArrayList;
import java.util.List;

public class FreeFromDream extends AbstractRhineCard {
    public static final String ID = "rhinemod:FreeFromDream";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/FreeFromDream.png";
    public static final String IMG_S = "resources/rhinemod/images/cards/FreeFromDreamS.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 7;
    public static final int UPGRADE_NONE_DMG = 3;
    public static final int VUL = 2;
    public static final int UPGRADE_VUL = 1;
    public static final int WATER_VAL = 16;
    public static final int EXHAUST_NUM = 4;
    public static final int SARIA_COST = 0;

    public FreeFromDream() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = VUL;
        secondMagicNumber = baseSecondMagicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (chosenBranch == 0) {
            addToBot(new FreeFromDreamAction(m, damage, magicNumber));
        } else if (chosenBranch == 2) {
            for (AbstractMonster mo: AbstractDungeon.getCurrRoom().monsters.monsters)
                if (mo.hasPower(Stunned.POWER_ID))
                    addToBot(new ApplyPowerAction(mo, p, new WaterDamage(mo, secondMagicNumber)));
        } else if (chosenBranch == 1) {
            int cnt = 0;
            for (AbstractCard c : p.hand.group)
                if (c instanceof Unscrupulous)
                    cnt++;
            for (AbstractCard c : p.discardPile.group)
                if (c instanceof Unscrupulous)
                    cnt++;
            for (AbstractCard c : p.drawPile.group)
                if (c instanceof Unscrupulous)
                    cnt++;
            addToBot(new ExhaustUnscrupulousAction(-1));
            if (cnt >= secondMagicNumber) {
                addToBot(new MakeTempCardInHandAction(new Pioneer()));
            }
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeDamage(UPGRADE_NONE_DMG);
                    upgradeMagicNumber(UPGRADE_VUL);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(1);
                    rawDescription = EXTENDED_DESCRIPTION[0];
                    exhaust = true;
                    cardsToPreview = new Pioneer();
                    type = CardType.SKILL;
                    target = CardTarget.SELF;
                    textureImg = IMG_S;
                    loadCardImage(textureImg);
                    upgradeBaseCost(SARIA_COST);
                    upgradeSecondMagicNumber(EXHAUST_NUM);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(3);
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    type = CardType.SKILL;
                    target = CardTarget.SELF;
                    textureImg = IMG_S;
                    loadCardImage(textureImg);
                    upgradeSecondMagicNumber(WATER_VAL);
                    tags.add(RhineTags.APPLY_WATER);
                    initializeDescription();
                }
            });
        }};
    }
}
