package rhinemod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rhinemod.cards.AbstractRhineCard;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Logger;

public class HandCardSelectPatch {

    @SpirePatch(clz = HandCardSelectScreen.class, method = SpirePatch.CLASS)
    public static class OptFields {
        public static SpireField<Boolean> selectingBranch = new SpireField<>(() -> false);
    }
    static public final int MAX_BRANCH = 4;
    public static AbstractCard[] branches = new AbstractCard[MAX_BRANCH];
    public static Hitbox[] hitboxes = new Hitbox[MAX_BRANCH];
    public static int branchesNum;
    public static boolean cardChecked;
    public static UUID curId;

    public static boolean IsSelectingBranch() {
        return OptFields.selectingBranch.get(AbstractDungeon.handCardSelectScreen);
    }

    public static void setBranchesPreview(HandCardSelectScreen _inst, AbstractRhineCard card) {
        if (!cardChecked) {
            curId = card.uuid;
            branchesNum = card.possibleBranches().size();
            for (int i = 0; i < branchesNum; i++) {
                AbstractRhineCard preview = card.makeStatEquivalentCopy();
                preview.chosenBranch = i;
                preview.possibleBranches().get(i).upgrade();
                preview.displayUpgrades();
                branches[i] = preview;
                hitboxes[i] = new Hitbox(0, 0);
            }
            cardChecked = true;
        }
        OptFields.selectingBranch.set(_inst, true);
        updateBranchPreview();
    }

    public static void updateBranchPreview() {
        branches[0].drawScale = 0.7F;
        branches[0].current_x = Settings.WIDTH * 0.63F;
        branches[0].current_y = Settings.HEIGHT / 2F + 160F * Settings.scale;
        branches[0].target_x = Settings.WIDTH * 0.63F;
        branches[0].target_y = Settings.HEIGHT / 2F + 160F * Settings.scale;
        branches[0].transparency = 1F;
        for (int i = 1; i < branchesNum; i++) {
            branches[i].drawScale = 0.55F;
            branches[i].current_x = Settings.WIDTH * (0.75F + (i == 3? 0.1F : 0.0F));
            branches[i].current_y = Settings.HEIGHT * (0.75F - (i - 1) % 2 * 0.24F);
            branches[i].target_x = Settings.WIDTH * (0.75F + (i == 3? 0.1F : 0.0F));
            branches[i].target_y = Settings.HEIGHT * (0.75F - (i - 1) % 2 * 0.24F);
            branches[i].transparency = 1F;
            branches[i].targetTransparency = 1F;
            hitboxes[i].resize(300.0F * Settings.scale * 0.5F, 420.0F * Settings.scale * 0.5F);
            hitboxes[i].move(branches[i].current_x, branches[i].current_y);
        }
    }

    public static class CheckIfCardHasBranches {
        public static void Insert(HandCardSelectScreen _inst) {
            AbstractCard card = _inst.selectedCards.group.get(0);
            if (card instanceof AbstractRhineCard && ((AbstractRhineCard) card).possibleBranches().size() > 1) {
                setBranchesPreview(_inst, (AbstractRhineCard) card);
            } else {
                branchesNum = 0;
            }
        }
    }

    @SpirePatch(clz = HandCardSelectScreen.class, method = "updateSelectedCards")
    public static class UpdateInputButtons {
        @SpirePrefixPatch
        public static void Prefix(HandCardSelectScreen _inst) {
            if (IsSelectingBranch()) {
                for (int i = 1; i < branchesNum; i++) {
                    Hitbox hb = hitboxes[i];
                    hb.update();
                    if (hb.justHovered) {
                        CardCrawlGame.sound.play("UI_HOVER");
                    }
                    if (hb.hovered && InputHelper.justClickedLeft) {
                        hb.clickStarted = true;
                        CardCrawlGame.sound.play("UI_CLICK_1");
                    }
                    if (hb.clicked) {
                        hb.clicked = false;
                        Collections.swap(Arrays.asList(branches), 0, i);
                        updateBranchPreview();
                    }
                }
            }
        }
    }

    @SpirePatch(clz = HandCardSelectScreen.class, method = "render")
    public static class RenderUpgradeBranches {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(HandCardSelectScreen.class.getName()) && m.getMethodName().equals("renderArrows")) {
                        m.replace("$_ = $proceed($$); " +
                                "if (" + HandCardSelectPatch.class.getName() + ".RenderingBranches(this, sb)) {return;}");
                    }
                }
            };
        }
    }

    public static boolean RenderingBranches(HandCardSelectScreen _inst, SpriteBatch sb) {
        AbstractCard card = _inst.selectedCards.group.get(0);
        boolean forUpgrade;
        try {
            Field upgrade = _inst.getClass().getDeclaredField("forUpgrade");
            upgrade.setAccessible(true);
            forUpgrade = upgrade.getBoolean(_inst);
        } catch (Exception e) {
            forUpgrade = false;
        }
        if (forUpgrade) {
            if (cardChecked && card.uuid != curId) cardChecked = false;
            if (!cardChecked) CheckIfCardHasBranches.Insert(_inst);
        }
        forUpgrade = forUpgrade && OptFields.selectingBranch.get(_inst);
        if (!forUpgrade) return false;
        branches[0].beginGlowing();
        for (int i = 0; i < branchesNum; i++) {
            if (i != 0) branches[i].stopGlowing();
            branches[i].applyPowers();
            branches[i].render(sb);
            branches[i].updateHoverLogic();
            branches[i].renderCardTip(sb);
        }
        try {
            Field peekButton = _inst.getClass().getDeclaredField("peekButton");
            peekButton.setAccessible(true);
            PeekButton peek = (PeekButton) peekButton.get(_inst);
            peek.render(sb);
        } catch (Exception e) {
            Logger.getLogger(HandCardSelectPatch.class.getName()).warning("Failed to render peek button in hand card select screen...");
        }
        AbstractDungeon.overlayMenu.combatDeckPanel.render(sb);
        AbstractDungeon.overlayMenu.discardPilePanel.render(sb);
        AbstractDungeon.overlayMenu.exhaustPanel.render(sb);
        return true;
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
    public static class CloseAllLogics {
        @SpirePostfixPatch
        public static void Postfix() {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT && OptFields.selectingBranch.get(AbstractDungeon.handCardSelectScreen)) {
                OptFields.selectingBranch.set(AbstractDungeon.handCardSelectScreen, false);
                cardChecked = false;
                branchesNum = 0;
            }
        }
    }

    @SpirePatch(clz = HandCardSelectScreen.class, method = "update")
    public static class ConfirmBranchingCheck {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(HandCardSelectScreen _inst) {
            AbstractCard card = _inst.selectedCards.group.get(0);
            if (IsSelectingBranch()) {
                Logger.getLogger(HandCardSelectPatch.class.getName()).info("final select branch:" + ((AbstractRhineCard) branches[0]).chosenBranch);
                if (!(card instanceof AbstractRhineCard)) return;
                ((AbstractRhineCard) card).chosenBranch = ((AbstractRhineCard) branches[0]).chosenBranch;
                OptFields.selectingBranch.set(_inst, false);
                cardChecked = false;
                branchesNum = 0;
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class,
                        "closeCurrentScreen");
                int[] totalTimes = LineFinder.findAllInOrder(ctMethodToPatch, matcher);
                int lastTime = totalTimes.length - 1;
                return new int[] {totalTimes[lastTime]};
            }
        }
    }
}
