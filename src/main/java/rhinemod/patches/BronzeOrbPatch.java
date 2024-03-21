package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.BronzeOrb;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;

public class BronzeOrbPatch {
    @SpirePatch(clz = BronzeOrb.class, method = "takeTurn")
    public static class IntentPatch {
        @SpirePrefixPatch
        public static void Prefix(BronzeOrb __inst) {
            if ((__inst.nextMove == 2) && (AbstractDungeon.getMonsters().getMonster("BronzeAutomaton") == null)) {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(__inst, __inst, 12));
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(__inst));
                __inst.nextMove = 4;
            }
        }
    }
}
