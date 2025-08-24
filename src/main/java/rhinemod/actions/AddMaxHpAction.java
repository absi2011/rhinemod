package rhinemod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import rhinemod.RhineMod;

public class AddMaxHpAction extends AbstractGameAction {
    public AddMaxHpAction(AbstractCreature target) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.target = target;
    }

    @Override
    public void update() {
        int amount = (int)(target.maxHealth * 0.1F * RhineMod.tagLevel);
        float percent = (float)target.currentHealth / target.maxHealth;
        target.maxHealth += amount;
        AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(target.hb.cX - target.animX, target.hb.cY, AbstractCreature.TEXT[2] + Integer.toString(amount), Settings.GREEN_TEXT_COLOR));
        target.heal(MathUtils.floor(amount * percent), true);
        target.healthBarUpdatedEvent();
        isDone = true;
    }
}
