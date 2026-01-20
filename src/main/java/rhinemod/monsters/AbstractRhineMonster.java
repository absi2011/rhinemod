package rhinemod.monsters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import rhinemod.RhineMod;

public abstract class AbstractRhineMonster extends CustomMonster {
    public enum AttackTarget {
        PLAYER,
        DOROTHY,
        BOTH,
        NONE
    }
    public AttackTarget attackTarget = AttackTarget.NONE;
    public static String[] TEXT = CardCrawlGame.languagePack.getUIString("rhinemod:IntentTip").TEXT;
    public float destX;
    public AbstractRhineMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.id.equals("Exordium") || AbstractDungeon.id.equals("TheCity") || AbstractDungeon.id.equals("TheBeyond")) {
            CardCrawlGame.music.fadeOutTempBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            CardCrawlGame.music.playTempBgmInstantly("m_bat_act19side_01_combine.mp3", true);
        }
    }

    public void realDie() {}

    public void addCCTags() {
        for (int i = 1; i <= RhineMod.tagLevel; i++) {
            LogManager.getLogger(AbstractRhineMonster.class.getName()).info("Tag {}:", i);
            addTag(i);
        }
    }

    public void addTag(int i) {
        PowerStrings tags = CardCrawlGame.languagePack.getPowerStrings(id + "Tag" + i);
        if (!tags.NAME.equals("[MISSING_NAME]")) {
            LogManager.getLogger(AbstractRhineMonster.class.getName()).info("{}//{}", tags.NAME, tags.DESCRIPTIONS[0]);
            tips.add(new PowerTip(tags.NAME, tags.DESCRIPTIONS[0]));
        }
    }

    public void dieAnimation() {}

    public void myUpdateIntentTip() {
        if (attackTarget == AttackTarget.NONE) return;
        if (intent == Intent.ATTACK || intent == Intent.ATTACK_BUFF || intent == Intent.ATTACK_DEBUFF || intent == Intent.ATTACK_DEFEND) {
            PowerTip tip = ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
            tip.body += TEXT[0];
            if (attackTarget == AttackTarget.PLAYER) tip.body += TEXT[1];
            else if (attackTarget == AttackTarget.DOROTHY) tip.body += TEXT[2];
            else tip.body += TEXT[1] + TEXT[3] + TEXT[2];
            tip.body += TEXT[4];
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentTip", tip);
        }
    }

    @Override
    public void createIntent() {
        super.createIntent();
        myUpdateIntentTip();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        myUpdateIntentTip();
    }
}
