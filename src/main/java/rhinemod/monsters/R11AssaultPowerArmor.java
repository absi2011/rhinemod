package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import rhinemod.powers.NoStun;

public class R11AssaultPowerArmor extends CustomMonster {
    public static final String ID = "rhinemod:R11";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final int StunStrike = 10;
    public static final int StunNum = 5;

    public R11AssaultPowerArmor(float x, float y) {
        super(NAME, ID, 140, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(150);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            damage.add(new DamageInfo(this, 12));
            damage.add(new DamageInfo(this, 24));
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            damage.add(new DamageInfo(this, 11));
            damage.add(new DamageInfo(this, 22));
        }
        else {
            damage.add(new DamageInfo(this, 10));
            damage.add(new DamageInfo(this, 20));
        }
        loadAnimation("images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new NoStun(this)));
    }

    @Override
    public void takeTurn() {
        if (nextMove == 7) {
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(1)));
        }
        else {
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
            // TODO: MOVE!
        }
        if (nextMove <= 5) {
            setMove(MOVES[0], (byte)(nextMove + 1), Intent.ATTACK, damage.get(0).base);
        }
        else {
            setMove((byte)7, Intent.ATTACK, damage.get(1).base);
        }
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte)1, Intent.ATTACK, damage.get(0).base);
    }
}
