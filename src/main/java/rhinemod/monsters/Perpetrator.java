package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class Perpetrator extends AbstractRhineMonster {
    public static final String ID = "rhinemod:Perpetrator";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    int AttTimes;
    boolean isRush;

    public Perpetrator(float x, float y) {
        super(NAME, ID, 85, 0, 0, 210.0F, 160.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(92);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 5));
            AttTimes = 2;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 5));
            AttTimes = 2;
        } else {
            damage.add(new DamageInfo(this, 4));
            AttTimes = 2;
        }
        isRush = true;
        loadAnimation("resources/rhinemod/images/monsters/enemy_1330_cbrush/enemy_1330_cbrush37.atlas", "resources/rhinemod/images/monsters/enemy_1330_cbrush/enemy_1330_cbrush37.json", 2F);
        state.setAnimation(0, "Move_2", true);
        flipHorizontal = true;
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
            state.setAnimation(0, "Idle", true);
        }
        else if (stateName.equals("RUSH")) {
            isRush = true;
            state.setAnimation(0, "Move_2", true);
        }
    }

    @Override
    public void takeTurn() {
        state.setAnimation(0, "Attack", false);
        state.addAnimation(0, "Idle", true, 0F);
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
        } else {
            AttTimes ++;
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, damage.get(0).base, AttTimes, (AttTimes > 1));
    }
}
