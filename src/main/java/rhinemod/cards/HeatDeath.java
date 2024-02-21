package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.HeatDeathAction;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.monsters.BlackHole;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class HeatDeath extends AbstractRhineCard {
    public static final String ID = "rhinemod:HeatDeath";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "images/cards/HighSpeedResonatingTroubleshooter.png";
    public static final int COST = 3;
    public static final int UPGRADE_COST = 2;
    public HeatDeath() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
        realBranch = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        float leftX = 1000.0F;
        for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters)
            if (!ms.isDeadOrEscaped()) {
                leftX = Math.min(leftX, (ms.hb.x - Settings.WIDTH * 0.75F) / Settings.xScale);
            }
        AbstractMonster bh = new BlackHole(leftX - 110.0F, 0.0F);
        bh.setMove((byte)0, AbstractMonster.Intent.UNKNOWN);
        bh.createIntent();
        bh.usePreBattleAction();
        addToBot(new SpawnMonsterAction(bh, false));
        addToBot(new HeatDeathAction());
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
