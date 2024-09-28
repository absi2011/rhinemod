package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

public class PittsAssortedFruits extends CustomRelic implements ClickableRelic {

    public static final String ID = "rhinemod:PittsAssortedFruits";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/PittsAssortedFruits.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/PittsAssortedFruits_p.png");
    public static final int HEAL_AMOUNT = 8;
    public PittsAssortedFruits() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
        counter = 1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onRightClick() {
        if (counter <= 0) {
            return;
        }
        AbstractPlayer p = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            p.heal(HEAL_AMOUNT);
        } else {
            addToBot(new HealAction(p, p, HEAL_AMOUNT));
        }
        counter = -2;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PittsAssortedFruits();
    }

    @SpirePatch(clz = AbstractCreature.class, method = "increaseMaxHp")
    public static class IncreaseMaxHpPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractCreature _inst, int amount, boolean showEffect) {
            if (_inst.equals(AbstractDungeon.player) && amount > 0 && AbstractDungeon.player.hasRelic(PittsAssortedFruits.ID)) {
                AbstractDungeon.player.getRelic(PittsAssortedFruits.ID).counter = 1;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "heal");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }
}
