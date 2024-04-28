package rhinemod.rooms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.Mushrooms;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.Legend;
import com.megacrit.cardcrawl.map.LegendItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;
import rhinemod.events.HeartEvent;

public class HeartRoom extends AbstractRoom{
    public HeartRoom() {
        this.phase = RoomPhase.EVENT;
        this.mapSymbol = "?";
        this.mapImg = ImageMaster.MAP_NODE_EVENT;
        this.mapImgOutline = ImageMaster.MAP_NODE_EVENT_OUTLINE;
    }

    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.event = new HeartEvent();
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


