package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import rhinemod.RhineMod;
import rhinemod.actions.AwakenAction;
import rhinemod.actions.SetTrapAction;
import rhinemod.patches.MusicPatch;
import rhinemod.powers.*;

import java.util.ArrayList;

public class Dorothy extends AbstractRhineMonster {
    public static final String ID = "rhinemod:Dorothy";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int lastMove = -1;

    public Dorothy(float x, float y) {
        super(NAME, ID, 200, 0, 0, 150.0F, 220.0F, null, x, y);
        type = EnemyType.ELITE;
        currentHealth = maxHealth / 2;
        if (AbstractDungeon.ascensionLevel >= 18) {
            damage.add(new DamageInfo(this, 8));
            damage.add(new DamageInfo(this, 30));
        }
        else if (AbstractDungeon.ascensionLevel >= 3) {
            damage.add(new DamageInfo(this, 7));
            damage.add(new DamageInfo(this, 25));
        }
        else {
            damage.add(new DamageInfo(this, 6));
            damage.add(new DamageInfo(this, 20));
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1257_lydrty/enemy_1257_lydrty33.atlas", "resources/rhinemod/images/monsters/enemy_1257_lydrty/enemy_1257_lydrty33.json", 1.5F);
        state.setAnimation(0, "F_Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new RegenPower(this, 10)));
        addToBot(new ApplyPowerAction(this, this, new BlockDamage(this, 1)));
        addToBot(new ApplyPowerAction(this, this, new DreamBreakPower(this, Math.max(AbstractDungeon.player.maxHealth / 2, 50))));
        addToBot(new ApplyPowerAction(this, this, new TrapPower(this)));
        if (AbstractDungeon.id.equals("TheBeyond")) {
            CardCrawlGame.music.playTempBgmInstantly(MusicPatch.DOROTHY_BGM_INTRO, false);
        }
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (nextMove == 1) {
            addToBot(new DamageAction(p, damage.get(0)));
            if (RhineMod.tagLevel >= 2) {
                addToBot(new ApplyPowerAction(p, this, new DreamingPower(p, damage.get(0).base)));
            }
            state.setAnimation(0, "F_Attack", false);
            state.addAnimation(0, "F_Idle", true, 0);
        } else {
            if (nextMove == 2) {
                ArrayList<AbstractCreature> targetPossible = new ArrayList<>();
                if (!p.hasPower(HealPower.POWER_ID)) {
                    targetPossible.add(p);
                }
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if ((!m.isDeadOrEscaped()) && (!m.hasPower(HealPower.POWER_ID)) && (m != this)) {
                        targetPossible.add(m);
                    }
                }
                if (targetPossible.isEmpty()) {
                    targetPossible.add(p);  // TODO: 要不要允许这种时候选自身（如果选的话对玩家威胁大幅降低）
                }
                AbstractCreature target = targetPossible.get(AbstractDungeon.monsterRng.random(0, targetPossible.size() - 1));
                addToBot(new ApplyPowerAction(target, this, new HealPower(target)));
            } else {
                addToBot(new AwakenAction(damage.get(1).base, this));
                addToBot(new RemoveSpecificPowerAction(this, this, Equality.POWER_ID));
            }
            state.setAnimation(0, "F_Skill", false);
            state.addAnimation(0, "F_Idle", true, 0);
        }
        addToBot(new SetTrapAction(RhineMod.tagLevel >= 3? 2 : 1)); // TODO: 可能做成power？
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        ArrayList<Integer> legalMoves = new ArrayList<>();
        for (int j = 1; j <= 3; j++) {
            if (lastMove != j) {
                legalMoves.add(j);
            }
        }
        legalMoves.add(2);
        int nextMove = legalMoves.get(AbstractDungeon.monsterRng.random(0, legalMoves.size()-1));
        lastMove = nextMove;
        if (nextMove == 1) {
            if (RhineMod.tagLevel >= 2) {
                setMove((byte)1, Intent.ATTACK_DEBUFF, damage.get(0).base);
            }
            else {
                setMove((byte)1, Intent.ATTACK, damage.get(0).base);
            }
        }
        else if (nextMove == 2) {
            setMove((byte)2, Intent.UNKNOWN);
        }
        else {
            addToBot(new ApplyPowerAction(this, this, new Equality(this)));
            setMove((byte)3, Intent.ATTACK, damage.get(1).base);
        }
    }

    @Override
    public void heal(int healAmount, boolean showEffect) {
        super.heal(healAmount);
        if (currentHealth == maxHealth) {
            hideHealthBar();
            escaped = true;
            isEscaping = true;
            AbstractDungeon.getCurrRoom().monsters.monsters.remove(this);
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.overlayMenu.endTurnButton.disable();
                for (AbstractCard c : AbstractDungeon.player.limbo.group) {
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                }
                AbstractDungeon.player.limbo.clear();
                //End Battle
            }
        }
    }
    @Override
    public void heal(int healAmount) {
        super.heal(healAmount);
        if (currentHealth == maxHealth) {
            hideHealthBar();
            escaped = true;
            isEscaping = true;
            AbstractDungeon.getCurrRoom().monsters.monsters.remove(this);
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.overlayMenu.endTurnButton.disable();
                for (AbstractCard c : AbstractDungeon.player.limbo.group) {
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                }
                AbstractDungeon.player.limbo.clear();
                //End Battle
            }
        }
    }
}
