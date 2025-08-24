package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FlightPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import rhinemod.RhineMod;
import rhinemod.powers.Stunned;

public class ArclightCommando extends AbstractRhineMonster {
    public static final String ID = "rhinemod:ArclightCommando";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String NAME = monsterStrings.NAME;
    public int flightNumber;
    boolean isFlying;

    public ArclightCommando(float x, float y) {
        super(NAME, ID, 40, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(43);
            flightNumber = 4;
        } else {
            flightNumber = 3;
        }
        if (RhineMod.tagLevel >= 2) {
            flightNumber++;
        }
        if (RhineMod.tagLevel >= 2) {
            damage.add(new DamageInfo(this, 5));
            damage.add(new DamageInfo(this, 14));
        } else if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 5));
            damage.add(new DamageInfo(this, 10));
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 5));
            damage.add(new DamageInfo(this, 9));
        } else {
            damage.add(new DamageInfo(this, 4));
            damage.add(new DamageInfo(this, 8));
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1327_cbrokt/enemy_1327_cbrokt33.atlas", "resources/rhinemod/images/monsters/enemy_1327_cbrokt/enemy_1327_cbrokt33.json", 1.5F);
        state.setAnimation(0, "Idle_1", true);
        isFlying = false;
        flipHorizontal = true;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (hasPower("rhinemod:Stunned") && isFlying) {
            addToBot(new ChangeStateAction(this, "GROUNDED"));
        }
    }

    @Override
    public void usePreBattleAction() {
        if (RhineMod.tagLevel >= 3) {
            addToBot(new ChangeStateAction(this, "FLYING"));
            addToBot(new ApplyPowerAction(this, this, new FlightPower(this, flightNumber)));
            addToBot(new ApplyPowerAction(this, this, new PlatedArmorPower(this, 5)));
            addToBot(new GainBlockAction(this, 5));
        }
    }

    public void changeState(String stateName) {
        if (stateName.equals("GROUNDED")) {
            isFlying = false;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, "Flight"));
            setMove((byte)20, Intent.STUN);
            createIntent();
            state.setAnimation(0, "Crash", false);
            state.addAnimation(0, "Idle_1", true, 0);
        }
        else if (stateName.equals("FLYING")) {
            isFlying = true;
            state.setAnimation(0, "Takeoff", false);
            state.addAnimation(0, "Idle_2", true, 0);
        }
    }

    @Override
    public void takeTurn() {
        if (nextMove == 2 || nextMove == 3 || nextMove == 11) {
            if (isFlying) {
                state.setAnimation(0, "Attack_2", false);
                state.addAnimation(0, "Idle_2", true, 0);
            } else {
                state.setAnimation(0, "Attack_1", false);
                state.addAnimation(0, "Idle_1", true, 0);
            }
        }
        if (nextMove == 2 || nextMove == 3) {
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        } else if (nextMove == 11) {
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(1)));
        } else if (nextMove == 4) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, Stunned.POWER_ID));
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "FLYING"));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FlightPower(this, flightNumber)));
        }
        if (nextMove == 1 || nextMove == 2) {
            setMove(MOVES[1], (byte)4, Intent.BUFF);
        } else if (nextMove == 4 || nextMove == 11) {
            setMove((byte)11, Intent.ATTACK, damage.get(1).base);
        } else {
            setMove((byte)3, Intent.ATTACK, damage.get(0).base);
        }
    }

    @Override
    protected void getMove(int i) {
        if (RhineMod.tagLevel >= 3) {
            setMove((byte)11, Intent.ATTACK, damage.get(1).base);
        }
        else if (RhineMod.tagLevel >= 1) {
            setMove(MOVES[1], (byte)4, Intent.BUFF);
        } else if (AbstractDungeon.ascensionLevel >= 17) {
            setMove((byte)2, Intent.ATTACK, damage.get(0).base);
        } else {
            setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
        }
    }

    @Override
    public void addCCTags() {
        if (RhineMod.tagLevel == 1) {
            addTag(1);
        }
        else if (RhineMod.tagLevel == 2) {
            addTag(1);
            addTag(2);
        }
        else if (RhineMod.tagLevel == 3) {
            addTag(2);
            addTag(3);
        }
    }
}
