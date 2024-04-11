package rhinemod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.ThornsPower;

public class ResearchProgress extends AbstractPower {
    public static final String POWER_ID = "rhinemod:ResearchProgress";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public ResearchProgress(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.loadRegion("curiosity");
        this.priority = 0;
        canGoNegative = true;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + getNext() + DESCRIPTIONS[2];
    }

    public void updateLevel(int prevLevel, int nowLevel) {
        if (prevLevel < nowLevel) {
            this.flash();
            for (int i = nowLevel; i > prevLevel; i--) {
                if (i % 2 == 1)
                    addToTop(new ApplyPowerAction(owner, owner, new MetallicizePower(owner, 3 + i / 2)));
                else
                    addToTop(new ApplyPowerAction(owner, owner, new ThornsPower(owner, 2 + i / 2)));
            }
        }
        else {
            for (int i = nowLevel + 1; i <= prevLevel; i++) {
                if (i % 2 == 1)
                    addToTop(new ReducePowerAction(owner, owner, "Metallicize", 3 + i / 2));
                else
                    addToTop(new ReducePowerAction(owner, owner, "Thorns", 2 + i / 2));
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        int prevLevel = getLevel();
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        int nowLevel = getLevel();
        updateLevel(prevLevel, nowLevel);
    }

    public void onInitialApplication() {
        updateLevel(0, getLevel());
    }

    public int getLevel() {
        int level = 0;
        int cur = 4;
        int sum = 0;
        for (;;) {
            sum += cur;
            if (sum > amount) return level;
            level ++;
            if (level % 2 == 0) cur += 2;
        }
    }

    public int getNext() {
        int level = 0;
        int cur = 4;
        int sum = 0;
        for (;;) {
            sum += cur;
            if (sum > amount) return sum - amount;
            level ++;
            if (level % 2 == 0) cur += 2;
        }
    }
}
