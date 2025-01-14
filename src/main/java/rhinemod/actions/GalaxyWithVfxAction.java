package rhinemod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.vfx.GalaxyEffect;

public class GalaxyWithVfxAction extends AbstractGameAction {
    private final int damage;
    public GalaxyWithVfxAction(AbstractMonster target, int damage) {
        duration = startDuration = 1.0F;
        this.target = target;
        this.damage = damage;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            AbstractDungeon.effectList.add(new GalaxyEffect());
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0F) {
            isDone = true;
            addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }
}
