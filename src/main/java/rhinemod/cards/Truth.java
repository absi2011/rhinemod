package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.cards.special.Egotist;
import rhinemod.characters.RhineLab;
import rhinemod.characters.StarRing;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class Truth extends AbstractRhineCard {
    public static final String ID = "rhinemod:Truth";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/Truth.png";
    public static final int COST = 1;
    public static final int RING_NEED = 3;
    public static final int UPGRADE_MINUS_RING_NEED = -1;

    public Truth() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = RING_NEED;
        realBranch = 2;
        cardsToPreview = new Egotist();
    }

    @Override
    public boolean extraTriggered() {
        return starRingCnt() >= magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.player instanceof RhineLab) {
            RhineLab player = (RhineLab)AbstractDungeon.player;
            player.currentRings.removeIf(r -> r.isDead);
            boolean firstRing = true;
            for (StarRing s: player.currentRings) {
                s.startOfTurnDamage(firstRing);
                firstRing = false;
            }
        }
        if (extraTriggered()) {
            addToBot(new MakeTempCardInHandAction(new Egotist()));
        }
    }

    int starRingCnt() {
        if (AbstractDungeon.player instanceof RhineLab) {
            return ((RhineLab) AbstractDungeon.player).currentRings.size();
        } else {
            return 0;
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_MINUS_RING_NEED);
                    initializeDescription();
                }
            });
        }};
    }
}
