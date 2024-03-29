package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FadingPower;
import com.megacrit.cardcrawl.powers.ShiftingPower;
import rhinemod.powers.DamageOutPower;

public class BlackHole extends CustomMonster {
    public static final String ID = "rhinemod:BlackHole";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;

    public BlackHole(float x, float y) {
        super(NAME, ID, 1, 0, 0, 200.0F, 280.0F, null, x, y);
        type = EnemyType.NORMAL;
        loadAnimation("resources/rhinemod/images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "resources/rhinemod/images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new DamageOutPower(this, 100, 0)));
        // addToBot(new ApplyPowerAction(this, this, new FadingPower(this, 1)));
    }

    @Override
    public void takeTurn() {
        this.state.setAnimation(0, "Move_Begin", false);
        this.hideHealthBar();
        addToBot(new WaitAction(3.0F));
        addToBot(new EscapeAction(this));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)99, Intent.UNKNOWN);
    }
}
