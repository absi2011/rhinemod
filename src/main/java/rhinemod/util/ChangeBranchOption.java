package rhinemod.util;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import rhinemod.vfx.CampfireChangeBranchEffect;

public class ChangeBranchOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rhinemod:ChangeBranchOption");
    public static final String[] TEXT = uiStrings.TEXT;
    public int changeNum;

    public ChangeBranchOption(boolean active) {
        changeNum = 0;
        label = TEXT[0];
        usable = active;
        updateUsability(active);
    }

    public void updateUsability(boolean canUse) {
        description = canUse? TEXT[1] : TEXT[2];
        img = ImageMaster.loadImage("resources/rhinemod/images/ui/ChangeBranch.png");
    }

    public void useOption() {
        if (usable)
            AbstractDungeon.effectList.add(new CampfireChangeBranchEffect(this));
    }
}
