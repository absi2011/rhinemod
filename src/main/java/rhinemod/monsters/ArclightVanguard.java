package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ShiftingPower;
import rhinemod.powers.DamageOutPower;
import rhinemod.powers.Fragile;

public class ArclightVanguard extends CustomMonster {
    public static final String ID = "rhinemod:ArclightVanguard";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final int AttackHPLoss = 5;
    public static final int LowDamageTimes = 2;
    public static final int HighDamageTimes = 6;

    public ArclightVanguard(float x, float y) {
        super(NAME, ID, 40, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(43);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            damage.add(new DamageInfo(this, 5));
            damage.add(new DamageInfo(this, 14));
            damage.add(new DamageInfo(this, 3));
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            damage.add(new DamageInfo(this, 4));
            damage.add(new DamageInfo(this, 12));
            damage.add(new DamageInfo(this, 3));
        }
        else {
            damage.add(new DamageInfo(this, 4));
            damage.add(new DamageInfo(this, 10));
            damage.add(new DamageInfo(this, 2));
        }
        loadAnimation("images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new Fragile(this)));
    }

    @Override
    public void takeTurn() {
        if (nextMove == 1) {
            for (int i = 1; i <= LowDamageTimes; i++) {
                addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
            }
        }
        else if (nextMove == 2) {
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(1)));
            addToBot(new DamageAction(this, new DamageInfo(this, AttackHPLoss, DamageInfo.DamageType.HP_LOSS)));
        }
        else {
            for (int i = 1; i <= HighDamageTimes; i++) {
                addToBot(new DamageAction(AbstractDungeon.player, damage.get(2)));
            }
            addToBot(new DamageAction(this, new DamageInfo(this, 2 * AttackHPLoss, DamageInfo.DamageType.HP_LOSS)));
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        int nextMove = AbstractDungeon.aiRng.random(1, 3);
        if (nextMove == 1) {
            setMove((byte)1, Intent.ATTACK, damage.get(0).base,LowDamageTimes,true);
        }
        else if (nextMove == 2) {
            setMove((byte)2, Intent.ATTACK, damage.get(1).base);
        }
        else {
            setMove((byte)3, Intent.ATTACK, damage.get(2).base,HighDamageTimes,true);
        }
    }
}
