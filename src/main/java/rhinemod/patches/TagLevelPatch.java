package rhinemod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import javassist.CtBehavior;
import rhinemod.RhineMod;

import java.lang.reflect.Field;

public class TagLevelPatch {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("rhinemod:TagLevel").TEXT;
    public static final float TAG_LEVEL_LABEL_W = FontHelper.getSmartWidth(FontHelper.cardTitleFont, TEXT[0], 9999.0F, 0.0F);
    public static final float TAG_LEVEL_W = FontHelper.getSmartWidth(FontHelper.cardTitleFont, TEXT[1] + "1", 9999.0F, 0.0F);
    public static final float LABEL_POS = Settings.WIDTH / 2.0F + 550.0F * Settings.scale;
    public static final float LEVEL_POS = Settings.WIDTH / 2.0F + 650.0F * Settings.scale;
    public static Hitbox tagLevelLabelHb;
    public static Hitbox tagLevelLeftHb;
    public static Hitbox tagLevelRightHb;

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
                        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[1] + RhineMod.tagLevel, LEVEL_POS + TAG_LEVEL_W / 2.0F, 70.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);

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
                    if (RhineMod.tagLevel != 0) RhineMod.tagLevel--;
                }

                if (tagLevelRightHb.clicked) {
                    tagLevelRightHb.clicked = false;
                    if (RhineMod.tagLevel != 3) RhineMod.tagLevel++;
                }
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "<ctor>", paramtypez = {String.class, String.class, int.class, float.class, float.class, float.class, float.class, String.class, float.class, float.class, boolean.class})
    public static class AbstractMonsterTagPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Postfix(AbstractMonster _inst) {
            _inst.maxHealth = (int)(_inst.maxHealth * (1.0F + 0.1F * RhineMod.tagLevel));
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(Settings.class, "isMobile");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
