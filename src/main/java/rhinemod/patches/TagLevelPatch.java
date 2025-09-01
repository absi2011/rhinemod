package rhinemod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import javassist.CtBehavior;
import rhinemod.RhineMod;
import rhinemod.actions.AddMaxHpAction;
import rhinemod.monsters.AbstractRhineMonster;
import rhinemod.powers.AddStunTag;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public class TagLevelPatch {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("rhinemod:TagLevel").TEXT;
    public static final float TAG_LEVEL_LABEL_W = FontHelper.getSmartWidth(FontHelper.cardTitleFont, TEXT[0], 9999.0F, 0.0F);
    public static final float TAG_LEVEL_W = FontHelper.getSmartWidth(FontHelper.cardTitleFont, TEXT[1] + "1", 9999.0F, 0.0F);
    public static final float LABEL_POS = Settings.WIDTH / 2.0F + 550.0F * Settings.scale;
    public static final float LEVEL_POS = Settings.WIDTH / 2.0F + 650.0F * Settings.scale;
    public static Hitbox tagLevelLabelHb;
    public static Hitbox tagLevelLeftHb;
    public static Hitbox tagLevelRightHb;
    public static int curTagLevel;

    @SpirePatch(clz = CharacterSelectScreen.class, method = "initialize")
    public static class RenderInitPatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen _inst) {
            tagLevelLabelHb = new Hitbox(TAG_LEVEL_LABEL_W, 70.0F * Settings.scale);
            tagLevelLeftHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
            tagLevelRightHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);

            if (!Settings.isMobile) {
                tagLevelLabelHb.move(LABEL_POS - TAG_LEVEL_LABEL_W / 2.0F, 70.0F * Settings.scale);
                tagLevelLeftHb.move(LEVEL_POS - TAG_LEVEL_W * 0.25F, 70.0F * Settings.scale);
                tagLevelRightHb.move(LEVEL_POS + TAG_LEVEL_W * 1.25F, 70.0F * Settings.scale);
            } else {
                tagLevelLabelHb.move(LABEL_POS - TAG_LEVEL_LABEL_W / 2.0F, 100.0F * Settings.scale);
                tagLevelLeftHb.move(LEVEL_POS - TAG_LEVEL_W * 0.25F, 100.0F * Settings.scale);
                tagLevelRightHb.move(LEVEL_POS + TAG_LEVEL_W * 1.25F, 100.0F * Settings.scale);
            }
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "renderAscensionMode")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen _inst, SpriteBatch sb) {
            try {
                Field field = CharacterSelectScreen.class.getDeclaredField("anySelected");
                field.setAccessible(true);
                if (field.getBoolean(_inst)) {
                    if (_inst.isAscensionMode && _inst.ascensionLevel == 20) {
                        sb.setColor(Color.WHITE);
                        if (tagLevelLabelHb.hovered) {
                            FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[0], LABEL_POS - TAG_LEVEL_LABEL_W / 2.0F, 70.0F * Settings.scale, Settings.GREEN_TEXT_COLOR);
                            TipHelper.renderGenericTip(InputHelper.mX - 140.0F * Settings.scale, InputHelper.mY + 340.0F * Settings.scale, TEXT[0], TEXT[2]);
                        }
                        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[0], LABEL_POS - TAG_LEVEL_LABEL_W / 2.0F, 70.0F * Settings.scale, Settings.GOLD_COLOR);
                        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[1] + curTagLevel, LEVEL_POS + TAG_LEVEL_W / 2.0F, 70.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);

                        if (tagLevelLeftHb.hovered) {
                            sb.setColor(Color.WHITE);
                        } else {
                            sb.setColor(Color.LIGHT_GRAY);
                        }
                        sb.draw(ImageMaster.CF_LEFT_ARROW, tagLevelLeftHb.cX - 24.0F, tagLevelLeftHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

                        if (tagLevelRightHb.hovered) {
                            sb.setColor(Color.WHITE);
                        } else {
                            sb.setColor(Color.LIGHT_GRAY);
                        }
                        sb.draw(ImageMaster.CF_RIGHT_ARROW, tagLevelRightHb.cX - 24.0F, tagLevelRightHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "updateAscensionToggle")
    public static class SelectUpdatePatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen _inst) {
            if (_inst.isAscensionMode && _inst.ascensionLevel == 20) {
                tagLevelLabelHb.update();
                tagLevelLeftHb.update();
                tagLevelRightHb.update();
                if (InputHelper.justClickedLeft) {
                    if (tagLevelLabelHb.hovered) {
                        tagLevelLabelHb.clickStarted = true;
                    } else if (tagLevelLeftHb.hovered) {
                        tagLevelLeftHb.clickStarted = true;
                    } else if (tagLevelRightHb.hovered) {
                        tagLevelRightHb.clickStarted = true;
                    }
                }

                if (tagLevelLeftHb.clicked) {
                    tagLevelLeftHb.clicked = false;
                    if (curTagLevel != 0) {
                        curTagLevel--;
                        for (CharacterOption o : _inst.options) {
                            if (o.selected) {
                                Prefs pref = o.c.getPrefs();
                                pref.putInteger("LAST_TAG_LEVEL", curTagLevel);
                                pref.flush();
                            }
                        }
                    }
                }

                if (tagLevelRightHb.clicked) {
                    tagLevelRightHb.clicked = false;
                    if (curTagLevel != 3) {
                        curTagLevel++;
                        for (CharacterOption o : _inst.options) {
                            if (o.selected) {
                                Prefs pref = o.c.getPrefs();
                                pref.putInteger("LAST_TAG_LEVEL", curTagLevel);
                                pref.flush();
                            }
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(clz = CharacterOption.class, method = "updateHitbox")
    public static class OptionSetTagLevelPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"pref"})
        public static void Insert(CharacterOption _inst, Prefs pref) {
            curTagLevel = pref.getInteger("LAST_TAG_LEVEL", 0);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(CharacterSelectScreen.class, "ascensionLevel");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "updateButtons")
    public static class ConfirmSelectPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CharacterSelectScreen _inst) {
            if (_inst.isAscensionMode && _inst.ascensionLevel == 20) {
                RhineMod.tagLevel = curTagLevel;
            } else {
                RhineMod.tagLevel = 0;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "isAscensionMode");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    public static void applyTags(AbstractMonster m) {
        if (RhineMod.tagLevel > 0) {
            AbstractDungeon.actionManager.addToBottom(new AddMaxHpAction(m));
            if (!(m instanceof AbstractRhineMonster)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, RhineMod.tagLevel)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new AddStunTag(m, 5 - RhineMod.tagLevel)));
            }
        }
    }

    @SpirePatch(clz = MonsterGroup.class, method = "usePreBattleAction")
    public static class AbstractMonsterAtkTagPatch {
        @SpirePostfixPatch
        public static void Postfix(MonsterGroup _inst) {
            if (!AbstractDungeon.loading_post_combat) {
                for (AbstractMonster m : _inst.monsters)
                    applyTags(m);
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "init")
    public static class AbstractMonsterAtkTagPatch2 {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster _inst) {
            applyTags(_inst);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "renderTip")
    public static class RenderTipPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractMonster _inst, SpriteBatch sb) {
            try {
                Field field = AbstractCreature.class.getDeclaredField("tips");
                field.setAccessible(true);
                ArrayList<PowerTip> tips = (ArrayList<PowerTip>) field.get(_inst);
                if (_inst instanceof AbstractRhineMonster) {
                    ((AbstractRhineMonster) _inst).addCCTags();
                } else {
                    if (RhineMod.tagLevel > 0) {
                        PowerStrings tag = CardCrawlGame.languagePack.getPowerStrings("rhinemod:AddAtkTag");
                        tips.add(new PowerTip(tag.NAME + String.join("", Collections.nCopies(RhineMod.tagLevel, "I")), tag.DESCRIPTIONS[0] + RhineMod.tagLevel + tag.DESCRIPTIONS[1]));
                    }
                }
                if (RhineMod.tagLevel > 0) {
                    PowerStrings tag = CardCrawlGame.languagePack.getPowerStrings("rhinemod:AddHpTag");
                    tips.add(new PowerTip(tag.NAME + String.join("", Collections.nCopies(RhineMod.tagLevel, "I")), tag.DESCRIPTIONS[0] + RhineMod.tagLevel * 10 + tag.DESCRIPTIONS[1]));
                }
            } catch (Exception ignored) {}
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

}
