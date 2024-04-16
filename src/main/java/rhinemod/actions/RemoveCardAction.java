package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.UUID;

public class RemoveCardAction extends AbstractGameAction {
    public final UUID uuid;
    public RemoveCardAction(UUID uuid) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
        this.uuid = uuid;
    }
    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractCard c : p.masterDeck.group)
            if (c.uuid == uuid) {
                CardCrawlGame.sound.play("CARD_EXHAUST");
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                p.masterDeck.removeCard(c);
                break;
            }
        isDone = true;
    }
}
