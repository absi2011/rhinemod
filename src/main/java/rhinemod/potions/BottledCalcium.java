package rhinemod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import rhinemod.RhineMod;
import rhinemod.actions.AddCalciumAction;

public class BottledCalcium extends AbstractPotion {
    public static String ID = "rhinemod:BottledCalcium";
    public static PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static String NAME = potionStrings.NAME;
    public static String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static int CA_NUM = 5;
    public BottledCalcium() {
        super(NAME, ID, PotionRarity.COMMON, PotionSize.S, PotionEffect.NONE, Color.GRAY, null, Color.DARK_GRAY);
        labOutlineColor = RhineMod.RhineMatte;
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        description = DESCRIPTIONS[0] + getPotency() + DESCRIPTIONS[1];
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        addToBot(new AddCalciumAction(getPotency()));
    }

    @Override
    public int getPotency(int i) {
        return CA_NUM;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BottledCalcium();
    }
}
