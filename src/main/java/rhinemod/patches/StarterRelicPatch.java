package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.PandorasBox;
import rhinemod.relics.RhineLabEmployeeCard;
import rhinemod.relics.TITStudentIdCard;

public class StarterRelicPatch {
    @SpirePatch(clz = CardGroup.class, method = "removeCard", paramtypez = {AbstractCard.class})
    public static class OnRemoveCardFromMasterDeckPatch {
        @SpirePostfixPatch
        public static void Post(CardGroup _inst, AbstractCard c) {
            if (_inst.type == CardGroup.CardGroupType.MASTER_DECK) {
                triggerStarterRelic(1);
            }
        }
    }

    @SpirePatch(clz = PandorasBox.class, method = "onEquip")
    public static class PandorasBoxPatch {
        @SpirePostfixPatch
        public static void Postfix(PandorasBox _inst, int ___count) {
            triggerStarterRelic(___count);
        }
    }

    public static void triggerStarterRelic(int cnt) {
        if (AbstractDungeon.player.hasRelic(TITStudentIdCard.ID)) {
            AbstractDungeon.player.getRelic(TITStudentIdCard.ID).counter += cnt;
        }
        if (AbstractDungeon.player.hasRelic(RhineLabEmployeeCard.ID)) {
            AbstractDungeon.player.getRelic(RhineLabEmployeeCard.ID).counter += cnt;
        }
    }
}
