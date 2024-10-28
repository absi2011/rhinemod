package rhinemod.monsters;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import javassist.CtBehavior;
import rhinemod.powers.*;

import java.lang.reflect.Field;
import java.util.logging.Logger;

public class StarPod extends AbstractRhineMonster {
    public static final String ID = "rhinemod:StarPod";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String STAR_POD_BGM_INTRO = "m_bat_cstlrs_intro.mp3";
    public static final String STAR_POD_BGM_LOOP = "m_bat_cstlrs_loop.mp3";
    public static final Texture BG_IMG = new Texture("resources/rhinemod/images/monsters/starpod.png");
    public static final Texture DAMAGEOUT_IMG = new Texture("resources/rhinemod/images/ui/damageout.png");
    int currentTurn = 0;
    int lastOp = -1;
    int AttackNum;
    int AttackPlusNum;
    int DefendNum;
    int DefendPlusNum;
    int RepairNum;
    public int damageOutAmt = 100;

    public StarPod(float x, float y) {
        super(NAME, ID, 99999, 0, 0, 360.0F, 500.0F, null, x, y);
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
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new CannotLoseAction());
        addToBot(new ApplyPowerAction(this, this, new Brittleness(this)));
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
            if (pow != null && pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof Brittleness)) {
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
        } else {
            AbstractDungeon.getCurrRoom().rewardAllowed = false;
            super.die();
            onBossVictoryLogic();
            onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!isDead && !AbstractDungeon.player.isDead) {
            hb.render(sb);
            renderHealth(sb);
            renderName(sb);
        }
    }

    Object getObjectFromSuperClass(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = AbstractCreature.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(this);
    }

    @Override
    public void renderHealth(SpriteBatch sb) {
        if (Settings.hideCombatElements) return;
        float x = hb.cX - hb.width / 2.0F + 40.0F * Settings.xScale;
        float y = hb.cY - hb.height / 2.0F;
        try {
            Color hbShadowColor = (Color) getObjectFromSuperClass("hbShadowColor");
            sb.setColor(hbShadowColor);
            sb.draw(ImageMaster.HB_SHADOW_R, x, y + hb.height, 0, 0, DAMAGEOUT_BAR_BREADTH, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.HB_SHADOW_R.getWidth(), ImageMaster.HB_SHADOW_R.getHeight(), false, false);
            sb.draw(ImageMaster.HB_SHADOW_B, x, y, 0, 0, hb.height, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.HB_SHADOW_B.getWidth(), ImageMaster.HB_SHADOW_B.getHeight(), false, false);
            sb.draw(ImageMaster.HB_SHADOW_L, x, y - DAMAGEOUT_BAR_BREADTH, 0, 0, DAMAGEOUT_BAR_BREADTH, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.HB_SHADOW_L.getWidth(), ImageMaster.HB_SHADOW_L.getHeight(), false, false);
            Color orangeHbBarColor = new Color(0.8F, 0.05F, 0.05F, hbAlpha);
            sb.setColor(orangeHbBarColor);
            sb.draw(ImageMaster.HEALTH_BAR_R, x, y + hb.height, 0, 0, DAMAGEOUT_BAR_BREADTH, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.HEALTH_BAR_R.getWidth(), ImageMaster.HEALTH_BAR_R.getHeight(), false, false);
            sb.draw(ImageMaster.HEALTH_BAR_B, x, y, 0, 0, hb.height, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.HEALTH_BAR_B.getWidth(), ImageMaster.HEALTH_BAR_B.getHeight(), false, false);
            sb.draw(ImageMaster.HEALTH_BAR_L, x, y - DAMAGEOUT_BAR_BREADTH, 0, 0, DAMAGEOUT_BAR_BREADTH, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.HEALTH_BAR_L.getWidth(), ImageMaster.HEALTH_BAR_L.getHeight(), false, false);
            Color blockColor = (Color) getObjectFromSuperClass("blockColor");

            damageOutAmt = 0;
            if (hasPower(DamageOutPower.POWER_ID)) damageOutAmt = getPower(DamageOutPower.POWER_ID).amount;
            float damageOutBarLength = hb.height * damageOutAmt / 100;
            if (damageOutAmt != 0) {
                Color blueHbBarColor = (Color) getObjectFromSuperClass("blueHbBarColor");
                sb.setColor(blueHbBarColor);
                sb.draw(ImageMaster.HEALTH_BAR_R, x, y + damageOutBarLength, 0, 0, DAMAGEOUT_BAR_BREADTH, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.HEALTH_BAR_R.getWidth(), ImageMaster.HEALTH_BAR_R.getHeight(), false, false);
                sb.draw(ImageMaster.HEALTH_BAR_B, x, y, 0, 0, damageOutBarLength, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.HEALTH_BAR_B.getWidth(), ImageMaster.HEALTH_BAR_B.getHeight(), false, false);
                sb.draw(ImageMaster.HEALTH_BAR_L, x, y - DAMAGEOUT_BAR_BREADTH, 0, 0, DAMAGEOUT_BAR_BREADTH, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.HEALTH_BAR_L.getWidth(), ImageMaster.HEALTH_BAR_L.getHeight(), false, false);
                Color blockOutlineColor = (Color) getObjectFromSuperClass("blockOutlineColor");
                sb.setColor(blockOutlineColor);
                sb.setBlendFunction(770, 1);
                sb.draw(ImageMaster.BLOCK_BAR_R, x, y + damageOutBarLength, 0, 0, DAMAGEOUT_BAR_BREADTH, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.BLOCK_BAR_R.getWidth(), ImageMaster.BLOCK_BAR_R.getHeight(), false, false);
                sb.draw(ImageMaster.BLOCK_BAR_B, x, y, 0, 0, damageOutBarLength, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.BLOCK_BAR_B.getWidth(), ImageMaster.BLOCK_BAR_B.getHeight(), false, false);
                sb.draw(ImageMaster.BLOCK_BAR_L, x, y - DAMAGEOUT_BAR_BREADTH, 0, 0, DAMAGEOUT_BAR_BREADTH, DAMAGEOUT_BAR_BREADTH, 1, 1, 90, 0, 0, ImageMaster.BLOCK_BAR_L.getWidth(), ImageMaster.BLOCK_BAR_L.getHeight(), false, false);
                sb.setBlendFunction(770, 771);

                Color damageOutColor = blockColor.cpy();
                damageOutColor.a = hbAlpha;
                sb.setColor(damageOutColor);
                sb.draw(DAMAGEOUT_IMG, x + BLOCK_ICON_X - 54.0F * Settings.scale, y + BLOCK_ICON_Y - 48.0F * Settings.scale + hb.height + DAMAGEOUT_BAR_BREADTH, 0, 0, 96 * Settings.scale, 96 * Settings.scale, 1, 1, 0, 0, 0, 64, 64, false, false);
                FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, Integer.toString(damageOutAmt), x + BLOCK_ICON_X - 4.0F * Settings.scale, y - 16.0F * Settings.scale + hb.height + DAMAGEOUT_BAR_BREADTH, Color.WHITE, 1.5F);
            }

            if (currentBlock > 0) {
                float blockOffset = (float) getObjectFromSuperClass("blockOffset");
                float blockScale = (float) getObjectFromSuperClass("blockScale");
                Color blockTextColor = (Color) getObjectFromSuperClass("blockTextColor");
                sb.setColor(blockColor);
                sb.draw(ImageMaster.BLOCK_ICON, x + BLOCK_ICON_X - 54.0F * Settings.scale, y + BLOCK_ICON_Y - 48.0F * Settings.scale + blockOffset, 0, 0, 96 * Settings.scale, 96 * Settings.scale, 1, 1, 0, 0, 0, 64, 64, false, false);
                FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, Integer.toString(currentBlock), x + BLOCK_ICON_X - 4.0F * Settings.scale, y - 16.0F * Settings.scale, blockTextColor, blockScale * 1.5F);
            }

            Field hbTextColorField = AbstractCreature.class.getDeclaredField("hbTextColor");
            hbTextColorField.setAccessible(true);
            Color hbTextColor = (Color) hbTextColorField.get(this);
            float offset = 10.0F * Settings.yScale;
            x += 32.0F * Settings.xScale;
            for (AbstractPower p : powers) {
                if (p instanceof DamageOutPower) continue;
                p.renderIcons(sb, x, y + offset, hbTextColor);
                offset += POWER_ICON_PADDING;
            }
            x += 32.0F * Settings.xScale;
            offset = -12.0F * Settings.yScale;
            for (AbstractPower p : powers) {
                if (p instanceof DamageOutPower) continue;
                p.renderAmount(sb, x, y + offset, hbTextColor);
                offset += POWER_ICON_PADDING;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "brokeBlock")
    public static class BrokeBlockPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(AbstractCreature _inst) {
            if (_inst instanceof StarPod) {
                AbstractDungeon.effectList.add(new HbBlockBrokenEffect(_inst.hb.cX - _inst.hb.width / 2.0F + 24.0F * Settings.xScale, _inst.hb.cY - _inst.hb.height / 2.0F + BLOCK_ICON_Y));
                CardCrawlGame.sound.play("BLOCK_BREAK");
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "loseBlock", paramtypez = {int.class, boolean.class})
    public static class LoseBlockPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(AbstractCreature _inst) {
            if (_inst instanceof StarPod) {
                AbstractDungeon.effectList.add(new HbBlockBrokenEffect(_inst.hb.cX - _inst.hb.width / 2.0F + 24.0F * Settings.xScale, _inst.hb.cY - _inst.hb.height / 2.0F + BLOCK_ICON_Y));
                CardCrawlGame.sound.play("BLOCK_BREAK");
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "effectList");
            return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
        }
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

    @SpirePatch(clz = AbstractRoom.class, method = "render")
    public static class RenderBackgroundPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractRoom _inst, SpriteBatch sb) {
            boolean hasStarPod = false;
            if (_inst.monsters == null) return;
            for (AbstractMonster m : _inst.monsters.monsters)
                if (m instanceof StarPod) {
                    hasStarPod = true;
                    break;
                }
            if (hasStarPod) {
                sb.setColor(Color.WHITE);
                float scale = Math.max(Settings.WIDTH * 1.0F / BG_IMG.getWidth(), Settings.HEIGHT * 1.0F / BG_IMG.getHeight());
                float w = BG_IMG.getWidth() * scale;
                float h = BG_IMG.getHeight() * scale;
                sb.draw(BG_IMG, (Settings.WIDTH - w) / 2.0F, (Settings.HEIGHT - h) / 2.0F, w, h);
            }
        }
    }

    private static final float DAMAGEOUT_BAR_BREADTH = 40.0F * Settings.scale;
    private static final float POWER_ICON_PADDING = 48.0F * Settings.scale;
}
