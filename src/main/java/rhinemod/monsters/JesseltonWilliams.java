package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import rhinemod.powers.DoubleSmash;
import rhinemod.powers.DoubleNonSmash;

public class JesseltonWilliams extends CustomMonster {
    public static final String ID = "rhinemod:JesseltonWilliams";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    public final int DazedNum;
    public boolean isStage2 = false;
    private boolean notTriggered = true;

    public final int metalNum;

    public JesseltonWilliams(float x, float y) {
        super(NAME, ID, 300, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(320);
            metalNum = 8;
        }
        else {
            metalNum = 6;
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 6));
            damage.add(new DamageInfo(this, 16));
            DazedNum = 3;
        }
        else if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 6));
            damage.add(new DamageInfo(this, 14));
            DazedNum = 2;
        }
        else {
            damage.add(new DamageInfo(this, 5));
            damage.add(new DamageInfo(this, 13));
            DazedNum = 2;
        }
        isStage2 = false;
        notTriggered = true;
        loadAnimation("resources/rhinemod/images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "resources/rhinemod/images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new DoubleSmash(this)));
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if ((currentHealth <= maxHealth / 2) && notTriggered) {
            setMove(MOVES[2], (byte)3, Intent.UNKNOWN);
            createIntent();
            notTriggered = false;
        }
    }

    public void changeState(String stateName) {
        if (stateName.equals("KILLER")) {
            // TODO: 复活动画
            addToBot(new HealAction(this, this, maxHealth / 2 - currentHealth));
            addToBot(new RemoveSpecificPowerAction(this, this, DoubleSmash.POWER_ID));
            addToBot(new ApplyPowerAction(this, this, new DoubleNonSmash(this)));
            addToBot(new ApplyPowerAction(this, this, new MetallicizePower(this, metalNum)));
        }
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (nextMove == 1) {
            addToBot(new DamageAction(p, damage.get(0)));
            addToBot(new MakeTempCardInDiscardAction(new Dazed(), DazedNum));
            // TODO: 一阶段技能
        }
        else if (nextMove == 2) {
            addToBot(new DamageAction(p, damage.get(1)));
            addToBot(new DamageAction(p, damage.get(1)));
            // TODO: 二阶段技能
        }
        else {
            isStage2 = true;
            addToBot(new ChangeStateAction(this, "KILLER"));
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        if (isStage2)
        {
            setMove(MOVES[1], (byte)2, Intent.ATTACK, damage.get(1).base, 2, true);
        }
        else
        {
            setMove(MOVES[0], (byte)1, Intent.ATTACK_DEBUFF, damage.get(0).base);
        }
    }
}
