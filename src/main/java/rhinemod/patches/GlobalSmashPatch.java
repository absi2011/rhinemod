package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import rhinemod.powers.AbstractRhinePower;
import rhinemod.powers.Armor;
import rhinemod.powers.Stunned;
import rhinemod.powers.WeaknessNonSmash;
import rhinemod.util.GlobalAttributes;

import static rhinemod.powers.WeaknessNonSmash.NotTrigger;

public class GlobalSmashPatch {
    private static int calcDamageAmountIfSmash(AbstractCreature target, DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount >= GlobalAttributes.smashThreshold) {
            if (info.owner != null) {
                for (AbstractPower po : info.owner.powers)
                    if (po instanceof AbstractRhinePower)
                        damageAmount = ((AbstractRhinePower) po).onSmash(target, damageAmount);
            }
            for (AbstractPower po : target.powers)
                if (po instanceof AbstractRhinePower)
                    damageAmount = ((AbstractRhinePower) po).onSmashed(damageAmount);
            if (target instanceof AbstractMonster) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, info.owner, new Stunned(target)));
            }
        } else if (damageAmount > 0) {
            if (target.hasPower(WeaknessNonSmash.POWER_ID) && ((info.name == null) || !info.name.equals(NotTrigger))) {
                target.getPower(WeaknessNonSmash.POWER_ID).flash();
                DamageInfo newInfo = new DamageInfo(target, damageAmount, DamageInfo.DamageType.THORNS);
                newInfo.name = NotTrigger;
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, newInfo));
            }
            if (info.type == DamageInfo.DamageType.NORMAL && target.hasPower(Armor.POWER_ID)) {
                target.getPower(Armor.POWER_ID).flash();
                damageAmount /= 2;
            }
        }
        return damageAmount;
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class MonsterDamagePatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void Insert(AbstractMonster _inst, DamageInfo info, @ByRef int[] damageAmount) {
            damageAmount[0] = calcDamageAmountIfSmash(_inst, info, damageAmount[0]);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "lastDamageTaken");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class PlayerDamagePatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void Insert(AbstractPlayer _inst, DamageInfo info, @ByRef int[] damageAmount) {
            damageAmount[0] = calcDamageAmountIfSmash(_inst, info, damageAmount[0]);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "lastDamageTaken");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
