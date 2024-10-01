package rhinemod.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class InvisibleSmokeEffect extends AbstractGameEffect {
    private final ArrayList<InvisibleSmokeBlurEffect> blurList;

    public InvisibleSmokeEffect(float x, float y) {
        blurList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            InvisibleSmokeBlurEffect e = new InvisibleSmokeBlurEffect(x, y);
            blurList.add(e);
            AbstractDungeon.effectsQueue.add(e);
        }
    }

    public void endEffect() {
        isDone = true;
        for (InvisibleSmokeBlurEffect e : blurList) e.disposing = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {}

    @Override
    public void dispose() {}
}
