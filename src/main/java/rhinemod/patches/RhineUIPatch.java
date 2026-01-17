package rhinemod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.*;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.options.*;
import com.megacrit.cardcrawl.ui.buttons.*;
import com.megacrit.cardcrawl.ui.panels.*;
import com.megacrit.cardcrawl.vfx.TintEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rhinemod.RhineMod;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.util.RhineImageMaster;
import rhinemod.vfx.PileFilesEffect;

public class RhineUIPatch {
    @SpirePatch(clz = CardCrawlGame.class, method = "create")
    public static class InitializePatch {
        @SpireInsertPatch(loc = 10)
        public static void Insert(CardCrawlGame _inst) {
            RhineImageMaster.initialize();
        }
    }

    @SpirePatch(clz = ImageMaster.class, method = "initialize")
    public static class ImageMasterPatch {
        @SpirePostfixPatch
        public static void Postfix() {
            if (RhineMod.useLoneTrail) {
                ImageMaster.TOP_PANEL_BAR = RhineImageMaster.topPanelBase;
                ImageMaster.TIMER_ICON = RhineImageMaster.topPanelTimer;
                ImageMaster.TP_HP = RhineImageMaster.topPanelHeart;
                ImageMaster.TP_GOLD = RhineImageMaster.topPanelGold;
                ImageMaster.TP_FLOOR = RhineImageMaster.topPanelFloor;
                ImageMaster.TP_ASCENSION = RhineImageMaster.topPanelAscension;
                ImageMaster.SETTINGS_ICON = RhineImageMaster.topPanelSettings;
                ImageMaster.END_TURN_HOVER = RhineImageMaster.endTurnButtonHover;
                ImageMaster.END_TURN_BUTTON_GLOW = RhineImageMaster.endTurnButtonGlow;
                ImageMaster.DECK_BTN_BASE = RhineImageMaster.deckButton;
                ImageMaster.DECK_COUNT_CIRCLE = RhineImageMaster.countCircle;
                ImageMaster.DISCARD_BTN_BASE = RhineImageMaster.discardButton;
                ImageMaster.REWARD_SCREEN_SHEET = RhineImageMaster.rewardScreenSheet;
                ImageMaster.REWARD_SCREEN_ITEM = RhineImageMaster.rewardListItemPanel;
                ImageMaster.REWARD_CARD_BOSS = RhineImageMaster.bossCardReward;
                ImageMaster.REWARD_CARD_NORMAL = RhineImageMaster.normalCardReward;
                ImageMaster.UI_GOLD = RhineImageMaster.topPanelGold;
                ImageMaster.RELIC_LINKED = RhineImageMaster.relicLink;
                ImageMaster.REWARD_SCREEN_TAKE_BUTTON = RhineImageMaster.takeAll;
                ImageMaster.REWARD_SCREEN_TAKE_USED_BUTTON = RhineImageMaster.takeAllUsed;
                ImageMaster.POTION_UI_BG = RhineImageMaster.potionBackground;
                ImageMaster.POTION_UI_TOP = RhineImageMaster.potionTop;
                ImageMaster.POTION_UI_BOT = RhineImageMaster.potionBottom;
                ImageMaster.PEEK_BUTTON = RhineImageMaster.peekButton;
                ImageMaster.DYNAMIC_BTN_IMG2 = RhineImageMaster.buttonL;
                ImageMaster.DYNAMIC_BTN_IMG3 = RhineImageMaster.buttonLRed;
                ImageMaster.CANCEL_BUTTON = RhineImageMaster.cancelButton;
                ImageMaster.CANCEL_BUTTON_OUTLINE = RhineImageMaster.cancelButtonOutline;
                ImageMaster.CONFIRM_BUTTON = RhineImageMaster.confirmButton;
                ImageMaster.CONFIRM_BUTTON_OUTLINE = RhineImageMaster.confirmButtonOutline;
                ImageMaster.OPTION_ABANDON = RhineImageMaster.optionAbandon;
                ImageMaster.OPTION_TOGGLE = RhineImageMaster.optionToggle;
                ImageMaster.OPTION_TOGGLE_ON = RhineImageMaster.optionToggleOn;
                ImageMaster.OPTION_SLIDER = RhineImageMaster.optionSlider;
                ImageMaster.OPTION_SLIDER_BG = RhineImageMaster.optionSliderBg;
                ImageMaster.OPTION_EXIT = RhineImageMaster.optionExit;
                ImageMaster.OPTION_CONFIRM = RhineImageMaster.optionConfirm;
                ImageMaster.OPTION_YES = RhineImageMaster.optionYes;
                ImageMaster.OPTION_NO = RhineImageMaster.optionNo;
                ImageMaster.RENAME_BOX = RhineImageMaster.renameBox;
                ImageMaster.INPUT_SETTINGS_EDGES = RhineImageMaster.inputSettings;
                ImageMaster.INPUT_SETTINGS_BG = RhineImageMaster.inputSettings;
                ImageMaster.SETTINGS_BACKGROUND = RhineImageMaster.settingsBackground;
                ImageMaster.RETICLE_CORNER = RhineImageMaster.reticleCorner;
                ImageMaster.CHECKBOX = RhineImageMaster.checkBox;
                ImageMaster.TICK = RhineImageMaster.tick;
                ImageMaster.POPUP_ARROW = RhineImageMaster.popupArrow;
                ImageMaster.SCROLL_BAR_HORIZONTAL_TRAIN = RhineImageMaster.scrollBarHorizontalTrain;
                ImageMaster.SCROLL_BAR_TRAIN = RhineImageMaster.scrollBarTrain;
                ImageMaster.SCROLL_BAR_HORIZONTAL_MIDDLE = RhineImageMaster.scrollBarTrackHorizontalMid;
                ImageMaster.SCROLL_BAR_MIDDLE = RhineImageMaster.scrollBarTrackMid;
                ImageMaster.SCROLL_BAR_BOTTOM = RhineImageMaster.scrollBarTrackBottom;
                ImageMaster.SCROLL_BAR_LEFT = RhineImageMaster.scrollBarTrackLeft;
                ImageMaster.SCROLL_BAR_RIGHT = RhineImageMaster.scrollBarTrackRight;
                ImageMaster.SCROLL_BAR_TOP = RhineImageMaster.scrollBarTrackTop;
                ImageMaster.EVENT_ROOM_PANEL = RhineImageMaster.eventTextPanel;
                ImageMaster.EVENT_BUTTON_ENABLED = RhineImageMaster.eventEnabledButton;
                ImageMaster.EVENT_BUTTON_DISABLED = RhineImageMaster.eventDisabledButton;
                ImageMaster.MAP_NODE_TREASURE = RhineImageMaster.mapChest;
                ImageMaster.MAP_NODE_ELITE = RhineImageMaster.mapElite;
                ImageMaster.MAP_NODE_EVENT = RhineImageMaster.mapEvent;
                ImageMaster.MAP_NODE_ENEMY = RhineImageMaster.mapMonster;
                ImageMaster.MAP_NODE_REST = RhineImageMaster.mapRest;
                ImageMaster.MAP_NODE_MERCHANT = RhineImageMaster.mapShop;
                ImageMaster.MAP_LEGEND = RhineImageMaster.mapLegend;
            }
        }
    }

    @SpirePatch(clz = GameCursor.class, method = "render")
    public static class CursorRenderPatch {
        public static final float OFFSET_X = 24.0F * Settings.scale;
        public static final float OFFSET_Y = -24.0F * Settings.scale;
        @SpireInsertPatch(rloc = 5)
        public static SpireReturn<?> Insert(GameCursor _inst, SpriteBatch sb) {
            float rotation = ReflectionHacks.getPrivate(_inst, GameCursor.class, "rotation");
            GameCursor.CursorType type = ReflectionHacks.getPrivate(_inst, GameCursor.class, "type");
            if (RhineMod.useLoneTrail) {
                sb.setColor(Color.WHITE);
                switch (type) {
                    case NORMAL:
                        sb.draw(RhineImageMaster.cursor1, InputHelper.mX - 32.0F + OFFSET_X, InputHelper.mY - 32.0F + OFFSET_Y, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, rotation, 0, 0, 64, 64, false, false);
                        break;
                    case INSPECT:
                        sb.draw(RhineImageMaster.cursor2, InputHelper.mX - 32.0F + OFFSET_X, InputHelper.mY - 32.0F + OFFSET_Y, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, rotation, 0, 0, 64, 64, false, false);
                        break;
                }
                _inst.changeType(GameCursor.CursorType.NORMAL);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "renderName")
    public static class TopPanelNameRenderPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(TopPanel _inst, SpriteBatch sb, String ___name, float ___nameX, String ___title, float ___titleX, float ___titleY, float ___ICON_Y, float ___NAME_Y) {
            if (RhineMod.useLoneTrail) {
                if (Settings.isEndless) {
                    sb.draw(RhineImageMaster.topPanelEndless, -32.0F + 46.0F * Settings.scale, ___ICON_Y - 32.0F + 29.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                } else if (Settings.isFinalActAvailable) {
                    if (Settings.hasSapphireKey) {
                        sb.draw(RhineImageMaster.topPanelKeyBlue, -32.0F + 46.0F * Settings.scale, ___ICON_Y - 32.0F + 29.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                    }
                    if (Settings.hasRubyKey) {
                        sb.draw(RhineImageMaster.topPanelKeyRed, -32.0F + 46.0F * Settings.scale, ___ICON_Y - 32.0F + 29.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                    }
                    if (Settings.hasEmeraldKey) {
                        sb.draw(RhineImageMaster.topPanelKeyGreen, -32.0F + 46.0F * Settings.scale, ___ICON_Y - 32.0F + 29.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                    }
                }

                FontHelper.renderFontLeftTopAligned(sb, FontHelper.panelNameFont, ___name, ___nameX, ___NAME_Y, Color.WHITE);
                if (Settings.isMobile) {
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipBodyFont, ___title, ___nameX, ___titleY - 30.0F * Settings.scale, Color.LIGHT_GRAY);
                } else {
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipBodyFont, ___title, ___titleX, ___titleY, Color.LIGHT_GRAY);
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "renderDeckIcon")
    public static class TopPanelDeckIconRenderPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
                        m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail) { $1 = " + RhineImageMaster.class.getName() + ".topPanelDeck; $10 = 0; $_ = $proceed($$); } else { $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "renderMapIcon")
    public static class TopPanelMapIconRenderPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
                        m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail) { $1 = " + RhineImageMaster.class.getName() + ".topPanelMap; $10 = 0; $_ = $proceed($$); } else { $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = EndTurnButton.class, method = "render")
    public static class EndTurnButtonRenderPatch {
        @SpireInsertPatch(rloc = 57, localvars = {"buttonImg"})
        public static void Insert(EndTurnButton _inst, SpriteBatch sb, @ByRef Texture[] buttonImg) {
            if (RhineMod.useLoneTrail) {
                Hitbox hb = ReflectionHacks.getPrivate(_inst, EndTurnButton.class, "hb");
                if (_inst.isGlowing && !hb.clickStarted) {
                    buttonImg[0] = RhineImageMaster.endTurnButtonGlow;
                } else {
                    buttonImg[0] = RhineImageMaster.endTurnButton;
                }
            }
        }
    }

    @SpirePatch(clz = EndTurnButton.class, method = "render")
    public static class EndTurnButtonRenderPatch3 {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("renderFontCentered")) {
                        m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail) { $4 = $4 + 20.0F * " + Settings.class.getName() + ".scale; $_ = $proceed($$); } else { $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame")
    public static class LargeCardRenderFramePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(SingleCardViewPopup _inst, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(_inst, SingleCardViewPopup.class, "card");
            if (RhineMod.useLoneTrail && card instanceof AbstractRhineCard) {
                Texture img;
                largeFrameLabel:
                switch (card.type) {
                    case ATTACK:
                        switch (card.rarity) {
                            case UNCOMMON:
                                img = RhineImageMaster.attackUncommonL;
                                break largeFrameLabel;
                            case RARE:
                                img = RhineImageMaster.attackRareL;
                                break largeFrameLabel;
                            case COMMON:
                            default:
                                img = RhineImageMaster.attackCommonL;
                                break largeFrameLabel;
                        }
                    case POWER:
                        switch (card.rarity) {
                            case UNCOMMON:
                                img = RhineImageMaster.powerUncommonL;
                                break largeFrameLabel;
                            case RARE:
                                img = RhineImageMaster.powerRareL;
                                break largeFrameLabel;
                            case COMMON:
                            default:
                                img = RhineImageMaster.powerCommonL;
                                break largeFrameLabel;
                        }
                    case SKILL:
                    default:
                        switch (card.rarity) {
                            case UNCOMMON:
                                img = RhineImageMaster.skillUncommonL;
                                break largeFrameLabel;
                            case RARE:
                                img = RhineImageMaster.skillRareL;
                                break largeFrameLabel;
                            case COMMON:
                            default:
                                img = RhineImageMaster.skillCommonL;
                                break largeFrameLabel;
                        }
                }
                ReflectionHacks.RMethod method = ReflectionHacks.privateMethod(SingleCardViewPopup.class, "renderHelper", SpriteBatch.class, float.class, float.class, TextureAtlas.AtlasRegion.class);
                TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(img, 0, 0, 1024, 1024);
                method.invoke(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, region);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardBanner")
    public static class LargeCardRenderBannerPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(SingleCardViewPopup _inst, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(_inst, SingleCardViewPopup.class, "card");
            if (RhineMod.useLoneTrail && card instanceof AbstractRhineCard) {
                Texture img;
                switch (card.rarity) {
                    case UNCOMMON:
                        img = RhineImageMaster.uncommonBannerL;
                        break;
                    case RARE:
                        img = RhineImageMaster.rareBannerL;
                        break;
                    case COMMON:
                    default:
                        img = RhineImageMaster.commonBannerL;
                        break;
                }
                ReflectionHacks.RMethod method = ReflectionHacks.privateMethod(SingleCardViewPopup.class, "renderHelper", SpriteBatch.class, float.class, float.class, TextureAtlas.AtlasRegion.class);
                TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(img, 0, 0, 1024, 1024);
                method.invoke(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, region);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardTypeText")
    public static class LargeCardRenderTypePatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("renderFontCentered")) {
                        m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail) { $6 = " + Settings.class.getName() + ".CREAM_COLOR; $_ = $proceed($$); } else { $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderPortraitFrame")
    public static class SmallCardRenderFramePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractCard _inst, SpriteBatch sb, float x, float y) {
            if (RhineMod.useLoneTrail && _inst instanceof AbstractRhineCard) {
                Texture img;
                smallFrameLabel:
                switch (_inst.type) {
                    case ATTACK:
                        switch (_inst.rarity) {
                            case UNCOMMON:
                                img = RhineImageMaster.attackUncommon;
                                break smallFrameLabel;
                            case RARE:
                                img = RhineImageMaster.attackRare;
                                break smallFrameLabel;
                            case COMMON:
                            default:
                                img = RhineImageMaster.attackCommon;
                                break smallFrameLabel;
                        }
                    case POWER:
                        switch (_inst.rarity) {
                            case UNCOMMON:
                                img = RhineImageMaster.powerUncommon;
                                break smallFrameLabel;
                            case RARE:
                                img = RhineImageMaster.powerRare;
                                break smallFrameLabel;
                            case COMMON:
                            default:
                                img = RhineImageMaster.powerCommon;
                                break smallFrameLabel;
                        }
                    case SKILL:
                    default:
                        switch (_inst.rarity) {
                            case UNCOMMON:
                                img = RhineImageMaster.skillUncommon;
                                break smallFrameLabel;
                            case RARE:
                                img = RhineImageMaster.skillRare;
                                break smallFrameLabel;
                            case COMMON:
                            default:
                                img = RhineImageMaster.skillCommon;
                                break smallFrameLabel;
                        }
                }
                ReflectionHacks.RMethod method = ReflectionHacks.privateMethod(AbstractCard.class, "renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
                Color renderColor = ReflectionHacks.getPrivate(_inst, AbstractCard.class, "renderColor");
                TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(img, 0, 0, 512, 512);
                method.invoke(_inst, sb, renderColor, region, x, y);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderBannerImage")
    public static class SmallCardRenderBannerPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractCard _inst, SpriteBatch sb, float x, float y) {
            if (RhineMod.useLoneTrail && _inst instanceof AbstractRhineCard) {
                Texture img;
                switch (_inst.rarity) {
                    case UNCOMMON:
                        img = RhineImageMaster.uncommonBanner;
                        break;
                    case RARE:
                        img = RhineImageMaster.rareBanner;
                        break;
                    case COMMON:
                    default:
                        img = RhineImageMaster.commonBanner;
                        break;
                }
                ReflectionHacks.RMethod method = ReflectionHacks.privateMethod(AbstractCard.class, "renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
                Color renderColor = ReflectionHacks.getPrivate(_inst, AbstractCard.class, "renderColor");
                TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(img, 0, 0, 512, 512);
                method.invoke(_inst, sb, renderColor, region, x, y);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderType")
    public static class SmallCardRenderTypePatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("renderRotatedText")) {
                        m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail) { $10 = renderColor; $_ = $proceed($$); } else { $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "<ctor>")
    public static class TopPanelInitPatch {
        @SpirePostfixPatch
        public static void Postfix(TopPanel _inst, @ByRef Texture[] ___potionSelectBox) {
            ___potionSelectBox[0] = RhineImageMaster.potionSelectBox;
        }
    }

    @SpirePatch(clz = DynamicBanner.class, method = "render")
    public static class RenderDynamicBannerPatch2 {
        public static float angle = 0.0F;
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(DynamicBanner _inst, SpriteBatch sb, TintEffect ___textTint, TintEffect ___tint, String ___label) {
            if (!RhineMod.useLoneTrail) return SpireReturn.Continue();
            if (!_inst.show || ___textTint.color.a == 0.0F || ___label == null) return SpireReturn.Return();
            sb.setColor(___textTint.color);
            sb.draw(RhineImageMaster.selectBanner, Settings.WIDTH / 2.0F - 556.0F, _inst.y - 119.0F, 556.0F, 119.0F, 1112.0F, 238.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1112, 238, false, false);
            sb.draw(RhineImageMaster.bannerRing, Settings.WIDTH / 2.0F - 290.0F, _inst.y - 20.0F, 45.0F, 45.0F, 90.0F, 90.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 90, 90, false, false);
            sb.draw(RhineImageMaster.bannerRing, Settings.WIDTH / 2.0F + 200.0F, _inst.y - 20.0F, 45.0F, 45.0F, 90.0F, 90.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 90, 90, false, false);
            RhineMod.selectBannerEffect.setPosition(Settings.WIDTH / 2.0F - 300.0F, _inst.y);
            RhineMod.selectBannerEffect.render(sb, ___tint.color.a);

            sb.setColor(___textTint.color);
            angle += 1.2F;
            sb.draw(RhineImageMaster.bannerCircleL, Settings.WIDTH / 2.0F - 290.0F, _inst.y - 20.0F, 45.0F, 45.0F, 90.0F, 90.0F, Settings.scale, Settings.scale, angle, 0, 0, 90, 90, false, false);
            sb.draw(RhineImageMaster.bannerCircleR, Settings.WIDTH / 2.0F + 200.0F, _inst.y - 20.0F, 45.0F, 45.0F, 90.0F, 90.0F, Settings.scale, Settings.scale, angle * 2, 0, 0, 90, 90, false, false);
            sb.draw(RhineImageMaster.bannerPointer, Settings.WIDTH / 2.0F + 200.0F, _inst.y - 20.0F, 45.0F, 45.0F, 90.0F, 90.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 90, 90, false, false);

            FontHelper.renderFontCentered(sb, FontHelper.losePowerFont, ___label, (float)Settings.WIDTH / 2.0F, _inst.y + 22.0F * Settings.scale, ___textTint.color, _inst.scale);
            return SpireReturn.Return();
        }

        static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = MenuCancelButton.class, method = "renderShadow")
    public static class RenderMenuCancelButtonShadowPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(MenuCancelButton _inst, SpriteBatch sb) {
            if (RhineMod.useLoneTrail) return SpireReturn.Return();
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CancelButton.class, method = "renderShadow")
    public static class RenderCancelButtonShadowPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(CancelButton _inst, SpriteBatch sb) {
            if (RhineMod.useLoneTrail) return SpireReturn.Return();
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ConfirmButton.class, method = "renderShadow")
    public static class RenderConfirmButtonShadowPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(ConfirmButton _inst, SpriteBatch sb) {
            if (RhineMod.useLoneTrail) return SpireReturn.Return();
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = GridSelectConfirmButton.class, method = "renderShadow")
    public static class RenderGridSelectConfirmButtonShadowPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(GridSelectConfirmButton _inst, SpriteBatch sb) {
            if (RhineMod.useLoneTrail) return SpireReturn.Return();
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ConfirmPopup.class, method = "renderButtons")
    public static class RenderConfirmPopupTextPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("renderFontCentered")) {
                        m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail) { $3 = \"\"; $_ = $proceed($$); } else { $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = ProceedButton.class, method = "render")
    public static class RenderProceedButtonOutlinePatch {
        public static int count = 0;
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
                        count++;
                        if (count == 1) {
                            m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail && " + AbstractDungeon.class.getName() + ".screen != " +
                                    AbstractDungeon.class.getName() + ".CurrentScreen.FTUE) { $1 = " + RhineImageMaster.class.getName() +
                                    ".proceedButtonOutline; $3 = $3 + 200.0F * " + Settings.class.getName() + ".scale; $_ = $proceed($$); } else { $proceed($$); }");
                        }
                    }
                }
            };
        }
    }

    @SpirePatch(clz = ProceedButton.class, method = "renderButton")
    public static class RenderProceedButtonPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
                        m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail && " + AbstractDungeon.class.getName() + ".screen != " +
                                AbstractDungeon.class.getName() + ".CurrentScreen.FTUE) { $1 = " + RhineImageMaster.class.getName() +
                                ".proceedButton; $3 = $3 + 200.0F * " + Settings.class.getName() + ".scale; $_ = $proceed($$); } else { $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = ProceedButton.class, method = "renderShadow")
    public static class RenderProceedButtonShadowPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(ProceedButton _inst, SpriteBatch sb) {
            if (RhineMod.useLoneTrail && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.FTUE) return SpireReturn.Return();
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ProceedButton.class, method = "<ctor>")
    public static class ProceedButtonHitboxPatch {
        @SpirePostfixPatch
        public static void Postfix(ProceedButton _inst, Hitbox ___hb) {
            if (RhineMod.useLoneTrail) ___hb.height += 250.0F * Settings.scale;
        }
    }

    @SpirePatch(clz = DrawPileViewScreen.class, method = "render")
    public static class RenderDrawPileViewScreenPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(DrawPileViewScreen _inst, SpriteBatch sb, CardGroup ___drawPileCopy, AbstractCard ___hoveredCard) {
            if (!RhineMod.useLoneTrail) return SpireReturn.Continue();

            renderFiles(sb);
            if (___hoveredCard == null) {
                ___drawPileCopy.render(sb);
            } else {
                ___drawPileCopy.renderExceptOneCard(sb, ___hoveredCard);
                ___hoveredCard.renderHoverShadow(sb);
                ___hoveredCard.render(sb);
                ___hoveredCard.renderCardTip(sb);
            }
            if (!AbstractDungeon.player.hasRelic("Frozen Eye")) {
                FontHelper.renderDeckViewTip(sb, DrawPileViewScreen.TEXT[1], 48.0F * Settings.scale, Settings.GOLD_COLOR);
            }
            FontHelper.renderDeckViewTip(sb, DrawPileViewScreen.TEXT[0], 96.0F * Settings.scale, Settings.CREAM_COLOR);
            AbstractDungeon.overlayMenu.combatDeckPanel.render(sb);
            return SpireReturn.Return();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(DrawPileViewScreen.class, "hoveredCard");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }

        public static PileFilesEffect effect = null;

        public static void renderFiles(SpriteBatch sb) {
            if (effect == null) effect = new PileFilesEffect(-400.0F * Settings.scale, 600.0F * Settings.scale, 400.0F * Settings.scale, 0.0F, -0.1F);
            effect.render(sb);
        }
    }

    @SpirePatch(clz = DiscardPileViewScreen.class, method = "render")
    public static class RenderDiscardPileViewScreenPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(DiscardPileViewScreen _inst, SpriteBatch sb, AbstractCard ___hoveredCard) {
            if (!RhineMod.useLoneTrail) return SpireReturn.Continue();
            renderFiles(sb);
            if (___hoveredCard == null) {
                AbstractDungeon.player.discardPile.render(sb);
            } else {
                AbstractDungeon.player.discardPile.renderExceptOneCard(sb, ___hoveredCard);
                ___hoveredCard.renderHoverShadow(sb);
                ___hoveredCard.render(sb);
                ___hoveredCard.renderCardTip(sb);
            }
            FontHelper.renderDeckViewTip(sb, DiscardPileViewScreen.TEXT[0], 96.0F * Settings.scale, Settings.CREAM_COLOR);
            AbstractDungeon.overlayMenu.discardPilePanel.render(sb);
            return SpireReturn.Return();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(DiscardPileViewScreen.class, "hoveredCard");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }

        public static PileFilesEffect effect = null;

        public static void renderFiles(SpriteBatch sb) {
            if (effect == null) effect = new PileFilesEffect(Settings.WIDTH + 400.0F * Settings.scale, 600.0F * Settings.scale, Settings.WIDTH - 400.0F * Settings.scale, 0.0F, 0.1F);
            effect.render(sb);
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class ResetPileFilesEffectPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon _inst, SpriteBatch sb) {
            if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GAME_DECK_VIEW) RenderDrawPileViewScreenPatch.effect = null;
            if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.DISCARD_VIEW) RenderDiscardPileViewScreenPatch.effect = null;
        }
    }

    @SpirePatch(clz = DoorLock.class, method = "<ctor>")
    public static class DoorLockPatch {
        public static int count = 0;
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(ImageMaster.class.getName()) && m.getMethodName().equals("loadImage")) {
                        count++;
                        String[] icons = {"lockRed", "glowRed", "lockGreen", "glowGreen", "lockBlue", "glowBlue"};
                        String icon = icons[count - 1];
                        m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail) { $1 = " + RhineImageMaster.class.getName() + "." + icon + "; $_ = $proceed($$); } else { $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = DoorUnlockScreen.class, method = "open")
    public static class DoorUnlockScreenPatch {
        public static int count = 0;
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(ImageMaster.class.getName()) && m.getMethodName().equals("loadImage")) {
                        count++;
                        String[] icons = {"doorLeft", "doorRight", "circleLeft", "circleRight"};
                        String icon = icons[count - 1];
                        m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail) { $1 = " + RhineImageMaster.class.getName() + "." + icon + "; $_ = $proceed($$); } else { $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = GenericEventDialog.class, method = "render")
    public static class GenericEventDialogPatch {
        public static int count = 0;
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
                        count++;
                        if (count == 1) {
                            m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail) { $1 = " + RhineImageMaster.class.getName() + ".eventPanel; $_ = $proceed($$); } else { $proceed($$); }");
                        }
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractEvent.class, method = "renderRoomEventPanel")
    public static class renderRoomEventPanelPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("setColor")) {
                        m.replace("if (" + RhineMod.class.getName() + ".useLoneTrail) { $1 = " + Color.class.getName() + ".WHITE; $_ = $proceed($$); } else { $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = DungeonMap.class, method = "<ctor>")
    public static class DungeonMapPatch {
        @SpirePostfixPatch
        public static void Postfix(DungeonMap _inst, @ByRef Texture[] ___top, @ByRef Texture[] ___mid, @ByRef Texture[] ___bot) {
            if (RhineMod.useLoneTrail) {
                ___top[0] = RhineImageMaster.mapTop;
                ___mid[0] = RhineImageMaster.mapMid;
                ___bot[0] = RhineImageMaster.mapBot;
            }
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "getMapImgOutline")
    public static class GetMapImgOutlinePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractRoom _inst) {
            if (RhineMod.useLoneTrail) return SpireReturn.Return(RhineImageMaster.mapOutline);
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MapRoomNode.class, method = "<ctor>")
    public static class MapRoomNodeColorPatch {
        @SpirePostfixPatch
        public static void Postfix(MapRoomNode _inst, int x, int y) {
            if (RhineMod.useLoneTrail) {
                MapRoomNode.AVAILABLE_COLOR.r = 1.0F;
                MapRoomNode.AVAILABLE_COLOR.g = 1.0F;
                MapRoomNode.AVAILABLE_COLOR.b = 1.0F;
                Color notTakenColor = ReflectionHacks.getPrivateStatic(MapRoomNode.class, "NOT_TAKEN_COLOR");
                notTakenColor.r = 0.8F;
                notTakenColor.g = 0.8F;
                notTakenColor.b = 0.8F;
            }
        }
    }

    @SpirePatch(clz = MapEdge.class, method = SpirePatch.CLASS)
    public static class OptFields {
        public static SpireField<Float> sx = new SpireField<>(() -> 0.0F);
        public static SpireField<Float> sy = new SpireField<>(() -> 0.0F);
        public static SpireField<Float> dx = new SpireField<>(() -> 0.0F);
        public static SpireField<Float> dy = new SpireField<>(() -> 0.0F);
    }

    @SpirePatch(clz = MapEdge.class, method = "<ctor>", paramtypez = {int.class, int.class, float.class, float.class, int.class, int.class, float.class, float.class, boolean.class})
    public static class MapEdgeInitPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmpSX", "tmpDX", "tmpSY", "tmpDY", "tmpRadius"})
        public static void Insert(MapEdge _inst, int srcX, int srcY, float srcOffsetX, float srcOffsetY, int dstX, int dstY, float dstOffsetX, float dstOffsetY, boolean isBoss, float tmpSX, float tmpDX, float tmpSY, float tmpDY, float tmpRadius) {
            float dx = tmpDX - tmpSX;
            float dy = tmpDY - tmpSY;
            float len = (float)Math.sqrt(dx * dx + dy * dy);
            tmpSX += dx / len * MapEdge.ICON_SRC_RADIUS;
            tmpSY += dy / len * MapEdge.ICON_SRC_RADIUS;
            tmpDX -= dx / len * tmpRadius;
            tmpDY -= dy / len * tmpRadius;
            OptFields.sx.set(_inst, tmpSX);
            OptFields.sy.set(_inst, tmpSY);
            OptFields.dx.set(_inst, tmpDX);
            OptFields.dy.set(_inst, tmpDY);
        }

        static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(MapEdge.class, "ICON_SRC_RADIUS");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = MapEdge.class, method = "render")
    public static class RenderMapEdgePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(MapEdge _inst, SpriteBatch sb) {
            if (!RhineMod.useLoneTrail) return SpireReturn.Continue();
            float srcX = OptFields.sx.get(_inst);
            float srcY = OptFields.sy.get(_inst);
            float dstX = OptFields.dx.get(_inst);
            float dstY = OptFields.dy.get(_inst);
            sb.setColor(_inst.color);
            float dx = dstX - srcX;
            float dy = dstY - srcY;
            float len = (float)Math.sqrt(dx * dx + dy * dy);
            float angle = (float)Math.toDegrees(Math.atan2(dy, dx));
            float width = 16.0F;
            float r = width * 0.5F;
            float OFFSET_Y = 172.0F * Settings.scale;
            sb.draw(RhineImageMaster.mapLine, srcX - r, srcY + DungeonMapScreen.offsetY + OFFSET_Y, r, 0, width, len, 1, 1, angle - 90, 0, 0, 16, 16, false, false);
            sb.draw(RhineImageMaster.mapDot, srcX - r, srcY + DungeonMapScreen.offsetY + OFFSET_Y, r, 0, width, width, 1, 1, angle - 270, 0, 0, 16, 16, false, false);
            sb.draw(RhineImageMaster.mapDot, dstX - r, dstY + DungeonMapScreen.offsetY + OFFSET_Y, r, 0, width, width, 1, 1, angle - 90, 0, 0, 16, 16, false, false);
            return SpireReturn.Return();
        }
    }
}
