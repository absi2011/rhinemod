package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.*;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Stargate extends CustomRelic implements ClickableRelic {

    public static final String ID = "rhinemod:Stargate";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/Stargate.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/Stargate_p.png");
    public Stargate() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.MAGICAL);
        counter = 2;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Stargate();
    }

    @Override
    public void onRightClick() {
        if (counter <= 0) {
            return;
        }
        if (!isObtained || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SETTINGS || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMPLETE) {
            return;
        }
        if (AbstractDungeon.currMapNode == null) {
            return;
        }
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss || AbstractDungeon.getCurrRoom() instanceof TreasureRoomBoss || AbstractDungeon.getCurrRoom() instanceof VictoryRoom || AbstractDungeon.getCurrRoom() instanceof TrueVictoryRoom) {
            return;
        }
        counter--;
        if (counter == 0) {
            setCounter(-2);
            description = DESCRIPTIONS[1];
            tips.clear();
            tips.add(new PowerTip(name, description));
            initializeTips();
        }
        AbstractDungeon.combatRewardScreen.clear();
        AbstractDungeon.currMapNode.taken = true;
        MapRoomNode cur = AbstractDungeon.currMapNode;
        MapRoomNode nxt = new MapRoomNode(cur.x, cur.y);
        nxt.room = new ShopRoom();
        ArrayList<MapEdge> curEdges = cur.getEdges();
        for (MapEdge edge : curEdges)
            nxt.addEdge(edge);

        AbstractDungeon.previousScreen = null;
        AbstractDungeon.dynamicBanner.hide();
        AbstractDungeon.dungeonMapScreen.closeInstantly();
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.fadeIn();
        AbstractDungeon.effectList.clear();
        AbstractDungeon.topLevelEffects.clear();
        AbstractDungeon.topLevelEffectsQueue.clear();
        AbstractDungeon.effectsQueue.clear();
        AbstractDungeon.dungeonMapScreen.dismissable = true;
        AbstractDungeon.nextRoom = nxt;
        AbstractDungeon.setCurrMapNode(nxt);

        try {
            AbstractDungeon.getCurrRoom().onPlayerEntry();
        } catch (Exception e) {
            Logger.getLogger(Stargate.class.getSimpleName()).info("Error Occurred while entering.");
        }

        AbstractDungeon.scene.nextRoom(nxt.room);
        AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
    }
}
