package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import org.apache.logging.log4j.LogManager;
import rhinemod.RhineMod;

public abstract class AbstractRhineMonster extends CustomMonster {

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
}
