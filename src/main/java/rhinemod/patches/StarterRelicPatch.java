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
                // TODO: 一次性删除多张牌时可能会敲到被删除的牌。
                // TODO: 和烟斗撞了，会报错
                ArrayList<AbstractCard> list = new ArrayList<>();
                for (AbstractCard ca : AbstractDungeon.player.masterDeck.group)
                    if (ca.canUpgrade())
                        list.add(ca);
                if (!list.isEmpty()) {
                    AbstractCard cardToUpgrade = list.get(AbstractDungeon.cardRng.random(0, list.size() - 1));
                    float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
                    float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;
                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(cardToUpgrade.makeStatEquivalentCopy(), x, y));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                    cardToUpgrade.upgrade();
                }
            }
        }
    }
}
