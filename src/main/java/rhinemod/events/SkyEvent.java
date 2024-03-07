package rhinemod.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rhinemod.util.TheSky;

public class SkyEvent extends AbstractImageEvent {
    public static final String ID = "rhinemod:SkyEvent";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    public SkyEvent() {
        super(NAME, DESCRIPTIONS[0], "images/ui/SkyScene/scene.png");
        imageEventText.setDialogOption(OPTIONS[0]);
    }

    public void buttonEffect(int buttonPressed) {
        imageEventText.clear();
        hasFocus = false;
        CardCrawlGame.mode = CardCrawlGame.GameMode.GAMEPLAY;
        CardCrawlGame.nextDungeon = TheSky.ID;
        CardCrawlGame.music.fadeOutBGM();
        CardCrawlGame.music.fadeOutTempBGM();
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.fadeOut();
        AbstractDungeon.isDungeonBeaten = true;
    }
}
