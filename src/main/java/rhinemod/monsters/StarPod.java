package rhinemod.monsters;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import rhinemod.powers.*;

import java.util.logging.Logger;

public class StarPod extends AbstractRhineMonster {
    public static final String ID = "rhinemod:StarPod";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String STAR_POD_BGM_INTRO = "m_bat_cstlrs_intro.mp3";
    public static final String STAR_POD_BGM_LOOP = "m_bat_cstlrs_loop.mp3";

    int currentTurn = 0;
    int lastOp = -1;
    int AttackNum;
    int AttackPlusNum;
    int DefendNum;
    int DefendPlusNum;
    int RepairNum;

    public StarPod(float x, float y) {
        super(NAME, ID, 9999, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 19) {
            AttackNum = 20;
            AttackPlusNum = 7;
            DefendNum = 6;
            DefendPlusNum = 3;
            RepairNum = 10;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            AttackNum = 14;
            AttackPlusNum = 4;
            DefendNum = 5;
            DefendPlusNum = 2;
            RepairNum = 7;
        } else {
            AttackNum = 9;
            AttackPlusNum = 3;
            DefendNum = 3;
            DefendPlusNum = 1;
            RepairNum = 5;
        }

        loadAnimation("resources/rhinemod/images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "resources/rhinemod/images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new CannotLoseAction());
        addToBot(new ApplyPowerAction(this, this, new Friable(this)));
        addToBot(new ApplyPowerAction(this, this, new NoStun(this)));
        addToBot(new ApplyPowerAction(this, this, new DamageOutPower(this, 100, 1)));
        if (AbstractDungeon.ascensionLevel >= 19) {
            addToBot(new ApplyPowerAction(this, this, new AutoDefend(this, DefendNum)));
            addToBot(new GainBlockAction(this, this, DefendNum));
        }
        CardCrawlGame.music.fadeOutTempBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly(STAR_POD_BGM_INTRO, false);
    }


    @Override
    public void takeTurn() {
        currentTurn++;
        if (currentTurn % 3 == 1) {
            if (currentTurn == 1) {
                addToBot(new ApplyPowerAction(this, this, new AutoAttack(this, 1)));
                addToBot(new ApplyPowerAction(this, this, new AutoDefend(this, 1)));
            } else {
                addToBot(new ApplyPowerAction(this, this, new VoidPower(this, 1)));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    cleanDebuff();
                }
            }
            if (currentTurn >= 10) {
                addToBot(new ApplyPowerAction(this, this, new DamageOutPower(this, RepairNum, 1)));
            }
            lastOp = -1;
        } else {
            int thisMove;
            if (lastOp == -1) {
                thisMove = AbstractDungeon.aiRng.random(0, 1);
                lastOp = 1 - thisMove;
            } else {
                thisMove = lastOp;
            }
            if (thisMove == 0) {
                int BuffNum = AttackNum;
                if (currentTurn >= 10) {
                    BuffNum = currentTurn * 2;
                } else if (currentTurn >= 7) {
                    BuffNum += AttackPlusNum;
                }
                addToBot(new ApplyPowerAction(this, this, new AutoAttack(this, BuffNum)));
            } else {
                int BuffNum = DefendNum;
                if (currentTurn >= 10) {
                    BuffNum = currentTurn;
                } else if (currentTurn >= 7) {
                    BuffNum += DefendPlusNum;
                }
                addToBot(new ApplyPowerAction(this, this, new AutoDefend(this, BuffNum)));
            }
        }
        getMove(0);
    }

    private void cleanDebuff() {
        for (AbstractPower pow : powers)
            if (pow != null && pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof Friable)) {
                addToBot(new RemoveSpecificPowerAction(this, this, pow));
            }
    }

    @Override
    protected void getMove(int i) {
        if ((currentTurn % 3 == 0) && (currentTurn >= 9)) {
            setMove(MOVES[0], (byte) 1, Intent.BUFF);
        } else {
            setMove((byte) 1, Intent.BUFF);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        currentHealth = maxHealth;
    }

    @Override
    public void die() {
        if (AbstractDungeon.getCurrRoom().cannotLose) {
            currentHealth = maxHealth;
        }
        else {
            AbstractDungeon.getCurrRoom().rewardAllowed = false;
            super.die();
            onBossVictoryLogic();
            onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        }
    }

    @Override
    public void healthBarUpdatedEvent() {
        super.healthBarUpdatedEvent();
    }

    @Override
    protected void updateHealthBar() {
        super.updateHealthBar();
    }

    @Override
    public void renderHealth(SpriteBatch sb) {
        super.renderHealth(sb);
        // TODO: 在这里改，但是我真的不会，毁灭吧(需要保留格挡数与状态栏，其他的全删了）
    }

    @SpirePatch(clz = TempMusic.class, method = "<ctor>", paramtypez = {String.class, boolean.class, boolean.class})
    public static class UpdateTempBGMPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = "music")
        public static void Insert(TempMusic _inst, Music music) {
            if (_inst.key.equals(STAR_POD_BGM_INTRO)) {
                music.setOnCompletionListener(music1 -> {
                    Logger.getLogger(StarPod.class.getName()).info("StarPod BGM intro done!");
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                            if (m.id.equals(StarPod.ID)) {
                                CardCrawlGame.music.playTempBgmInstantly(STAR_POD_BGM_LOOP, true);
                                break;
                            }
                    }
                });
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(Music.class, "play");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }
}
