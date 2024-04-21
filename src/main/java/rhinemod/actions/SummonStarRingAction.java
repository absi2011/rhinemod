package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.characters.RhineLab;

public class SummonStarRingAction extends AbstractGameAction {
    public final int strength;
    public final int block;
    public final int blast;
    public SummonStarRingAction(int amount) {
        this(amount, 0);
    }
    public SummonStarRingAction(int amount, int strength) {
        this(amount, strength, 0);
    }
    public SummonStarRingAction(int amount, int strength, int block) {
        this(amount, strength, block, 0);
    }


    public SummonStarRingAction(int amount, int strength, int block, int blast) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
        this.strength = strength;
        this.block = block;
        this.blast = blast;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player instanceof RhineLab)
            ((RhineLab)AbstractDungeon.player).summonStarRing(amount, strength, block, blast);
        isDone = true;
    }
}
