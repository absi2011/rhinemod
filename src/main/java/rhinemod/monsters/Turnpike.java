package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import rhinemod.RhineMod;
import rhinemod.actions.DelayDieAction;
import rhinemod.actions.SummonLTEnemyAction;
import rhinemod.powers.DamageOutPower;
import rhinemod.powers.ExplodePower;

public class Turnpike extends AbstractRhineMonster {
    public static final String ID = "rhinemod:Turnpike";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private int Armor = 70;
    public AbstractMonster[] allies = new AbstractMonster[3];
    boolean justSummon = false;
    int summonMyself;
    public Turnpike(float x, float y,int summon) {
        super(NAME, ID, 150, 0, 0, 150.0F, 320.0F, null, x, y);
        if ((RhineMod.tagLevel >= 2) && (summon == 1))
        {
            summonMyself = 1;
            // TODO: 切换BGM
        }
        else
        {
            summonMyself = 0;
        }
        type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(160);
            Armor = 75;
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            damage.add(new DamageInfo(this, 28));
        }
        else if (AbstractDungeon.ascensionLevel >= 3) {
            damage.add(new DamageInfo(this, 24));
        }
        else {
            damage.add(new DamageInfo(this, 22));
        }

        loadAnimation("resources/rhinemod/images/monsters/enemy_1332_cbterm/enemy_1332_cbterm33.atlas", "resources/rhinemod/images/monsters/enemy_1332_cbterm/enemy_1332_cbterm33.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void addCCTags() {
        if (summonMyself == 1) {
            super.addCCTags();
        }
        else {
            if (RhineMod.tagLevel >= 3) {
                addTag(3);
            }
        }
    }

    public Turnpike(float x, float y) {
        this(x, y, 1);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new DamageOutPower(this, Armor, 1)));
        addToBot(new ApplyPowerAction(this,this, new ExplodePower(this, 30)));
        addToBot(new CannotLoseAction());
        CardCrawlGame.music.fadeOutTempBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly("m_bat_act19side_01_combine.mp3", true);
        if (RhineMod.tagLevel >= 3) {
            for (int i = LTAllyNum(); i < 3; i++) {
                addToBot(new SummonLTEnemyAction(allies, true, summonMyself == 0));
            }
        }
    }

    @Override
    public void takeTurn() {
        if (nextMove == 1) {
            justSummon = true;
            if ((AbstractDungeon.ascensionLevel >= 18) && (LTAllyNum() == 0)) {
                addToBot(new SummonLTEnemyAction(allies, true));
            }
            addToBot(new SummonLTEnemyAction(allies, true));
            addToBot(new SummonLTEnemyAction(allies, true));
        }
        else {
            justSummon = false;
            state.setAnimation(0, "Attack", false);
            state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        }
        getMove(-1);
    }

    int LTAllyNum()
    {
        int count = 0;
        for (AbstractMonster m : allies) {
            if ((m != null) && (!m.isDeadOrEscaped())) {
                count++;
            }
        }

        return count;
    }
    private int getSmartPosition(AbstractMonster m) {
        int position = 0;

        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!(m.drawX > mo.drawX)) {
                break;
            }
            ++position;
        }

        return position;
    }

    @Override
    public void die() {
        if (!halfDead) {
            int insert = 0;
            int i = 0;
            for (AbstractGameAction a : AbstractDungeon.actionManager.actions) {
                i ++;
                if (a.actionType == AbstractGameAction.ActionType.DAMAGE) {
                    insert = i;
                }
            }
            AbstractDungeon.actionManager.actions.add(insert, new DelayDieAction(this, 2.0F));
            halfDead = true;
        }
    }

    @Override
    public void dieAnimation() {
        state.setAnimation(0, "Die", false);
    }
    @Override
    public void realDie() {
        float offsetX = (drawX - Settings.WIDTH * 0.75F) / Settings.xScale;
        float offsetY = (drawY - AbstractDungeon.floorY) / Settings.yScale;
        AbstractMonster m;
        if (summonMyself == 1) {
            m = new Turnpike(offsetX, offsetY, 0);
            ((Turnpike)m).allies = allies;
        }
        else {
            m = new TrafficPolice(offsetX, offsetY);
        }
        m.init();
        m.applyPowers();
        AbstractDungeon.getCurrRoom().monsters.addMonster(getSmartPosition(m), m);
        if (ModHelper.isModEnabled("Lethality")) {
            this.addToBot(new ApplyPowerAction(m, m, new StrengthPower(m, 3), 3));
        }

        if (ModHelper.isModEnabled("Time Dilation")) {
            this.addToBot(new ApplyPowerAction(m, m, new SlowPower(m, 0)));
        }
        m.animX = 0.0F;
        m.showHealthBar();
        m.usePreBattleAction();
        m.rollMove();
        m.createIntent();
        halfDead = false;
        super.die();
    }

    @Override
    protected void getMove(int i) {
        if ((RhineMod.tagLevel >= 3) && (i != -1)) {
            setMove((byte)2, Intent.ATTACK, damage.get(0).base);
        }
        else if (!justSummon && LTAllyNum() + 2 <= 3) {
            setMove((byte)1, Intent.UNKNOWN);
        } else {
            setMove((byte)2, Intent.ATTACK, damage.get(0).base);
        }
    }
}
