package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.cards.red.Reaper;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import rhinemod.cards.special.Traitor;

public class HAMHRRAction extends AbstractGameAction {

    int basic;
    int extra;
    public HAMHRRAction(AbstractCreature p, AbstractCreature m, int basic, int extra) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
        this.source = p;
        this.target = m;
        this.basic = basic;
        this.extra = extra;
    }

    @Override
    public void update() {
        this.target.damage(new DamageInfo(source, basic));
        if (target.lastDamageTaken < 15) // TODO: 先写个15
        {
            this.addToBot(new SFXAction("ATTACK_HEAVY"));
            this.addToBot(new VFXAction(source, new MindblastEffect(source.dialogX, source.dialogY, source.flipHorizontal), 0.1F));
            // Copied from 超能光束
            addToBot(new DamageAction(target, new DamageInfo(source, extra)));
        }
        isDone = true;
    }
}
