package rhinemod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.monsters.AbstractRhineMonster;
import rhinemod.relics.LoneTrail;
import rhinemod.vfx.MyLaserEffect;

public class LaserAction extends AbstractGameAction {
    LoneTrail l;
    private final int counter;
    private float nextDuration;
    private int cnt;
    public LaserAction(AbstractMonster m, LoneTrail l, int counter) {
        actionType = ActionType.SPECIAL;
        this.l = l;
        this.counter = counter;
        duration = counter * 0.5F;
        nextDuration = duration;
        target = m;
        cnt = 0;
    }

    @Override
    public void update() {
        if (duration < nextDuration) {
            AbstractDungeon.effectList.add(new MyLaserEffect(l.hb.cX, l.hb.cY, target.hb.cX, target.hb.cY, cnt / (counter / 3)));
            target.damage(new DamageInfo(AbstractDungeon.player, 1, DamageInfo.DamageType.THORNS));
            cnt ++;
            nextDuration -= 0.5F;
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0F) {
            isDone = true;
        }
    }
}
