package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.characters.RhineLab;

public class SummonStarRingAction extends AbstractGameAction {
    public final int strength;
    public final int block;
    public SummonStarRingAction(int amount) {
        this(amount, 0);
    }
    public SummonStarRingAction(int amount, int strength) {
        this(amount, strength, 0);
    }

    public SummonStarRingAction(int amount, int strength, int block) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
        this.strength = strength;
        this.block = block;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player instanceof RhineLab)
            ((RhineLab)AbstractDungeon.player).summonStarRing(amount, strength, block);
        isDone = true;
    }
}
