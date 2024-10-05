package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rhinemod.actions.Dor3BionicAction;
import rhinemod.monsters.Awaken_Monster;
import rhinemod.monsters.ExperimentalPowerArmor;
import rhinemod.monsters.R31HeavyPowerArmor;

public class Dor3Bionic extends CustomRelic {

    public static final String ID = "rhinemod:Dor3Bionic";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/Dor3Bionic.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/Dor3Bionic_p.png");
    private boolean triggered;
    public Dor3Bionic() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
        triggered = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public boolean checkTrigger(AbstractMonster m) {
        if (triggered) return false;
        AbstractMonster aimMonster = null;
        AbstractRelic aimRelic = null;
        if (m instanceof Awaken_Monster) {
            aimRelic = new Awaken();
            aimMonster = m;
        } else if (m instanceof R31HeavyPowerArmor) {
            aimRelic = new R31HeavyPowerArmorRelic();
            aimMonster = m;
        } else if (m instanceof ExperimentalPowerArmor) {
            aimRelic = new ExperimentalPowerArmorRelic();
            aimMonster = m;
        }
        if (aimRelic != null) {
            triggered = true;
            addToBot(new Dor3BionicAction(this, aimRelic, aimMonster));
            return true;
        }
        return false;
    }

    @Override
    public void atBattleStart() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
            if (checkTrigger(m))
                return;
    }

    @Override
    public void onSpawnMonster(AbstractMonster m) {
        checkTrigger(m);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Dor3Bionic();
    }
}
