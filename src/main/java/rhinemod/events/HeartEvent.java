package rhinemod.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import rhinemod.relics.LoneTrail;

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
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
        this.hasDialog = true;
        this.hasFocus = true;
        noCardsInRewards = true;
        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem());
        AbstractDungeon.getCurrRoom().rewardAllowed = false;
        if (Settings.hasEmeraldKey && Settings.hasRubyKey && Settings.hasSapphireKey) {
            if (AbstractDungeon.player.hasRelic(LoneTrail.ID)) {
                if (AbstractDungeon.ascensionLevel >= 15) {
                    AbstractDungeon.player.getRelic(LoneTrail.ID).counter = 6;
                }
                else {
                    AbstractDungeon.player.getRelic(LoneTrail.ID).counter = 9;
                }
                AbstractDungeon.player.getRelic(LoneTrail.ID).flash();
            }
            screenNum = 0;
            this.roomEventText.addDialogOption(OPTIONS[0], false);
        }
        else if (AbstractDungeon.ascensionLevel == 20) {
            this.body = DESCRIPTIONS[1];
            screenNum = 1;
            this.roomEventText.addDialogOption(OPTIONS[1], false);
        } else {
            this.body = DESCRIPTIONS[1];
            screenNum = 1;
            this.roomEventText.addDialogOption(OPTIONS[0], false);
        }
        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(ENC_NAME);
    }
    public void buttonEffect(int buttonPressed) {
        if ((AbstractDungeon.ascensionLevel != 20) || (screenNum != 1)) {
            if (screenNum == 6) {
                openMap();
            } else {
                screenNum++;
                if ((screenNum == 3) || (screenNum == 4)) {
                    AbstractMonster heart = AbstractDungeon.getCurrRoom().monsters.monsters.get(0);
                    AbstractDungeon.effectList.add(new StrikeEffect(heart, heart.hb.cX, heart.hb.cY, 300));
                }
                if (screenNum == 5) {
                    AbstractMonster heart = AbstractDungeon.getCurrRoom().monsters.monsters.get(0);
                    AbstractDungeon.effectList.add(new StrikeEffect(heart, heart.hb.cX, heart.hb.cY, 300));
                    AbstractDungeon.getCurrRoom().monsters.monsters.clear();
                }
                this.roomEventText.updateBodyText(DESCRIPTIONS[screenNum]);
                if (AbstractDungeon.ascensionLevel == 20) {
                    this.roomEventText.updateDialogOption(0, OPTIONS[1]);
                }
                else if (screenNum != 6) {
                    this.roomEventText.updateDialogOption(0, OPTIONS[0]);
                }
                else {
                    this.roomEventText.updateDialogOption(0, OPTIONS[2]);
                }
            }
        } else {
            screenNum = 10;
            this.enterCombat();
            AbstractDungeon.lastCombatMetricKey = ENC_NAME;
        }
    }


    @Override
    public void reopen() {
        this.roomEventText.updateBodyText(DESCRIPTIONS[6]);
    }
}
