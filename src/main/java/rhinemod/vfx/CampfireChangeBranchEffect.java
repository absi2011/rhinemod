package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.patches.GridCardSelectPatch;
import rhinemod.util.ChangeBranchOption;

import java.util.ArrayList;
import java.util.logging.Logger;

public class CampfireChangeBranchEffect extends AbstractGameEffect {
    public static final String EFFECT_TEXT = ChangeBranchOption.TEXT[3];
    public static ArrayList<String> ids = new ArrayList<>();
    private boolean openedScreen = false;
    private final Color screenColor = AbstractDungeon.fadeColor.cpy();
    private final ChangeBranchOption changeBranchOption;

    public CampfireChangeBranchEffect(ChangeBranchOption c) {
        duration = 1.5F;
        screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        changeBranchOption = c;
    }

    @Override
    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            duration -= Gdx.graphics.getDeltaTime();
            updateBlackScreenColor();
        }

        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && AbstractDungeon.gridSelectScreen.forUpgrade) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (!(c instanceof AbstractRhineCard)) continue;
                Logger.getLogger(CampfireChangeBranchEffect.class.getName()).info("Change branch at campfire, " + c.cardID + " -> " + ((AbstractRhineCard) c).chosenBranch);
                ((AbstractRhineCard) c).swapBranch(((AbstractRhineCard) c).chosenBranch);
                String metricID = c.getMetricID();
                ids.add(metricID.substring(0, metricID.length() - 1) + GridCardSelectPatch.formerBranch);
                ids.add(metricID);
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                changeBranchOption.changeNum++;
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            ((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
        }

        if (duration < 1.0F && !openedScreen) {
            openedScreen = true;
            CardGroup list = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
                if (c instanceof AbstractRhineCard && c.upgraded && ((AbstractRhineCard) c).possibleBranches().size() > 1)
                    list.group.add(c);
            AbstractDungeon.gridSelectScreen.open(list, 1, EFFECT_TEXT, true, false, true, false);
        }

        if (duration < 0.0F) {
            isDone = true;
            if (CampfireUI.hidden) {
                CampfireUI campfireUI = ((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI;
                if (changeBranchOption.changeNum > 0)
                    changeBranchOption.usable = false;
                campfireUI.reopen();
            }
        }
    }

    private void updateBlackScreenColor() {
        if (duration > 1.0F) {
            screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (duration - 1.0F) * 2.0F);
        } else {
            screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, duration / 1.5F);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID)
            AbstractDungeon.gridSelectScreen.render(sb);
    }

    @Override
    public void dispose() {}
}
