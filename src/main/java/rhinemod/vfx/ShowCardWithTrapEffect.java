package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;
import rhinemod.patches.DorothyResonatorPatch;

public class ShowCardWithTrapEffect extends AbstractGameEffect {
    private final AbstractCard card;

    public ShowCardWithTrapEffect(AbstractCard srcCard, float x, float y) {
        card = srcCard.makeStatEquivalentCopy();
        DorothyResonatorPatch.OptFields.trapped.set(card, true);
        duration = 1.5F;
        card.target_x = x;
        card.target_y = y;

        AbstractDungeon.effectsQueue.add(new CardPoofEffect(card.target_x, card.target_y));
        card.drawScale = 0.01F;
        card.targetDrawScale = 1.0F;
    }

    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        card.update();
        if (duration < 0.0F) {
            isDone = true;
            card.shrink();
        }
    }

    public void render(SpriteBatch sb) {
        if (!isDone) {
            card.render(sb);
        }
    }

    public void dispose() {}
}
