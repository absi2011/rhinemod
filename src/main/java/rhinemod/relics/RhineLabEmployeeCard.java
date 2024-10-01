package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import rhinemod.cards.AbstractRhineCard;

import java.util.ArrayList;

public class RhineLabEmployeeCard extends CustomRelic {

    public static final String ID = "rhinemod:RhineLabEmployeeCard";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/LoneTrail.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/LoneTrail_p.png");
    public RhineLabEmployeeCard() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RhineLabEmployeeCard();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        for (; counter > 0; counter--) {
            ArrayList<AbstractCard> list = new ArrayList<>();
            for (AbstractCard ca : AbstractDungeon.player.masterDeck.group)
                if (ca.canUpgrade())
                    list.add(ca);
            if (!list.isEmpty()) {
                AbstractCard cardToUpgrade = list.get(AbstractDungeon.cardRng.random(0, list.size() - 1));
                float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
                float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;
                if (cardToUpgrade instanceof AbstractRhineCard) ((AbstractRhineCard) cardToUpgrade).randomUpgrade();
                else cardToUpgrade.upgrade();
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(cardToUpgrade.makeStatEquivalentCopy(), x, y));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
            }
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(TITStudentIdCard.ID);
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(TITStudentIdCard.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++)
                if (AbstractDungeon.player.relics.get(i).relicId.equals(TITStudentIdCard.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
        } else {
            super.obtain();
        }
    }
}
