package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ShiftingPower;
import jdk.tools.jlink.internal.plugins.AbstractPlugin;
import rhinemod.powers.DamageOutPower;

public class ArclightMirrorguard extends CustomMonster {
    public static final String ID = "rhinemod:ArclightMirrorguard";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String NAME = monsterStrings.NAME;
    boolean isFlying;

    public ArclightMirrorguard(float x, float y) {
        super(NAME, ID, 70, 0, 0, 150.0F, 320.0F, null, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(76);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.damage.add(new DamageInfo(this, 18));
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.damage.add(new DamageInfo(this, 20));
        }
        else {
            this.damage.add(new DamageInfo(this, 18));
        }
        loadAnimation("images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        isFlying = false;
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel < 18) {
            addToBot(new ApplyPowerAction(this, this, new ShiftingPower(this)));
        }
        addToBot(new ApplyPowerAction(this, this, new DamageOutPower(this, 60, 3)));
    }

    public void changeState(String stateName) {
    }

    @Override
    public void takeTurn() {
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
    }
}
