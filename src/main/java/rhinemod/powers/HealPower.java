package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import rhinemod.cards.AbstractRhineCard;

import java.lang.reflect.InvocationTargetException;

public class HealPower extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:HealPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    int recordAmount = 0;
    public int needRemove = 1;
    public HealPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/HealPower 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/HealPower 32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        // 保险起见，留下这个时间点，一般来说不会触发
        if (info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            recordAmount = damageAmount;
            return 0;
        }
        else {
            return damageAmount;
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        // 我在这个时间点治疗不是因为我有品，而是因为之前根本不知道打的是谁
        if (info.type == DamageInfo.DamageType.NORMAL) {
            addToBot(new HealAction(target, owner, recordAmount));
            if (needRemove == 1) {
                needRemove = 0;
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class HealPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractMonster _inst, DamageInfo info) {
            if ((info.owner != null) && (info.owner.hasPower(POWER_ID)) && (info.type == DamageInfo.DamageType.NORMAL)) {
                AbstractDungeon.actionManager.addToBottom(new HealAction(_inst, info.owner, info.output));
                HealPower p = (HealPower)info.owner.getPower(POWER_ID);
                p.flash();
                if (p.needRemove == 1) {
                    p.needRemove = 0;
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p.owner, p.owner, p));
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class HealPatchPlayer {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractPlayer _inst, DamageInfo info) {
            if ((info.owner != null) && (info.owner.hasPower(POWER_ID)) && (info.type == DamageInfo.DamageType.NORMAL)) {
                AbstractDungeon.actionManager.addToBottom(new HealAction(_inst, info.owner, info.output));
                HealPower p = (HealPower)info.owner.getPower(POWER_ID);
                p.flash();
                if (p.needRemove == 1) {
                    p.needRemove = 0;
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p.owner, p.owner, p));
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
