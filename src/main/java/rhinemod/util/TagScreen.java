package rhinemod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import rhinemod.patches.TagLevelPatch;

import java.util.ArrayList;
import java.util.Arrays;

public class TagScreen implements ScrollBarListener {
    private static final float text_max_width = Settings.isMobile ? 1170.0F * Settings.scale : 1050.0F * Settings.scale;
    private static final float line_spacing = Settings.BIG_TEXT_MODE ? 40.0F * Settings.scale : 32.0F * Settings.scale;
    private MenuCancelButton cancelButton = new MenuCancelButton();
    public boolean screenUp;
    public static float screenX;
    private ArrayList<String> tagList;
    private boolean grabbedScreen;
    private float grabStartY;
    private float targetY;
    private float scrollY;
    private float scrollUpperBound;
    private float scrollLowerBound;
    private final ScrollBar scrollBar;
    public TagScreen() {
        screenX = Settings.isMobile ? (240.0F * Settings.xScale) : (300.0F * Settings.xScale);
        grabbedScreen = false;
        screenUp = false;
        grabStartY = 0.0F;
        targetY = 0.0F;
        scrollY = 0.0F;
        if (Settings.isMobile) {
            scrollBar = new ScrollBar(this, Settings.WIDTH - 280.0F * Settings.xScale - ScrollBar.TRACK_W / 2.0F, Settings.HEIGHT / 2.0F, Settings.HEIGHT - 256.0F * Settings.scale, true);
        } else {
            scrollBar = new ScrollBar(this, Settings.WIDTH - 280.0F * Settings.xScale - ScrollBar.TRACK_W / 2.0F, Settings.HEIGHT / 2.0F, Settings.HEIGHT - 256.0F * Settings.scale);
        }
    }

    private void initTagList() {
        tagList = new ArrayList<>();
        if (TagLevelPatch.curTagLevel == 0) {
            return;
        }
        UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rhinemod:Level" + TagLevelPatch.curTagLevel + "Tags");       tagList.addAll(Arrays.asList(uiStrings.TEXT));
    }

    @SpireEnum public static MainMenuScreen.CurScreen TAG_LIST;

    public void open() {
        targetY = 0.0F;
        screenUp = true;
        CardCrawlGame.mainMenuScreen.screen = TAG_LIST;
        cancelButton.show(CharacterSelectScreen.TEXT[5]);
        initTagList();
        calculateScrollBounds();
    }

    public void update() {
        updateCancelButton();
        updateScrolling();
    }

    private void updateCancelButton() {
        cancelButton.update();
        if (cancelButton.hb.clicked || InputHelper.pressedEscape) {
            InputHelper.pressedEscape = false;
            cancelButton.hb.clicked = false;
            cancelButton.hide();
            CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.CHAR_SELECT;
        }
    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (scrollUpperBound > 0.0F) {
            if (!grabbedScreen) {
                if (InputHelper.scrolledDown) {
                    targetY += Settings.SCROLL_SPEED;
                } else if (InputHelper.scrolledUp) {
                    targetY -= Settings.SCROLL_SPEED;
                }

                if (InputHelper.justClickedLeft) {
                    grabbedScreen = true;
                    grabStartY = y - targetY;
                }
            } else if (InputHelper.isMouseDown) {
                targetY = y - grabStartY;
            } else {
                grabbedScreen = false;
            }
        }

        scrollY = MathHelper.scrollSnapLerpSpeed(scrollY, targetY);
        if (targetY < scrollLowerBound) {
            targetY = MathHelper.scrollSnapLerpSpeed(targetY, scrollLowerBound);
        } else if (targetY > scrollUpperBound) {
            targetY = MathHelper.scrollSnapLerpSpeed(targetY, scrollUpperBound);
        }

        updateBarPosition();
    }

    private void calculateScrollBounds() {
        scrollUpperBound = tagList.size() * 90.0F * Settings.scale + 270.0F * Settings.scale;
        scrollLowerBound = 100.0F * Settings.scale;
    }

    @Override
    public void scrolledUsingBar(float newPercent) {
        float newPosition = MathHelper.valueFromPercentBetween(scrollLowerBound, scrollUpperBound, newPercent);
        scrollY = newPosition;
        targetY = newPosition;
        updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(scrollLowerBound, scrollUpperBound, scrollY);
        scrollBar.parentScrolledToPercent(percent);
    }

    public void render(SpriteBatch sb) {
        scrollBar.render(sb);
        cancelButton.render(sb);
        sb.setColor(Color.WHITE);
        float height = scrollY;
        for (String tag : tagList) {
            FontHelper.renderSmartText(sb, FontHelper.charDescFont, tag, (screenX + 120.0F) * Settings.scale, height + 12.0F * Settings.scale, text_max_width, line_spacing, Settings.CREAM_COLOR);
            height -= FontHelper.getSmartHeight(FontHelper.charDescFont, tag, text_max_width, line_spacing) + 70.0F * Settings.scale;
        }
    }
}
