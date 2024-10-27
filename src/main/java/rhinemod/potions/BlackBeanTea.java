package rhinemod.potions;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rhinemod.RhineMod;
import rhinemod.powers.BlackBeanTeaPower;

public class BlackBeanTea extends AbstractPotion {
    public static String ID = "rhinemod:BlackBeanTea";
    public static PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static String NAME = potionStrings.NAME;
    public static String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static int STRENGTH = 7;
    public static int COIN_LOSE = 50;
    public BlackBeanTea() {
        super(NAME, ID, PotionRarity.UNCOMMON, PotionSize.BOTTLE, PotionEffect.NONE, Color.GRAY, Color.DARK_GRAY, null);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1] + COIN_LOSE + DESCRIPTIONS[2];
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.STRENGTH.NAMES[0]), BaseMod.getKeywordDescription(GameDictionary.keywords.get(GameDictionary.STRENGTH.NAMES[0]))));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, potency)));
        addToBot(new ApplyPowerAction(p, p, new BlackBeanTeaPower(p, COIN_LOSE)));
    }

    @Override
    public int getPotency(int i) {
        return STRENGTH;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BlackBeanTea();
    }
}
