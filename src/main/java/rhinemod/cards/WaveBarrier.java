package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import rhinemod.actions.HeatDeathAction;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.monsters.BlackHole;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.relics.Melt;

import java.util.ArrayList;
import java.util.List;

public class WaveBarrier extends AbstractRhineCard {
    public static final String ID = "rhinemod:WaveBarrier";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/HighSpeedResonatingTroubleshooter.png";
    public static final int COST = 2;
    public static final int BARRIER = 4;
    public static final int UPGRADE_BARRIER = 1;
    public WaveBarrier() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = BARRIER;
        realBranch = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BufferPower(p, magicNumber)));
        if (p.hasRelic(Melt.ID))
        {
            p.getRelic(Melt.ID).counter = 3;
        }
        else
        {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new Melt());
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_BARRIER);
                    initializeDescription();
                }
            });
        }};
    }
}
