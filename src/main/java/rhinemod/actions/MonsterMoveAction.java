package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ShowMoveNameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.characters.RhineLab;
import rhinemod.powers.PlanetaryDebrisPower;
import rhinemod.powers.Stunned;
import rhinemod.util.GlobalAttributes;

public class MonsterMoveAction extends AbstractGameAction {

    public MonsterMoveAction(AbstractMonster monster, int waiting) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.target = monster;
        this.amount = waiting;
    }

    @Override
    public void update() {
        if (amount == 0) {
            addToBot(new ShowMoveNameAction((AbstractMonster)target));
            ((AbstractMonster)target).flashIntent();
            ((AbstractMonster)target).takeTurn();
            ((AbstractMonster)target).createIntent();
            target.applyTurnPowers();
            addToBot(new WaitAction(0.5F));
        }
        else {
            addToBot(new MonsterMoveAction((AbstractMonster) target, amount - 1));
        }
        isDone = true;
    }
}
