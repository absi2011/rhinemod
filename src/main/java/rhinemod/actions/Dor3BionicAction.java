package rhinemod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

public class Dor3BionicAction extends AbstractGameAction {
    private static final float START_X = 64.0F * Settings.scale;
    private static final float START_Y = Settings.isMobile ? (float)Settings.HEIGHT - 132.0F * Settings.scale : (float)Settings.HEIGHT - 102.0F * Settings.scale;
    private static final float PAD_X = 72.0F * Settings.scale;
    public AbstractRelic replacedRelic;
    public AbstractRelic aimRelic;
    public AbstractMonster aimMonster;
    public int stage;
    public Dor3BionicAction(AbstractRelic replacedRelic, AbstractRelic aimRelic, AbstractMonster aimMonster) {
        actionType = ActionType.SPECIAL;
        duration = 0.3F;
        this.replacedRelic = replacedRelic;
        this.aimRelic = aimRelic;
        this.aimMonster = aimMonster;
        stage = 0;
    }

    @Override
    public void update() {
        if (stage == 0) {
            stage = 1;
            replacedRelic.targetX = aimMonster.drawX;
            replacedRelic.targetY = aimMonster.drawY;
            replacedRelic.isDone = false;
        } else if (stage == 1) {
            if (!replacedRelic.isDone) return;
            stage = 2;
            aimMonster.hideHealthBar();
            aimMonster.intentAlpha = 0.0F;
            aimMonster.tint.fadeOut();
            aimMonster.isEscaping = true;
            aimRelic.isObtained = true;
            aimRelic.currentX = replacedRelic.currentX;
            aimRelic.currentY = replacedRelic.currentY;
        } else if (stage == 2) {
            duration -= Gdx.graphics.getDeltaTime();
            if (duration > 0.0F) return;
            stage = 3;
            AbstractPlayer p = AbstractDungeon.player;
            int slot = p.relics.indexOf(replacedRelic);
            AbstractDungeon.player.relics.set(slot, aimRelic);
            aimRelic.targetX = START_X + slot * PAD_X;
            aimRelic.targetY = START_Y;
            aimRelic.hb.move(aimRelic.currentX, aimRelic.currentY);
            UnlockTracker.markRelicAsSeen(aimRelic.relicId);
            aimRelic.getUpdatedDescription();
            if (AbstractDungeon.topPanel != null) {
                AbstractDungeon.topPanel.adjustRelicHbs();
            }
        } else {
            if (!aimRelic.isDone) return;
            aimMonster.escaped = true;
            if (AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.getCurrRoom().isBattleOver) {
                // 能抓的时候应该不会有其他人导致cannotLose吧
                AbstractDungeon.getCurrRoom().endBattle();
            }
            isDone = true;
        }
    }
}
