package rhinemod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.relics.LoneTrail;
import rhinemod.vfx.MyLaserEffect;

public class LaserAction extends AbstractGameAction {
    LoneTrail l;
    private final int counter;
    private float nextDuration;
    private int cnt;
    private final float tx;
    private final float ty;
    public LaserAction(AbstractMonster m, LoneTrail l, int counter) {
        actionType = ActionType.SPECIAL;
        this.l = l;
        this.counter = counter;
        duration = counter * 0.4F;
        nextDuration = duration;
        target = m;
        tx = m.hb.cX;
        ty = m.hb.cY;
        cnt = 0;
    }

    @Override
    public void update() {
        if (duration < nextDuration) {
            AbstractDungeon.effectList.add(0, new MyLaserEffect(l.hb.cX, l.hb.cY, tx, ty, cnt / (counter / 3)));
            target.damage(new DamageInfo(AbstractDungeon.player, 1, DamageInfo.DamageType.THORNS));
            cnt ++;
            nextDuration -= 0.4F;
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0F) {
            isDone = true;
        }
    }
}
