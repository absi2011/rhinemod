package rhinemod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.patches.DorothyResonatorPatch;
import rhinemod.vfx.ShowCardWithTrapEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SetTrapAction extends AbstractGameAction {
    public SetTrapAction(int amount) {
        actionType = ActionType.SPECIAL;
        this.amount = amount;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> lst = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.drawPile.group)
            if (!DorothyResonatorPatch.OptFields.trapped.get(c))
                lst.add(c);
        Collections.shuffle(lst, new Random(AbstractDungeon.cardRng.randomLong()));
        amount = Math.min(amount, lst.size());
        float PADDING = 25.0F * Settings.scale;
        if (amount == 1) {
            DorothyResonatorPatch.OptFields.trapped.set(lst.get(0), true);
            AbstractDungeon.effectList.add(new ShowCardWithTrapEffect(lst.get(0), Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH * 0.5F), Settings.HEIGHT / 2.0F));
        } else if (amount == 2) {
            DorothyResonatorPatch.OptFields.trapped.set(lst.get(0), true);
            DorothyResonatorPatch.OptFields.trapped.set(lst.get(1), true);
            AbstractDungeon.effectList.add(new ShowCardWithTrapEffect(lst.get(0), Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
            AbstractDungeon.effectList.add(new ShowCardWithTrapEffect(lst.get(1), Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), Settings.HEIGHT / 2.0F));
        } else {
            for (int i = 0; i < amount; i++) {
                DorothyResonatorPatch.OptFields.trapped.set(lst.get(i), true);
                AbstractDungeon.effectList.add(new ShowCardWithTrapEffect(lst.get(i), MathUtils.random(Settings.WIDTH * 0.2F, Settings.WIDTH * 0.8F), MathUtils.random(Settings.HEIGHT * 0.3F, Settings.HEIGHT * 0.7F)));
            }
        }
        isDone = true;
    }
}
