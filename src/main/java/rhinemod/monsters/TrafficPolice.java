package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import rhinemod.actions.SummonLTEnemyAction;
import rhinemod.powers.HiddenPower;

public class TrafficPolice extends CustomMonster {
    public static final String ID = "rhinemod:TrafficPolice";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final int maxDebuff = 6;
    private int turn;
    public AbstractMonster[] allies = new AbstractMonster[3];
    boolean isHidden = false;
    public TrafficPolice(float x, float y) {
        super(NAME, ID, 56, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(60);
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
        turn = 0;

        loadAnimation("images/monsters/enemy_1333_cbbgen/enemy_1333_cbbgen33.atlas", "images/monsters/enemy_1333_cbbgen/enemy_1333_cbbgen33.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new CannotLoseAction());
        addToBot(new ApplyPowerAction(this, this, new HiddenPower(this, 2)));
        addToBot(new ChangeStateAction(this, "HIDDEN"));
        // 隐匿特效
    }

    public void changeState(String stateName) {
        if (stateName.equals("HIDDEN")) {
            isHidden = true;
            halfDead = true;
            currentHealth = 0;
        }
        else if (stateName.equals("NORMAL")) {
            isHidden = false;
            addToBot(new CanLoseAction());
            currentHealth = maxHealth;
            halfDead = false;
        }
    }

    @Override
    public void takeTurn() {
        turn ++;
        DamageInfo t = new DamageInfo(this, damage.get(0).output);
        // TODO: 没想到一个好的办法，目前先做成打荆棘伤害吧
        if (turn <= 2) {
            t.type = DamageInfo.DamageType.THORNS;
        }
        state.setAnimation(0, "Attack", false);
        state.addAnimation(0, "Idle", true, 0);
        addToBot(new DamageAction(AbstractDungeon.player, t));
        int debuff = AbstractDungeon.aiRng.random(1, maxDebuff);
        int anotherDebuff = AbstractDungeon.aiRng.random(1, maxDebuff-1);
        if (anotherDebuff >= debuff) {
            anotherDebuff++;
        }
        applyDebuff(debuff);
        if (AbstractDungeon.ascensionLevel >= 18) {
            applyDebuff(anotherDebuff);
        }
        if (haveSlot(allies)) {
            addToBot(new SummonLTEnemyAction(allies, false));
        }
        if (turn == 2) {
            addToBot(new ChangeStateAction(this, "NORMAL"));
        }
        getMove(0);
    }
    private boolean haveSlot(AbstractMonster[] allies) {
        for (AbstractMonster ally : allies) {
            if (ally == null || ally.isDying) {
                return true;
            }
        }

        return false;
    }

    void applyDebuff(int id) {
        AbstractPlayer p = AbstractDungeon.player;
        if (id == 1) {
            addToBot(new ApplyPowerAction(p, null, new WeakPower(p, 1, true)));
        }
        else if (id == 2) {
            addToBot(new ApplyPowerAction(p, null, new VulnerablePower(p, 1, true)));
        }
        else if (id == 3) {
            addToBot(new ApplyPowerAction(p, null, new FrailPower(p, 1, true)));
        }
        else if (id == 4) {
            addToBot(new ApplyPowerAction(p, null, new StrengthPower(p, -1)));
        }
        else if (id == 5) {
            addToBot(new ApplyPowerAction(p, null, new DexterityPower(p, -1)));
        }
        else {
            addToBot(new ApplyPowerAction(p, null, new FocusPower(p, -1)));
        }
    }

    @Override
    public void renderHealth(SpriteBatch sb) {
        if (halfDead) currentHealth = maxHealth;
        super.renderHealth(sb);
        if (halfDead) currentHealth = 0;
    }

    @Override
    public void healthBarUpdatedEvent() {
        if (halfDead) currentHealth = maxHealth;
        super.healthBarUpdatedEvent();
        if (halfDead) currentHealth = 0;
    }

    @Override
    public void healthBarRevivedEvent() {
        if (halfDead) currentHealth = maxHealth;
        super.healthBarRevivedEvent();
        if (halfDead) currentHealth = 0;
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK_DEBUFF, damage.get(0).base);
    }
}
