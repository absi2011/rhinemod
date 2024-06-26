package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import rhinemod.cards.special.Loner;

public class PHAAction extends AbstractGameAction {
    int damage;
    public PHAAction(AbstractCreature p, AbstractCreature m, int damage) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
        this.source = p;
        this.target = m;
        this.damage = damage;
    }

    @Override
    public void update() {
        this.target.damage(new DamageInfo(source, damage));
        if (target.lastDamageTaken >= 15)
        {
            addToBot(new MakeTempCardInHandAction(new Loner()));
        }
        isDone = true;
    }
}
