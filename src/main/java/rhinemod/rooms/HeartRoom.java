package rhinemod.rooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.SpireHeart;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rhinemod.events.HeartEvent;
import rhinemod.relics.LoneTrail;

public class HeartRoom extends AbstractRoom{
    public HeartRoom() {
        this.phase = RoomPhase.EVENT;
        this.mapSymbol = "?";
        this.mapImg = ImageMaster.MAP_NODE_EVENT;
        this.mapImgOutline = ImageMaster.MAP_NODE_EVENT_OUTLINE;
    }

    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        if (AbstractDungeon.player.getRelic(LoneTrail.ID).counter == 0) {
            Settings.hasSapphireKey = false;
            Settings.hasRubyKey = false;
            Settings.hasEmeraldKey = false;
            this.event = new SpireHeart();
        }
        else {
            this.event = new HeartEvent();
        }
        this.event.onEnterRoom();
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp) {
            this.event.update();
        }

        if (this.event.waitTimer == 0.0F && !this.event.hasFocus && this.phase != RoomPhase.COMBAT) {
            this.phase = RoomPhase.COMPLETE;
            this.event.reopen();
        }
    }

    public void render(SpriteBatch sb) {
        if (this.event != null) {
            this.event.renderRoomEventPanel(sb);
            this.event.render(sb);
        }

        super.render(sb);
    }

    public void renderAboveTopPanel(SpriteBatch sb) {
        super.renderAboveTopPanel(sb);
        if (this.event != null) {
            this.event.renderAboveTopPanel(sb);
        }
    }

    public void addPotionToRewards() {
    }

    /* Copied from ArenaRoom */
    // 没看懂，抄一遍
    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class RenderPatch {
        @SpireInsertPatch(rloc = 36)
        public static void Insert(AbstractDungeon __instance, SpriteBatch sb) {
            if (AbstractDungeon.getCurrRoom() instanceof HeartRoom) {
                AbstractDungeon.getCurrRoom().renderEventTexts(sb);
            }
        }
    }

}


