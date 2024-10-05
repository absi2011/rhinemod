package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rhinemod.powers.Stunned;

public class R31HeavyPowerArmorRelic extends CustomRelic {

    public static final String ID = "rhinemod:R31HeavyPowerArmorRelic";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/R31HeavyPowerArmorRelic.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/R31HeavyPowerArmorRelic_p.png");
    public R31HeavyPowerArmorRelic() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(Stunned.NAME, Stunned.DESCRIPTIONS[0]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        AbstractMonster m = AbstractDungeon.getRandomMonster();
        if (m != null) {
            flash();
            for (int i = 0; i < 10; i++)
                addToBot(new DamageAction(m, new DamageInfo(null, 3, DamageInfo.DamageType.THORNS)));
            addToBot(new ApplyPowerAction(m, null, new Stunned(m)));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new R31HeavyPowerArmorRelic();
    }
}
