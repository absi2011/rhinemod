package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FlightPower;

public class ArclightCommando extends CustomMonster {
    public static final String ID = "rhinemod:ArclightCommando";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String NAME = monsterStrings.NAME;
    public final int FlightNumber;
    boolean isFlying;

    public ArclightCommando(float x, float y) {
        super(NAME, ID, 40, 0, 0, 150.0F, 320.0F, null, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(43);
            FlightNumber = 4;
        }
        else {
            FlightNumber = 3;
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.damage.add(new DamageInfo(this, 5));
            this.damage.add(new DamageInfo(this, 10));
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.damage.add(new DamageInfo(this, 5));
            this.damage.add(new DamageInfo(this, 9));
        }
        else {
            this.damage.add(new DamageInfo(this, 4));
            this.damage.add(new DamageInfo(this, 8));
        }
        loadAnimation("images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        isFlying = false;
    }

    public void applyPowers() {
        super.applyPowers();
        if (this.hasPower("rhinemod:Stunned") && this.isFlying) {
            addToBot(new ChangeStateAction(this, "GROUNDED"));
        }
    }

    public void changeState(String stateName) {
        if (stateName.equals("GROUNDED")) {
            isFlying = false;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, "Flight"));
            this.setMove((byte)20, Intent.STUN);
            this.createIntent();
            // TODO: 落地特效
        }
        else if (stateName.equals("FLYING")) {
            isFlying = true;
            // TODO: 飞行特效
        }

        /* 鸟的特效参考
        AnimationState.TrackEntry e;
        switch (stateName) {
            case "FLYING":
                this.loadAnimation("images/monsters/theCity/byrd/flying.atlas", "images/monsters/theCity/byrd/flying.json", 1.0F);
                e = this.state.setAnimation(0, "idle_flap", true);
                e.setTime(e.getEndTime() * MathUtils.random());
                this.updateHitbox(0.0F, 50.0F, 240.0F, 180.0F);
                break;
            case "GROUNDED":
                this.setMove((byte)4, Intent.STUN);
                this.createIntent();
                this.isFlying = false;
                this.loadAnimation("images/monsters/theCity/byrd/grounded.atlas", "images/monsters/theCity/byrd/grounded.json", 1.0F);
                e = this.state.setAnimation(0, "idle", true);
                e.setTime(e.getEndTime() * MathUtils.random());
                this.updateHitbox(10.0F, -50.0F, 240.0F, 180.0F);
        }
        */
    }

    @Override
    public void takeTurn() {
        if ((this.nextMove == 2) || (this.nextMove == 3)) {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
        }
        else if (this.nextMove == 11) {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
        }
        else if (this.nextMove == 4) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "FLYING"));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FlightPower(this, FlightNumber)));
        }
        if ((this.nextMove == 1) || (this.nextMove == 2)) {
            setMove(MOVES[1], (byte)4, Intent.BUFF);
        }
        else if ((this.nextMove == 4) || (this.nextMove == 11)) {
            setMove((byte)11, Intent.ATTACK, this.damage.get(1).base);
        }
        else {
            setMove((byte)3, Intent.ATTACK, this.damage.get(0).base);
        }
    }

    @Override
    protected void getMove(int i) {
        if (AbstractDungeon.ascensionLevel >= 17) {
            setMove((byte)2, Intent.ATTACK, this.damage.get(0).base);
        }
        else {
            setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
        }
    }
}
