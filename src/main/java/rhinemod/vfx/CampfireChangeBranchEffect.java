package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.patches.GridCardSelectPatch;
import rhinemod.util.ChangeBranchOption;

public class CampfireChangeBranchEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rhinemod:CampfireChangeBranchEffect");
    public static final String[] TEXT = uiStrings.TEXT;
    private boolean openedScreen = false;
    private final Color screenColor = AbstractDungeon.fadeColor.cpy();
    private ChangeBranchOption changeBranchOption;

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
                CardCrawlGame.metricData.addCampfireChoiceData("CHANGE_BRANCH", c.getMetricID());
                if (!(c instanceof AbstractRhineCard)) continue;
                ((AbstractRhineCard) c).swapBranch(((AbstractRhineCard) c).chosenBranch);
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                ChangeBranchOption.changeNum++;
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
            GridCardSelectPatch.OptFields.isChangingBranch.set(AbstractDungeon.gridSelectScreen, true);
            AbstractDungeon.gridSelectScreen.open(list, 1, TEXT[0], true, false, false, false);
        }
        
        if (duration < 0.0F) {
            isDone = true;
            if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
                CampfireUI campfireUI = ((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI;
                changeBranchOption.usable = false;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
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
