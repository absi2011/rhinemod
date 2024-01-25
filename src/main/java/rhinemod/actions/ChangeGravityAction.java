package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.characters.RhineLab;
import rhinemod.powers.PlanetaryDebrisPower;
import rhinemod.powers.Stunned;
import rhinemod.util.GlobalAttributes;

public class ChangeGravityAction extends AbstractGameAction {
    public GlobalAttributes.GravityDirection direction;
    public ChangeGravityAction() {
        this(GlobalAttributes.GravityDirection.UNKNOWN);
    }

    public ChangeGravityAction(GlobalAttributes.GravityDirection direction) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.direction = direction;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (!(p instanceof RhineLab)) {
            isDone = true;
            return;
        }
        ((RhineLab) p).globalAttributes.changeGravity(direction);
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
            if (!m.isDeadOrEscaped())
                if (m.hasPower(Stunned.POWER_ID))
                    ((Stunned)m.getPower(Stunned.POWER_ID)).onGravityChange();

        if (p.hasPower(PlanetaryDebrisPower.POWER_ID)) {
            ((PlanetaryDebrisPower)p.getPower(PlanetaryDebrisPower.POWER_ID)).onGravityChange();
        }
        isDone = true;
    }
}
