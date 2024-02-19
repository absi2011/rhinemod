package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FlightPower;

public class MilitaryBeckbeast extends CustomMonster {
    public static final String ID = "rhinemod:MilitaryBeckbeast";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String NAME = monsterStrings.NAME;
    public final int AttackBlock;
    public final int OnlyBlock;
    boolean isFlying;

    public MilitaryBeckbeast(float x, float y) {
        super(NAME, ID, 30, 0, 0, 150.0F, 320.0F, null, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(32);
            AttackBlock = 6;
            OnlyBlock = 12;
        }
        else {
            AttackBlock = 5;
            OnlyBlock = 10;
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.damage.add(new DamageInfo(this, 5));
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.damage.add(new DamageInfo(this, 4));
        }
        else {
            this.damage.add(new DamageInfo(this, 3));
        }
        loadAnimation("images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        isFlying = false;
    }

    public void changeState(String stateName) {
    }

    @Override
    public void takeTurn() {
        if (this.nextMove == 2) {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            addToBot(new GainBlockAction(this, AttackBlock));
        }
        else {
            addToBot(new GainBlockAction(this, OnlyBlock));
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        int nextMove = AbstractDungeon.aiRng.random(0, 1);
        if (nextMove == 1) {
            setMove((byte)1, Intent.DEFEND);
        }
        else {
            setMove((byte)2, Intent.ATTACK_DEFEND, this.damage.get(0).base);
        }
    }
}
