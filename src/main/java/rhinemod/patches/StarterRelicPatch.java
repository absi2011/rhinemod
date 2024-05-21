package rhinemod.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import rhinemod.relics.TITStudentIdCard;

import java.util.ArrayList;

public class StarterRelicPatch {
    @SpirePatch(clz = CardGroup.class, method = "removeCard", paramtypez = {AbstractCard.class})
    public static class OnRemoveCardFromMasterDeckPatch {
        @SpirePostfixPatch
        public static void Post(CardGroup _inst, AbstractCard c) {
            if (_inst.type == CardGroup.CardGroupType.MASTER_DECK && AbstractDungeon.player.hasRelic(TITStudentIdCard.ID)) {
                AbstractDungeon.player.getRelic(TITStudentIdCard.ID).counter ++;
            }
        }
    }
}
