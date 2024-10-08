package rhinemod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.scenes.AbstractScene;

public class TheSkyScene extends AbstractScene {
    public TheSkyScene() {
        super("resources/rhinemod/images/ui/SkyScene/scene.atlas");
        ambianceName = "AMBIANCE_BEYOND";
        fadeInAmbiance();
    }

    @Override
    public void nextRoom(AbstractRoom room) {
        super.nextRoom(room);
        randomizeScene();
        fadeInAmbiance();
    }

    @Override
    public void renderCombatRoomBg(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        renderAtlasRegionIf(sb, bg, true);
    }

    @Override
    public void renderCombatRoomFg(SpriteBatch sb) {}

    @Override
    public void renderCampfireRoom(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        renderAtlasRegionIf(sb, this.campfireBg, true);
    }

    @Override
    public void randomizeScene() {}
}
