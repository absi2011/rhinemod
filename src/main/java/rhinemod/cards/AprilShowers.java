package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Seek;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddCalciumAction;
import rhinemod.actions.AddFlowingShapeAction;
import rhinemod.cards.special.Seeker;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.powers.ResearchProgress;

import java.util.ArrayList;
import java.util.List;

public class AprilShowers extends AbstractRhineCard {
    public static final String ID = "rhinemod:AprilShowers";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/PaleFir.png";
    public static final int COST = 1;
    public static final int BLOCK_GAIN = 8;
    public static final int UPGRADE_PLUS_BLOCK = 3;
    public AprilShowers() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_GAIN;
        cardsToPreview = new Seeker();
    }

    @Override
    public boolean extraTriggered() {
        boolean[] exist = new boolean[4];
        for (AbstractCard c : AbstractDungeon.player.hand.group)
            if (c instanceof AbstractRhineCard) {
                if (((AbstractRhineCard) c).realBranch != -1)
                {
                    exist[((AbstractRhineCard) c).realBranch] = true;
                }
            }
        return exist[1] && exist[2] && exist[3];
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (extraTriggered()) {
            addToBot(new MakeTempCardInHandAction(new Seeker()));
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeBlock(UPGRADE_PLUS_BLOCK);
                    initializeDescription();
                }
            });
        }};
    }
}
