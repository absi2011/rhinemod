package rhinemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import rhinemod.patches.DorothyResonatorPatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SetTrapAction extends AbstractGameAction {
    public SetTrapAction(int amount) {
        actionType = ActionType.SPECIAL;
        this.amount = amount;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> lst = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.drawPile.group)
            if (!DorothyResonatorPatch.OptFields.trapped.get(c))
                lst.add(c);
        Collections.shuffle(lst, new Random(AbstractDungeon.cardRng.randomLong()));
        for (int i = 0; i < amount && i < lst.size(); i++) {
            DorothyResonatorPatch.OptFields.trapped.set(lst.get(i), true);
            //TODO: 展示下这张些卡
        }
        isDone = true;
    }
}
