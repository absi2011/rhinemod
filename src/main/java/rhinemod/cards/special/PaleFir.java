package rhinemod.cards.special;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.patches.RhineTags;
import rhinemod.relics.OrangeStorm;

public class PaleFir extends CustomCard {
    public static final String ID = "rhinemod:PaleFir";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "resources/rhinemod/images/cards/PaleFir.png";
    private static final int COST = 0;
    private static final int BLOCK_AMT = 6;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int NO_ORANGE = 5;
    private static final int ORANGE = 3;

    public PaleFir() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        exhaust = true;
        tags.add(RhineTags.IS_PLANT);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = NO_ORANGE;
        tags.add(RhineTags.IS_PLANT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int extraBlock;
        if (hasOrange()) {
            extraBlock = (p.maxHealth - p.currentHealth) / NO_ORANGE;
        }
        else {
            extraBlock = (p.maxHealth - p.currentHealth) / ORANGE;
        }
        baseBlock += extraBlock;
        applyPowersToBlock();
        addToBot(new GainBlockAction(p, block));
        baseBlock -= extraBlock;
        applyPowersToBlock();
    }

    @Override
    public void triggerWhenCopied()
    {
        if (hasOrange()) {
            baseMagicNumber = ORANGE;
            magicNumber = ORANGE;
        }
    }

    @Override
    public void applyPowers() {
        if (hasOrange()) {
            isMagicNumberModified = true;
        }
        super.applyPowers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new PaleFir();
    }

    boolean hasOrange() {
        return AbstractDungeon.player.hasRelic(OrangeStorm.ID);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
