package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import rhinemod.actions.AwakenAction;
import rhinemod.actions.SummonMechAction;
import rhinemod.powers.Equality;
import rhinemod.powers.Journey;

public class Awaken_Monster extends AbstractRhineMonster {
    public static final String ID = "rhinemod:Awaken_Monster";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public int stage1Journey;
    public int stage2Add;
    public boolean isStage2;
    private boolean notTriggered;

    public AbstractMonster[] mechs = new AbstractMonster[3];


    public Awaken_Monster(float x, float y) {
        super(NAME, ID, 600, 0, 0, 370.0F, 520.0F, null, x, y);
        type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(630);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            damage.add(new DamageInfo(this, 140));
            damage.add(new DamageInfo(this, 200));
            stage1Journey = 10;
            stage2Add = 6;
        }
        else if (AbstractDungeon.ascensionLevel >= 3) {
            damage.add(new DamageInfo(this, 140));
            damage.add(new DamageInfo(this, 200));
            stage1Journey = 8;
            stage2Add = 4;
        }
        else {
            damage.add(new DamageInfo(this, 120));
            damage.add(new DamageInfo(this, 176));
            stage1Journey = 8;
            stage2Add = 4;
        }
        isStage2 = false;
        notTriggered = true;

        loadAnimation("resources/rhinemod/images/monsters/enemy_1531_bbrain/enemy_1531_bbrain33.atlas", "resources/rhinemod/images/monsters/enemy_1531_bbrain/enemy_1531_bbrain33.json", 1.5F);
        state.addAnimation(0, "Idle", true, 0.0F);
        // flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new Equality(this)));
        addToBot(new ApplyPowerAction(this, this, new Journey(this, stage1Journey)));
        CardCrawlGame.music.fadeOutTempBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly("m_bat_bbrain_combine.mp3", true);
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
    }

    private void shatteredVision(int stage) {
        state.setAnimation(0, "Skill_2_Start", false);
        state.setAnimation(0, "Skill_2_Loop", false);
        state.setAnimation(0, "Skill_2_End", false);
        state.addAnimation(0, "Idle", true, 0);
        CardCrawlGame.sound.play("SHATTERED_VISION");
        addToBot(new WaitAction(0.3F));
        addToBot(new AwakenAction(damage.get(stage).base, this));
    }

    @Override
    public void takeTurn() {
        if (nextMove <= 2) {
            shatteredVision(nextMove - 1);
        } else if (nextMove == 3) {
            state.setAnimation(0, "Skill_1", false);
            state.addAnimation(0, "Idle", true, 0);
            addToBot(new SummonMechAction(mechs));
        }
        if ((currentHealth <= maxHealth / 2) && (notTriggered)) {
            addToBot(new ApplyPowerAction(this, this, new Journey(this, stage2Add)));
            isStage2 = true;
            notTriggered = false;
        }
        getMove(0);
    }

    private int MechNum() {
        int count = 0;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != null && m != this && !m.isDying && !m.isDeadOrEscaped()) {
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
        if (Mechs != 3) {
            setMove((byte)3, Intent.UNKNOWN);
        } else if (isStage2) {
            setMove(MOVES[0], (byte)2, Intent.ATTACK, damage.get(1).base);
        } else {
            setMove(MOVES[0], (byte)1, Intent.ATTACK, damage.get(0).base);
        }
    }
}
