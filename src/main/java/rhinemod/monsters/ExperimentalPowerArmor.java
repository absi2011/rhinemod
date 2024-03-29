package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import rhinemod.powers.HalfDamage;
import rhinemod.powers.NoStun;

public class ExperimentalPowerArmor extends CustomMonster {
    public static final String ID = "rhinemod:ExperimentalPowerArmor";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    public ExperimentalPowerArmor(float x, float y) {
        super(NAME, ID, 50, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(54);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 8));
        }
        else if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 7));
        }
        else {
            damage.add(new DamageInfo(this, 6));
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "resources/rhinemod/images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new HalfDamage(this)));
    }

    @Override
    public void takeTurn() {
        addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }
}
