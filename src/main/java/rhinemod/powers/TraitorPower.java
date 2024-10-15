package rhinemod.powers;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import rhinemod.characters.RhineLab;
import rhinemod.util.GlobalAttributes;

import java.util.logging.Logger;

public class TraitorPower extends AbstractDescriptionPower {
    public static final String POWER_ID = "rhinemod:TraitorPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public TraitorPower(AbstractCreature owner) {
        super("Tr");
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class ChangeDamagePatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void Insert(AbstractMonster _inst, DamageInfo info, @ByRef int[] damageAmount) {
            if (info.owner == AbstractDungeon.player && AbstractDungeon.player.hasPower(TraitorPower.POWER_ID)) {
                if (AbstractDungeon.player instanceof RhineLab && info.type == DamageInfo.DamageType.NORMAL && damageAmount[0] >= GlobalAttributes.smashThreshold) {
                    AbstractDungeon.player.getPower(TraitorPower.POWER_ID).flashWithoutSound();
                    damageAmount[0] *= 2;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(DamageInfo.class, "owner");
                int[] totalTimes = LineFinder.findAllInOrder(ctBehavior, fieldAccessMatcher);
                Logger.getLogger(TraitorPower.class.getName()).info("find " + totalTimes.length + " \"owners\"");
                return new int[] {totalTimes[3]};
            }
        }
    }
}
