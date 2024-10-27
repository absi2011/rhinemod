package rhinemod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rhinemod.cards.AbstractRhineCard;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

public class SingleCardViewBranchPatch {
    static public final int MAX_BRANCH = 4;
    public static AbstractCard[] branches = new AbstractCard[MAX_BRANCH];
    public static Hitbox[] hitboxes = new Hitbox[MAX_BRANCH];
    public static int branchesNum;
    public static boolean cardChecked;
    public static AbstractCard originCard;
    private static boolean wasViewingUpgrade;

    public static void setBranchesPreview(SingleCardViewPopup _inst, AbstractRhineCard card) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!cardChecked) {
            AbstractRhineCard _card = card.makeStatEquivalentCopy();
            if (_card.upgraded) _card.resetUpgrade();
            branchesNum = _card.possibleBranches().size();
            for (int i = 0; i < branchesNum; i++) {
                AbstractRhineCard preview = _card.makeStatEquivalentCopy();
                preview.chosenBranch = i;
                preview.possibleBranches().get(i).upgrade();
                preview.displayUpgrades();
                branches[i] = preview;
                hitboxes[i] = new Hitbox(0, 0);
            }
            if (card.chosenBranch != 0) Collections.swap(Arrays.asList(branches), 0, card.chosenBranch);
            cardChecked = true;
        }
        updateBranchPreview(_inst);
    }

    private static void setPreviewCard(AbstractCard c) {
        try {
            Field card = SingleCardViewPopup.class.getDeclaredField("card");
            card.setAccessible(true);
            card.set(CardCrawlGame.cardPopup, c.makeStatEquivalentCopy());
        } catch (Exception ignored) {
        }
    }

    public static void updateBranchPreview(SingleCardViewPopup _inst) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setPreviewCard(branches[0]);
        for (int i = 1; i < branchesNum; i++) {
            branches[i].drawScale = 0.75F;
            branches[i].current_x = Settings.WIDTH * 0.92F;
            branches[i].current_y = Settings.HEIGHT * (0.8F - (i - 1) * 0.3F);
            branches[i].target_x = Settings.WIDTH * 0.9F;
            branches[i].target_y = Settings.HEIGHT * (0.8F - (i - 1) * 0.3F);
            branches[i].transparency = 1F;
            branches[i].targetTransparency = 1F;
            hitboxes[i].move(branches[i].current_x, branches[i].current_y);
            hitboxes[i].resize(300.0F * Settings.scale * 0.75F, 420.0F * Settings.scale * 0.75F);
        }
        Method method = SingleCardViewPopup.class.getDeclaredMethod("loadPortraitImg");
        method.setAccessible(true);
        method.invoke(_inst);
    }

    public static boolean isCardHasBranches() {
        if (!(originCard instanceof AbstractRhineCard)) return false;
        return ((AbstractRhineCard) originCard).possibleBranches().size() > 1 && SingleCardViewPopup.isViewingUpgrade;
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "update")
    public static class CheckIfCardHasBranches {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup _inst) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
            if (isCardHasBranches()) {
                setBranchesPreview(_inst, (AbstractRhineCard) originCard);
            } else {
                if (!SingleCardViewPopup.isViewingUpgrade && wasViewingUpgrade) {
                    setPreviewCard(originCard);
                }
                branchesNum = 0;
                cardChecked = false;
            }
            wasViewingUpgrade = SingleCardViewPopup.isViewingUpgrade;
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "updateInput")
    public static class UpdateInputButtons {
        @SpirePrefixPatch
        public static void Prefix(SingleCardViewPopup _inst) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
            if (isCardHasBranches()) {
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
                        updateBranchPreview(_inst);
                    }
                }
            }
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "updateInput")
    public static class SecureBranchesHitboxes {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() throws Exception {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    int line = 291;
                    try {
                        Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "close");
                        line = LineFinder.findAllInOrder(m.where(), matcher)[0];
                    } catch (Exception ignored) {}
                    if (m.getMethodName().equals("close") && m.getLineNumber() == line) {
                        m.replace("if (" + SingleCardViewBranchPatch.class.getName() + ".BranchesUnhovered(this)) $_ = $proceed($$);");
                    }
                }
            };
        }
    }

    public static boolean BranchesUnhovered(SingleCardViewPopup _inst) {
        for (int i = 1; i < branchesNum; i++)
            if (hitboxes[i].hovered)
                return false;
        return true;
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "render")
    public static class RenderingBranches {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(SingleCardViewPopup _inst, SpriteBatch sb) {
            if (isCardHasBranches()) {
                for (int i = 1; i < branchesNum; i++) {
                    branches[i].render(sb);
                    branches[i].updateHoverLogic();
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderCardBack");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "close")
    public static class CloseAllLogics {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup _inst) {
            cardChecked = false;
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "open", paramtypez = {AbstractCard.class, CardGroup.class})
    public static class OpenPatch {
        @SpirePrefixPatch
        public static void Prefix(SingleCardViewPopup _inst, AbstractCard card, CardGroup group) {
            originCard = card.makeStatEquivalentCopy();
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "open", paramtypez = {AbstractCard.class})
    public static class OpenPatch2 {
        @SpirePrefixPatch
        public static void Prefix(SingleCardViewPopup _inst, AbstractCard card) {
            originCard = card.makeStatEquivalentCopy();
        }
    }
}
