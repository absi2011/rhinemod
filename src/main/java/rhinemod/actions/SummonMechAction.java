//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package rhinemod.actions;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rhinemod.monsters.*;

public class SummonMechAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(SummonMechAction.class.getName());
    private AbstractMonster m;

    public SummonMechAction(AbstractMonster[] mechs) {
        this.actionType = ActionType.SPECIAL;
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }

        this.duration = this.startDuration;
        int slot = this.identifySlot(mechs);
        if (slot == -1) {
            logger.info("INCORRECTLY ATTEMPTED TO CHANNEL MECH.");
        } else {
            this.m = this.getRandomMech(slot);
            mechs[slot] = this.m;
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onSpawnMonster(this.m);
        }
    }

    private int identifySlot(AbstractMonster[] mechs) {
        for(int i = 0; i < mechs.length; ++i) {
            if (mechs[i] == null || mechs[i].isDying) {
                return i;
            }
        }

        return -1;
    }

    private AbstractMonster getRandomMech(int slot) {
        ArrayList<String> pool = new ArrayList<String>();
        pool.add(R11AssaultPowerArmor.ID);
        pool.add(R31HeavyPowerArmor.ID);
        pool.add(Crossroads.ID);
        pool.add(Crossroads.ID);
        pool.add(ExperimentalPowerArmor.ID);
        pool.add(ExperimentalPowerArmor.ID);
        pool.add(Perpetrator.ID);
        pool.add(Perpetrator.ID);
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
        String monsterName = pool.get(AbstractDungeon.aiRng.random(0, pool.size() - 1));
        AbstractMonster m;
        if (monsterName.equals(R11AssaultPowerArmor.ID)) {
            m = new R11AssaultPowerArmor(x,y);
        }
        else if (monsterName.equals(R31HeavyPowerArmor.ID)) {
            m = new R31HeavyPowerArmor(x,y);
        }
        else if (monsterName.equals(Crossroads.ID)) {
            m = new Crossroads(x,y);
        }
        else if (monsterName.equals(Perpetrator.ID)) {
            m = new Perpetrator(x,y);
        }
        else if (monsterName.equals(ExperimentalPowerArmor.ID)) {
            m = new ExperimentalPowerArmor(x,y);
        }
        else {
            m = null;
        }
        return m;
    }

    private int getSmartPosition() {
        int position = 0;

        for(Iterator var2 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator(); var2.hasNext(); ++position) {
            AbstractMonster mo = (AbstractMonster)var2.next();
            if (!(this.m.drawX > mo.drawX)) {
                break;
            }
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

            this.addToBot(new ApplyPowerAction(this.m, this.m, new MinionPower(this.m)));
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
