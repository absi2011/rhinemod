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
import rhinemod.powers.Fragile;

public class Perpetrator extends CustomMonster {
    public static final String ID = "rhinemod:Perpetrator";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    int AttTimes;
    boolean isRush = false;

    public Perpetrator(float x, float y) {
        super(NAME, ID, 85, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(92);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 5));
            AttTimes = 2;
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 5));
            AttTimes = 2;
        }
        else {
            damage.add(new DamageInfo(this, 4));
            AttTimes = 2;
        }
        isRush = true;
        loadAnimation("images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    public void applyPowers() {
        super.applyPowers();
        if (hasPower("rhinemod:Stunned") && isRush) {
            addToBot(new ChangeStateAction(this, "STOPPED"));
        }
        else if (!hasPower("rhinemod:Stunned") && !isRush) {
            addToBot(new ChangeStateAction(this, "RUSH"));
        }
    }
    public void changeState(String stateName) {
        if (stateName.equals("STOPPED")) {
            isRush = false;
            //TODO
        }
        else if (stateName.equals("RUSH")) {
            isRush = true;
            //TODO: 要不要搞个冲刺残影？
        }
    }

    @Override
    public void takeTurn() {
        for (int i = 1; i <= AttTimes; i++) {
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        }
        if (!isRush) {
            if (AbstractDungeon.ascensionLevel < 17) {
                AttTimes--;
                if (AttTimes <= 1) {
                    AttTimes = 1;
                }
            }
        }
        else {
            AttTimes ++;
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, damage.get(0).base, AttTimes, (AttTimes > 1));
    }
}
