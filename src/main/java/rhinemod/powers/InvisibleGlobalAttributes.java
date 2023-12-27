package rhinemod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rhinemod.characters.RhineLab;
import rhinemod.util.GlobalAttributes;

public class InvisibleGlobalAttributes extends AbstractPower {
    public static final String POWER_ID = "rhinemod:InvisibleGlobalAttributes";
    public InvisibleGlobalAttributes() {
        ID = POWER_ID;
        type = PowerType.BUFF;
        this.priority = -100;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = "";
    }

    public float calcDmgScale() {
        AbstractPlayer p = AbstractDungeon.player;
        float scale = 1.0F;
        if (((RhineLab)p).globalAttributes.gravity == GlobalAttributes.GravityDirection.UP)
            scale *= 1.3F;
        else if (((RhineLab)p).globalAttributes.gravity == GlobalAttributes.GravityDirection.DOWN)
            scale /= 1.3F;
        return scale;
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        damage *= calcDmgScale();
        return damage;
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        damage *= calcDmgScale();
        return damage;
    }
}
