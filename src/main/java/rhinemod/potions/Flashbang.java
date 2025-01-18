package rhinemod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rhinemod.powers.Stunned;
import rhinemod.vfx.FlashbangEffect;

public class Flashbang extends AbstractPotion {
    public static String ID = "rhinemod:Flashbang";
    public static PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static String NAME = potionStrings.NAME;
    public static String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static int STRENGTH_LOSE = 6;
    public Flashbang() {
        super(NAME, ID, PotionRarity.UNCOMMON, PotionSize.SPHERE, PotionEffect.NONE, Color.WHITE, null, null);
        isThrown = true;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        description = DESCRIPTIONS[0] + getPotency() + DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        potency = getPotency();
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.effectList.add(new FlashbangEffect());
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters)
            if (m.hasPower(Stunned.POWER_ID)) {
                addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -potency)));
            } else {
                addToBot(new ApplyPowerAction(m, p, new Stunned(m)));
            }
    }

    @Override
    public int getPotency(int i) {
        return STRENGTH_LOSE;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new Flashbang();
    }
}
