package rhinemod.util;

import basemod.abstracts.CustomMultiPageFtue;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;

import java.util.logging.Logger;

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
        Logger.getLogger(RhineTipsFtue.class.getName()).info("rhine tips ftue init");
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.FTUE) {
            Logger.getLogger(RhineTipsFtue.class.getName()).info("screen correct(ftue)");
        } else {
            Logger.getLogger(RhineTipsFtue.class.getName()).info("screen incorrect(not ftue)");
        }
    }
}
