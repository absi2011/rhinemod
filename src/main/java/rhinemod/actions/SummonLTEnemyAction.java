//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package rhinemod.actions;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rhinemod.monsters.*;

import java.util.ArrayList;

public class SummonLTEnemyAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(SummonLTEnemyAction.class.getName());
    private AbstractMonster m;

    public SummonLTEnemyAction(AbstractMonster[] allies, boolean FromTurnpike) {
        this.actionType = ActionType.SPECIAL;
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }

        this.duration = this.startDuration;
        int slot = this.identifySlot(allies);
        if (slot == -1) {
            logger.info("INCORRECTLY ATTEMPTED TO CHANNEL LONE TRAIL ALLY.");
        } else {
            this.m = this.getRandomAlly(slot, FromTurnpike);
            allies[slot] = this.m;
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onSpawnMonster(this.m);
        }
    }

    private int identifySlot(AbstractMonster[] allies) {
        for(int i = 0; i < allies.length; ++i) {
            if (allies[i] == null || allies[i].isDying) {
                return i;
            }
        }

        return -1;
    }

    private AbstractMonster getRandomAlly(int slot, boolean FromTurnpike) {
        ArrayList<String> pool = new ArrayList<String>();
        pool.add(ArclightCommando.ID);
        pool.add(ArclightMirrorguard.ID);
        pool.add(ArclightVanguard.ID);
        pool.add(Crossroads.ID);
        pool.add(MilitaryBeckbeast.ID);
        pool.add(Perpetrator.ID);
        pool.add(TrimountsCityGuard.ID);
        float x;
        float y;
        // 抄的地精首领的
        if (slot == 0) {
            x = -366.0F;
            y = -4.0F;
        }
        else if (slot == 1) {
            x = -170.0F;
            y = 6.0F;
        }
        else if (slot == 2) {
            x = -532.0F;
            y = 0.0F;
        }
        else {
            x = 0.0F;
            y = 0.0F;
        }
        String monsterName;
        if (!FromTurnpike) {
            y += 200; // 和交通亭的做区分
            monsterName = pool.get(AbstractDungeon.aiRng.random(0, pool.size() - 1));
        }
        else {
            monsterName = TrimountsCityGuard.ID;
        }
        AbstractMonster m;
        if (monsterName.equals(ArclightCommando.ID)) {
            m = new ArclightCommando(x,y);
        }
        else if (monsterName.equals(ArclightVanguard.ID)) {
            m = new ArclightVanguard(x,y);
        }
        else if (monsterName.equals(Crossroads.ID)) {
            m = new Crossroads(x,y);
        }
        else if (monsterName.equals(Perpetrator.ID)) {
            m = new Perpetrator(x,y);
        }
        else if (monsterName.equals(ArclightMirrorguard.ID)) {
            m = new ArclightMirrorguard(x,y);
        }
        else if (monsterName.equals(TrimountsCityGuard.ID)) {
            m = new TrimountsCityGuard(x,y);
        }
        else if (monsterName.equals(MilitaryBeckbeast.ID)) {
            m = new MilitaryBeckbeast(x,y);
        }
        else {
            m = null;
        }
        return m;
    }

    private int getSmartPosition() {
        int position = 0;

        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!(this.m.drawX > mo.drawX)) {
                break;
            }
            ++position;
        }

        return position;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            this.m.animX = 1200.0F * Settings.xScale;
            this.m.init();
            this.m.applyPowers();
            AbstractDungeon.getCurrRoom().monsters.addMonster(this.getSmartPosition(), this.m);
            if (ModHelper.isModEnabled("Lethality")) {
                this.addToBot(new ApplyPowerAction(this.m, this.m, new StrengthPower(this.m, 3), 3));
            }

            if (ModHelper.isModEnabled("Time Dilation")) {
                this.addToBot(new ApplyPowerAction(this.m, this.m, new SlowPower(this.m, 0)));
            }

        }

        this.tickDuration();
        if (this.isDone) {
            this.m.animX = 0.0F;
            this.m.showHealthBar();
            this.m.usePreBattleAction();
        } else {
            this.m.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
        }

    }
}
