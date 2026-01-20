package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
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

public class Dor1 extends AbstractRhineMonster {
    public static final String ID = "rhinemod:Dor1";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    int turn = 0;

    public Dor1(float x, float y) {
        super(NAME, ID, 28, 0, 0, 160.0F, 200.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (RhineMod.tagLevel >= 3) {
            setHp(60);
        }
        else if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(30);
        }
        else {
            setHp(28);
        }
        if (RhineMod.tagLevel >= 1) {
            damage.add(new DamageInfo(this, 10));
            damage.add(new DamageInfo(this, 10));
        }
        else if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 10));
            damage.add(new DamageInfo(this, 5));
        }
        else if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 9));
            damage.add(new DamageInfo(this, 4));
        }
        else {
            damage.add(new DamageInfo(this, 8));
            damage.add(new DamageInfo(this, 4));
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1251_lysyta/enemy_1251_lysyta.atlas", "resources/rhinemod/images/monsters/enemy_1251_lysyta/enemy_1251_lysyta37.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (RhineMod.tagLevel >= 2) {
            addToBot(new ApplyPowerAction(this, this, new IntangiblePower(this, 1)));
        }
    }

    @Override
    public void takeTurn() {
        turn ++;
        if (nextMove == 50) {
            AbstractPlayer p = AbstractDungeon.player;
            float destX, destY;
            if (p instanceof RhineLab) {
                destX = (drawX - hb.width * 0.6F - (p.hb.cX + p.hb.width * 0.6F));
            }
            else {
                destX = (drawX - hb.width * 0.5F - (p.hb.cX + p.hb.width * 0.5F));
            }
            destY = (drawY - hb.height * 0.5F - (p.hb.cY + p.hb.height * 0.5F));
            AbstractDungeon.effectList.add(new R31MoveEffect(this, destX));
            // TODO: 跑到动力甲那，但是可能这里只有横向移动，也许要再写个effect？
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if ((!m.isDeadOrEscaped()) && (m instanceof SleepingR31)) {
                    m.getPower(FeedingPower.POWER_ID).onSpecificTrigger();
                    addToBot(new SuicideAction(this, false));
                    break;
                }
            }
        }
        else {
            AbstractCreature target = null;
            DamageInfo info;
            if (nextMove == 1) {
                target = AbstractDungeon.player;
                info = damage.get(0);
            }
            else {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m instanceof Dorothy) {
                        target = m;
                        break;
                    }
                }
                if (target == null) {
                    getMove(0);
                    return;
                }
                info = damage.get(1);
            }
            addToBot(new DamageAction(target, info));
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        if (turn == 3) {
            setMove((byte) 50, Intent.UNKNOWN);
        }
        else {
            int possibleMoves = 1;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if ((!m.isDeadOrEscaped()) && (m instanceof Dorothy)) {
                    possibleMoves = 2;
                }
            }
            if ((possibleMoves == 1) || (AbstractDungeon.monsterRng.randomBoolean())) {
                setMove((byte) 1, Intent.ATTACK, damage.get(0).base);
                attackTarget = AttackTarget.PLAYER;
            }
            else {
                setMove((byte) 2, Intent.ATTACK, damage.get(1).base);
                attackTarget = AttackTarget.DOROTHY;
            }
        }
    }
}
