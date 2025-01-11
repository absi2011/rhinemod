package rhinemod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import rhinemod.actions.UnknownReagentAction;

public class UnknownReagent extends AbstractPotion {
    public static String ID = "rhinemod:UnknownReagent";
    public static PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static String NAME = potionStrings.NAME;
    public static String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static int DAMAGE = 8;
    public static int TIMES = 4;
    public UnknownReagent() {
        super(NAME, ID, PotionRarity.RARE, PotionSize.T, PotionEffect.NONE, Color.GRAY, null, Color.DARK_GRAY);
        isThrown = true;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        description = DESCRIPTIONS[0] + getPotency() + DESCRIPTIONS[1] + TIMES + DESCRIPTIONS[2];
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        potency = getPotency();
        addToBot(new UnknownReagentAction(potency));
    }

    @Override
    public int getPotency(int i) {
        return DAMAGE;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new UnknownReagent();
    }
}
