package rhinemod.util;

import basemod.BaseMod;
import basemod.IUIElement;
import basemod.ModLabel;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.options.DropdownMenu;
import com.megacrit.cardcrawl.screens.options.DropdownMenuListener;
import rhinemod.RhineMod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class SettingConfig {
    public static SpireConfig config;
    public static Properties rhineModSettings = new Properties();
    public static ModPanel panel;
    public static final String NEW_MONSTER_MULTI = "newMonsterMulti";
    public static final float[] values = new float[]{0.0F, 0.5F, 1.0F, 6.0F};
    public static void init() {
        rhineModSettings.put(NEW_MONSTER_MULTI, String.valueOf(2));
        try {
            config = new SpireConfig("rhinemod", "rhinemodConfig", rhineModSettings);
            RhineMod.newMonsterMulti = values[config.getInt(NEW_MONSTER_MULTI)];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ModDropdownMenu implements IUIElement, DropdownMenuListener {
        private float xPos;
        private float yPos;
        private final BiConsumer<Integer, String> onChangeSelectionTo;
        private final DropdownMenu menu;
        private final ModLabel textLabel;

        public ModDropdownMenu(BiConsumer<Integer, String> listener, ArrayList<String> options, BitmapFont font, Color textColor, String labelText) {
            onChangeSelectionTo = listener;
            menu = new DropdownMenu(this, options, font, textColor);
            xPos = 380F;
            yPos = 715F;
            Logger.getLogger(SettingConfig.class.getName()).info("Settings scale = " + Settings.scale);
            textLabel = new ModLabel(labelText, xPos, (yPos + 20.0F), textColor, font, SettingConfig.panel, modLabel -> {});
        }

        @Override
        public void render(SpriteBatch sb) {
            sb.setColor(Color.WHITE);
            menu.render(sb, xPos * Settings.xScale, yPos * Settings.yScale);
            textLabel.render(sb);
        }

        @Override
        public void update() {
            menu.update();
            textLabel.update();
        }

        @Override
        public int renderLayer() {
            return 1;
        }

        @Override
        public int updateOrder() {
            return 1;
        }

        @Override
        public void changedSelectionTo(DropdownMenu dropdownMenu, int i, String s) {
            onChangeSelectionTo.accept(i, s);
        }

        @Override
        public void set(float xPos, float yPos) {
             this.xPos = xPos;
             this.yPos = yPos;
             textLabel.set(xPos, yPos + 20.0F);
        }

        @Override
        public float getX() {
             return xPos;
        }

        @Override
        public void setX(float xPos) {
             this.xPos = xPos;
             textLabel.setX(xPos);
        }

        @Override
        public float getY() {
            return yPos;
        }

        @Override
        public void setY(float yPos) {
            this.yPos = yPos;
            textLabel.setY(yPos + 20.0F);
        }
    }

    public static void initMenu() {
        panel = new ModPanel();
        UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rhinemod:Config");
        ModDropdownMenu modMenu = new ModDropdownMenu(
                (i, s) -> {
                    RhineMod.newMonsterMulti = values[i];
                    try {
                        config.setInt(NEW_MONSTER_MULTI, i);
                        config.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                new ArrayList<>(Arrays.asList(uiStrings.EXTRA_TEXT)), FontHelper.charDescFont, Settings.CREAM_COLOR, uiStrings.TEXT[0]);
        modMenu.menu.setSelectedIndex(config.getInt(NEW_MONSTER_MULTI));
        panel.addUIElement(modMenu);
        Texture badge = ImageMaster.loadImage("resources/rhinemod/images/powers/BionicDevice 32.png");
        BaseMod.registerModBadge(badge, "rhinemod", "_noname512, absi2011", "Settings", panel);
    }
}
