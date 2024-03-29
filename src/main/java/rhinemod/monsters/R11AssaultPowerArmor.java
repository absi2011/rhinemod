package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import rhinemod.powers.NoStun;

public class R11AssaultPowerArmor extends CustomMonster {
    public static final String ID = "rhinemod:R11";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

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
        else if (AbstractDungeon.ascensionLevel >= 3) {
            damage.add(new DamageInfo(this, 11));
            damage.add(new DamageInfo(this, 22));
        }
        else {
            damage.add(new DamageInfo(this, 10));
            damage.add(new DamageInfo(this, 20));
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1256_lyacpa/enemy_1256_lyacpa33.atlas", "resources/rhinemod/images/monsters/enemy_1256_lyacpa/enemy_1256_lyacpa33.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new NoStun(this)));
    }

    @Override
    public void takeTurn() {
        if (nextMove == 7) {
            state.setAnimation(0, "Attack_2", false);
            state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(1)));
        }
        else {
            state.setAnimation(0, "Attack", false);
            state.addAnimation(0, "Move", false, 0);
            state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
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
