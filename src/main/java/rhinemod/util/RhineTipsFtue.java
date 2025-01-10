package rhinemod.util;

import basemod.abstracts.CustomMultiPageFtue;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.localization.TutorialStrings;

public class RhineTipsFtue extends CustomMultiPageFtue {
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString("rhinemod:RhineTips");
    public static final String[] TEXT = tutorialStrings.TEXT;
    public static final Texture[] IMAGES = new Texture[] {
            new Texture("resources/rhinemod/images/ui/tips/t1.png"),
            new Texture("resources/rhinemod/images/ui/tips/t2.png"),
            new Texture("resources/rhinemod/images/ui/tips/t3.png"),
            new Texture("resources/rhinemod/images/ui/tips/t4.png"),
            new Texture("resources/rhinemod/images/ui/tips/t5.png")
    };

    public RhineTipsFtue() {
        super(IMAGES, TEXT);
    }

    @SpirePatch(clz = TipTracker.class, method = "refresh")
    public static class TipTrackerPatch {
        @SpirePostfixPatch
        public static void Postfix() {
            TipTracker.tips.put("RHINE_COMBAT_TIP", TipTracker.pref.getBoolean("RHINE_COMBAT_TIP", false));
        }
    }
}
