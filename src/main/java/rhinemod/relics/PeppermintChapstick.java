package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import rhinemod.actions.AddFlowingShapeAction;
import rhinemod.characters.RhineLab;

import java.util.ArrayList;

public class PeppermintChapstick extends CustomRelic implements ClickableRelic {

    public static final String ID = "rhinemod:PeppermintChapstick";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/Deal.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/Deal.png");
    private static final int FLOWSP_AMOUNT = 2;
    public PeppermintChapstick() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.MAGICAL);
        counter = 3;
        updateDescription(AbstractDungeon.player.chosenClass);
    }

    @Override
    public String getUpdatedDescription() {
        if (counter > 0) {
            return DESCRIPTIONS[0] + counter + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[2];
        }
    }

    @Override
    public void onRightClick() {
        if (counter <= 0) {
            return;
        }
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }
        if (AbstractDungeon.player instanceof RhineLab) {
            addToBot(new AddFlowingShapeAction(FLOWSP_AMOUNT));
            counter--;
            if (counter <= 0) {
                counter = -2;
                updateDescription(AbstractDungeon.player.chosenClass);
            }
        }
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c) {
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        if (p.hasRelic(PeppermintChapstick.ID)) {
            AbstractRelic r = p.getRelic(PeppermintChapstick.ID);
            r.counter = 3;
            r.updateDescription(p.chosenClass);
            r.flash();
            this.isDone = true;
            this.isObtained = true;
            this.discarded = true;
        } else {
            super.instantObtain(p, slot, callOnEquip);
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PeppermintChapstick();
    }

    @SpirePatch(clz = ShopScreen.class, method = "initRelics")
    public static class ShopPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen _inst, ArrayList<StoreRelic> ___relics) {
            if (AbstractDungeon.player.hasRelic(PeppermintChapstick.ID) && AbstractDungeon.player.getRelic(PeppermintChapstick.ID).counter <= 0) {
                ___relics.remove(___relics.size() - 1);
                StoreRelic relic = new StoreRelic(new PeppermintChapstick(), 2, _inst);
                relic.price = 60;
                ___relics.add(relic);
            }
        }
    }
}
