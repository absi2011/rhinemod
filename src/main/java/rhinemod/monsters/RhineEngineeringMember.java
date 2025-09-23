package rhinemod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import rhinemod.RhineMod;
import rhinemod.characters.RhineLab;
import rhinemod.powers.FeedingPower;
import rhinemod.vfx.R31MoveEffect;

public class RhineEngineeringMember extends AbstractRhineMonster {
    public static final String ID = "rhinemod:RhineEngineeringMember";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    private float repairPercent = 0.15F;
    Dor1 summon = null;
    int lastMove = 1;

    public RhineEngineeringMember(float x, float y) {
        super(NAME, ID, 300, 0, 0, 220.0F, 360.0F, null, x, y);
        type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(320);
        }
        if (RhineMod.tagLevel >= 3) {
            repairPercent = 0.3F;
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            damage.add(new DamageInfo(this, 20));
        }
        else if (AbstractDungeon.ascensionLevel >= 4) {
            damage.add(new DamageInfo(this, 17));
        }
        else {
            damage.add(new DamageInfo(this, 15));
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1256_lyacpa/enemy_1256_lyacpa33.atlas", "resources/rhinemod/images/monsters/enemy_1256_lyacpa/enemy_1256_lyacpa33.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
    }

    @Override
    public void takeTurn()
    {
        if (nextMove == 1) {
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m instanceof Dorothy) {
                    addToBot(new DamageAction(m, damage.get(0)));
                }
            }
        }
        else if (nextMove == 2) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if ((m instanceof SleepingR31) || (m instanceof R31HeavyPowerArmor)) {
                    addToBot(new HealAction(m, this, MathUtils.floor(m.maxHealth * repairPercent)));
                }
            }
        }
        else {
            summon = new Dor1(0.0F, 0.0F);
            addToBot(new SpawnMonsterAction(summon, true));
        }
        getMove(0);
    }
    @Override
    protected void getMove(int i) {
        if ((summon == null) || (summon.isDeadOrEscaped()))
        {
            setMove((byte) 50, Intent.UNKNOWN);
        }
        else
        {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if ((m instanceof SleepingR31) || (m instanceof R31HeavyPowerArmor)) {
                    break;
                }
            }
            if (lastMove == 0) {
                lastMove = 1;
                setMove((byte)1, Intent.ATTACK, damage.get(0).base);
            }
            else {
                lastMove = 0;
                setMove((byte)2, Intent.BUFF);
            }
        }
    }

    @Override
    public void die() {
        super.die();
        summon.die();
    }
}
