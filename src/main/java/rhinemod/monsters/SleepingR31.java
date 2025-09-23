package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rhinemod.RhineMod;
import rhinemod.characters.RhineLab;
import rhinemod.powers.DreamBreakPower;
import rhinemod.powers.FeedingPower;
import rhinemod.powers.NoStun;
import rhinemod.powers.R11LoseHPPower;
import rhinemod.vfx.R11MoveEffect;

public class SleepingR31 extends AbstractRhineMonster {
    public static final String ID = "rhinemod:R31";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    public int feedLeft = 0;

    public SleepingR31(float x, float y) {
        super(NAME, ID, 1000, 0, 0, 220.0F, 360.0F, null, x, y);
        type = EnemyType.BOSS;
        if (RhineMod.tagLevel >= 1) {
            currentHealth = 400;
        }
        else if (AbstractDungeon.ascensionLevel >= 9) {
            currentHealth = 300;
        }
        else {
            currentHealth = 250;
        }
        if (RhineMod.tagLevel >= 3) {
            feedLeft = 1;
        }
        else {
            feedLeft = 2;
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1256_lyacpa/enemy_1256_lyacpa33.atlas", "resources/rhinemod/images/monsters/enemy_1256_lyacpa/enemy_1256_lyacpa33.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new NoStun(this)));
        addToBot(new ApplyPowerAction(this, this, new FeedingPower(this, feedLeft)));
    }

    @Override
    public void takeTurn()
    {
        if (RhineMod.tagLevel >= 2) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m instanceof Dorothy) {
                    addToBot(new DamageAction(m, new DamageInfo(this, 10, DamageInfo.DamageType.THORNS)));
                    addToBot(new ApplyPowerAction(m, this, new DreamBreakPower(m, 5)));
                }
            }
        }
        getMove(0);
    }

    @Override
    public void heal(int healAmount, boolean showEffect) {
        heal(healAmount);
    }

    public void Awaken() {
        float offsetX = (drawX - Settings.WIDTH * 0.75F) / Settings.xScale;
        float offsetY = (drawY - AbstractDungeon.floorY) / Settings.yScale;
        R31HeavyPowerArmor m;
        m = new R31HeavyPowerArmor(offsetX, offsetY);
        m.maxHealth = this.maxHealth;
        m.currentHealth = this.currentHealth;

        m.init();
        m.applyPowers();
        AbstractDungeon.getCurrRoom().monsters.addMonster(getSmartPosition(m), m);
        if (ModHelper.isModEnabled("Lethality")) {
            this.addToBot(new ApplyPowerAction(m, m, new StrengthPower(m, 3), 3));
        }

        if (ModHelper.isModEnabled("Time Dilation")) {
            this.addToBot(new ApplyPowerAction(m, m, new SlowPower(m, 0)));
        }
        m.animX = 0.0F;
        m.showHealthBar();
        m.usePreBattleAction();
        m.SpecialMove();
        m.createIntent();
        if (RhineMod.tagLevel >= 1) {
            addToBot(new ApplyPowerAction(m, m, new StrengthPower(m, 3)));
        }
        AbstractDungeon.getCurrRoom().monsters.monsters.remove(this);
        super.die();
    }

    @Override
    public void heal(int healAmount) {
        super.heal(healAmount);
        if (currentHealth == maxHealth) {
            Awaken();
        }
    }

    private int getSmartPosition(AbstractMonster m) {
        int position = 0;

        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!(m.drawX > mo.drawX)) {
                break;
            }
            ++position;
        }

        return position;
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)0, Intent.SLEEP);
    }
}
