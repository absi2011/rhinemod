package rhinemod.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;

public class AbstractCharacterSpine {
    protected TextureAtlas atlas;
    protected Skeleton skeleton;
    public AnimationState state;
    protected AnimationStateData stateData;
    AbstractPlayer p;
    public float deltaX;
    public String idle;
    public String attack;
    public AbstractCharacterSpine(AbstractPlayer p, float deltaX, String atlasUrl, String skeletonUrl, float scale, String idle, String attack) {
        this.p = p;
        this.deltaX = deltaX;
        atlas = new TextureAtlas(Gdx. files.internal(atlasUrl));
        SkeletonJson json = new SkeletonJson(atlas);
        json.setScale(Settings.renderScale / scale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
        skeleton = new Skeleton(skeletonData);
        skeleton.setColor(Color.WHITE);
        stateData = new AnimationStateData(skeletonData);
        state = new AnimationState(stateData);
        this.idle = idle;
        this.attack = attack;
        stateData.setMix(idle, "Die", 0.1F);
        state.setAnimation(0, idle, true);
    }

    public void dispose() {
        if (atlas != null) atlas.dispose();
    }

    public void setAttack() {
        state.setAnimation(0, attack, false);
        state.addAnimation(0, idle, true, 0);
    }

    public void render(SpriteBatch sb) {
        if (atlas != null) {
            state.update(Gdx.graphics.getDeltaTime());
            state.apply(skeleton);
            skeleton.updateWorldTransform();
            skeleton.setPosition(p.drawX + p.animX + deltaX, p.drawY + p.animY);
            skeleton.setColor(p.tint.color);
            skeleton.setFlip(p.flipHorizontal, p.flipVertical);
            sb.end();
            CardCrawlGame.psb.begin();
            AbstractPlayer.sr.draw(CardCrawlGame.psb, skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
        }
    }
}
