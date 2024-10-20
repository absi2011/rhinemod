package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import rhinemod.powers.NoStun;
import rhinemod.vfx.R31MoveEffect;

public class R31HeavyPowerArmor extends AbstractRhineMonster {
    public static final String ID = "rhinemod:R31";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final int stunStrike = 10;
    public static final int stunNum = 5;
    private static float moveX;

    public R31HeavyPowerArmor(float x, float y) {
        super(NAME, ID, 140, 0, 0, 220.0F, 360.0F, null, x, y);
        type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(150);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            damage.add(new DamageInfo(this, 5));
            damage.add(new DamageInfo(this, 30));
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            damage.add(new DamageInfo(this, 4));
            damage.add(new DamageInfo(this, 25));
        } else {
            damage.add(new DamageInfo(this, 3));
            damage.add(new DamageInfo(this, 20));
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1255_lybgpa/enemy_1255_lybgpa33.atlas", "resources/rhinemod/images/monsters/enemy_1255_lybgpa/enemy_1255_lybgpa33.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        moveX = (drawX - hb.width * 0.5F - (AbstractDungeon.player.hb.cX + AbstractDungeon.player.hb.width / 2)) / 6;
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new NoStun(this)));
    }

    @Override
    public void takeTurn() {
        if (nextMove == 5) {
            state.setAnimation(0, "Skill_Begin", false);
            state.addAnimation(0, "Skill_Loop", false, 0);
            state.addAnimation(0, "Skill_End", false, 0);
            state.addAnimation(0, "Idle", true, 0);
            for (int i = 1; i <= stunStrike; i++) {
                addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
            }
            addToBot(new MakeTempCardInDrawPileAction(new Dazed(), stunNum, false, true)) ;
        } else if (nextMove >= 6) {
            state.setAnimation(0, "Attack", false);
            state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(1)));
        } else {
            state.setAnimation(0, "Move", false);
            state.addAnimation(0, "Move", false, 0);
            moveX = (drawX - hb.width * 0.5F - (AbstractDungeon.player.hb.cX + AbstractDungeon.player.hb.width / 2)) / (7 - nextMove);
            AbstractDungeon.effectList.add(new R31MoveEffect(this, moveX));
            state.addAnimation(0, "Idle", true, 0);
        }
        if (nextMove <= 3) {
            setMove(MOVES[0], (byte)(nextMove + 1), Intent.UNKNOWN);
        } else if ((nextMove == 4) || (nextMove == 8)) {
            setMove(MOVES[1], (byte)5, Intent.ATTACK_DEBUFF, damage.get(0).base, stunStrike, true);
        } else {
            setMove((byte)(nextMove + 1), Intent.ATTACK, damage.get(1).base);
        }
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
    }
}
