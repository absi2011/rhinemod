package rhinemod.events;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import rhinemod.relics.CalcareousStamp;
import rhinemod.relics.Imperishable;
import rhinemod.relics.WaterdropManifestation;
import rhinemod.util.MyDialogOptionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class StarlitNight extends AbstractEvent {
    public static final String ID = "rhinemod:StarlitNight";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private CurScreen screen = CurScreen.INTRO;
    public final ArrayList<AbstractRelic> relics = new ArrayList<>(Arrays.asList(new Imperishable(), new WaterdropManifestation(), new CalcareousStamp()));
    public final ArrayList<String> choices = new ArrayList<>(Arrays.asList("Star", "Saria", "Stone"));
    private enum CurScreen {
        INTRO, LEAVE
    }
    public StarlitNight() {
        initializeImage("resources/rhinemod/images/event/StarlitNight.png", 0, 0);
        RoomEventDialog.optionList.clear();
        body = DESCRIPTIONS[0];
        hasDialog = true;
        hasFocus = true;
        noCardsInRewards = true;
        for (int i = 0; i < 3; i++) {
            MyDialogOptionButton btn = new MyDialogOptionButton(i, OPTIONS[i], false, null, relics.get(i).makeCopy());
            RoomEventDialog.optionList.add(btn);
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        if (screen == CurScreen.INTRO) {
            AbstractRelic r = relics.get(buttonPressed).makeCopy();
            roomEventText.updateBodyText(DESCRIPTIONS[buttonPressed + 1]);
            screen = CurScreen.LEAVE;
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, r);
            LargeDialogOptionButton btn = RoomEventDialog.optionList.get(0);
            btn.msg = OPTIONS[3];
            if (btn instanceof MyDialogOptionButton) {
                ((MyDialogOptionButton) btn).relic = null;
            }
            roomEventText.clearRemainingOptions();
            AbstractEvent.logMetricObtainRelic(ID, choices.get(buttonPressed), r);
        } else {
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.dungeonMapScreen.open(false);
        }
    }

    @Override
    public void renderRoomEventPanel(SpriteBatch sb) {
        sb.setColor(new Color(0.0F, 0.0F, 0.0F, panelAlpha));
        sb.draw(ImageMaster.EVENT_ROOM_PANEL, 0.0F, Settings.HEIGHT - 375.0F * Settings.scale, Settings.WIDTH, 300.0F * Settings.scale);
    }

    @SpirePatch(clz = RoomEventDialog.class, method = "render")
    public static class RenderWordsPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(RoomEventDialog _inst, SpriteBatch sb, ArrayList<DialogWord> ___words) {
            if (AbstractDungeon.getCurrRoom().event instanceof StarlitNight) {
                AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
                if (!AbstractDungeon.player.isDead) {
                    for (DialogWord w : ___words)
                        w.render(sb, Settings.HEIGHT - 425.0F * Settings.scale);
                    for (LargeDialogOptionButton b : RoomEventDialog.optionList)
                        b.render(sb);
                    for (LargeDialogOptionButton b : RoomEventDialog.optionList)
                        b.renderRelicPreview(sb);
                }
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }
}
