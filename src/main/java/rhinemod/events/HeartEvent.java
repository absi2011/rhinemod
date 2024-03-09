package rhinemod.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.events.city.Colosseum;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rhinemod.util.TheSky;

public class HeartEvent extends AbstractEvent {
    public static final String ID = "rhinemod:HeartEvent";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String ENC_NAME = "The Heart";
    private int screenNum = 0;
    public HeartEvent() {
        this.body = DESCRIPTIONS[0];
        if (AbstractDungeon.ascensionLevel == 20)
        {
            this.roomEventText.addDialogOption(OPTIONS[7], false);
            this.roomEventText.addDialogOption(OPTIONS[1], true);
        }
        else {
            this.roomEventText.addDialogOption(OPTIONS[0], true);
            this.roomEventText.addDialogOption(OPTIONS[8], false);
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
        this.hasDialog = true;
        this.hasFocus = true;
        noCardsInRewards = true;
        AbstractDungeon.getCurrRoom().rewardAllowed = false;
        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(ENC_NAME);
    }
    public void buttonEffect(int buttonPressed) {
        if (buttonPressed == 0) {
            if (screenNum == 5)
            {
                openMap();
            }
            else {
                screenNum++;
                this.roomEventText.updateBodyText(DESCRIPTIONS[screenNum]);
                this.roomEventText.updateDialogOption(0, OPTIONS[screenNum + 1]);
                if (screenNum == 1) {
                    this.roomEventText.removeDialogOption(1);
                }
            }
        }
        else {
            screenNum = 10;
            // A20, Fight!
            this.enterCombat();
            AbstractDungeon.lastCombatMetricKey = ENC_NAME;
        }
    }


    @Override
    public void reopen() {
        this.roomEventText.updateBodyText(DESCRIPTIONS[5]);
    }

}
