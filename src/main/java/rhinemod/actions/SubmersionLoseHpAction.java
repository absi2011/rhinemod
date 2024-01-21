package rhinemod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class SubmersionLoseHpAction extends AbstractGameAction {
    public final AbstractPlayer p = AbstractDungeon.player;
    public SubmersionLoseHpAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            isDone = true;
            return;
        }
        if (duration == Settings.ACTION_DUR_FAST && target.currentHealth > 0) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.POISON));
        }
        tickDuration();
        if (isDone) {
            if (target.currentHealth > 0) {
                target.tint.color = Color.SKY.cpy();
                target.tint.changeColor(Color.WHITE.cpy());
                target.damage(new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS));
            }
            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}
