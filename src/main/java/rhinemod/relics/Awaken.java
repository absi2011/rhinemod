package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class Awaken extends CustomRelic {

    public static final String ID = "rhinemod:Awaken";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/Awaken.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/Awaken_p.png");
    public static final int TOTAL_DMG = 20;
    public Awaken() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.HEAVY);
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayerEndTurn() {
        counter++;
        if (counter == 3) {
            counter = 0;
            flash();
            ArrayList<AbstractMonster> list = new ArrayList<>();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if (!m.isDeadOrEscaped())
                    list.add(m);
            if (list.isEmpty()) return;
            int[] dmg = new int[list.size()];
            int baseDmg = TOTAL_DMG / list.size();
            for (int i = 0; i < list.size(); i++)
                dmg[i] = baseDmg;
            int res = TOTAL_DMG % list.size();
            for (int i = 0; i < res; i++)
                dmg[i]++;
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, dmg, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    @Override
    public void atBattleStart() {
        counter = 0;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Awaken();
    }
}
