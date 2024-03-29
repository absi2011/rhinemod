package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import rhinemod.actions.AwakenAction;
import rhinemod.actions.SummonMechAction;
import rhinemod.powers.Average;
import rhinemod.powers.Journey;

import java.util.Iterator;

public class Awaken_Monster extends CustomMonster {
    public static final String ID = "rhinemod:Awaken_Monster";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public int stage1Journey;
    public int stage2Add;
    public boolean isStage2 = false;
    private boolean notTriggered = true;

    public AbstractMonster[] mechs = new AbstractMonster[3];


    public Awaken_Monster(float x, float y) {
        super(NAME, ID, 600, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(630);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            damage.add(new DamageInfo(this, 80));
            damage.add(new DamageInfo(this, 140));
            stage1Journey = 10;
            stage2Add = 6;
        }
        else if (AbstractDungeon.ascensionLevel >= 3) {
            damage.add(new DamageInfo(this, 80));
            damage.add(new DamageInfo(this, 140));
            stage1Journey = 8;
            stage2Add = 4;
        }
        else {
            damage.add(new DamageInfo(this, 68));
            damage.add(new DamageInfo(this, 120));
            stage1Journey = 8;
            stage2Add = 4;
        }
        isStage2 = false;
        notTriggered = true;

        loadAnimation("images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new Average(this)));
        addToBot(new ApplyPowerAction(this, this, new Journey(this, stage1Journey)));
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
    }

    public void changeState(String stateName) {
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (nextMove == 1) {
            addToBot(new AwakenAction(damage.get(0).base, this));
            // TODO: 一阶段技能
        }
        else if (nextMove == 2) {
            addToBot(new AwakenAction(damage.get(1).base, this));
            // TODO: 二阶段技能
        }
        else if (nextMove == 3) {
            // TODO: 召唤
            addToBot(new SummonMechAction(mechs));
        }
        if ((currentHealth <= maxHealth / 2) && (notTriggered))
        {
            addToBot(new ApplyPowerAction(this, this, new Journey(this, stage2Add)));
            isStage2 = true;
        }
        getMove(0);
    }

    int MechNum()
    {
        int count = 0;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != null && m != this && !m.isDying) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public void die() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
            if (!m.isDeadOrEscaped()) {
                addToTop(new HideHealthBarAction(m));
                addToTop(new SuicideAction(m));
                addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
            }
        super.die();
    }

    @Override
    protected void getMove(int i) {
        int Mechs = MechNum();
        if (nextMove == 3) {
            Mechs ++;
        }
        if (Mechs != 3)
        {
            setMove((byte)3, Intent.UNKNOWN);
        }
        else if (isStage2)
        {
            setMove(MOVES[0], (byte)2, Intent.ATTACK, damage.get(1).base);
        }
        else
        {
            setMove(MOVES[0], (byte)1, Intent.ATTACK, damage.get(0).base);
        }
    }
}
