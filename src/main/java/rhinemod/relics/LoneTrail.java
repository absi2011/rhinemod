package rhinemod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import rhinemod.cards.special.*;
import rhinemod.rooms.HeartRoom;

import java.util.ArrayList;

public class LoneTrail extends CustomRelic {

    public static final String ID = "rhinemod:LoneTrail";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/rhinemod/images/relics/LoneTrail.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/rhinemod/images/relics/LoneTrail_p.png");
    public LoneTrail() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard c: AbstractDungeon.player.masterDeck.group) {
            if ((c instanceof Egotist) || (c instanceof Traitor) || (c instanceof Seeker) || (c instanceof Loner) || (c instanceof Pioneer)) {
                cards.add(c);
            }
        }
        for (AbstractCard c: cards) {
            CardCrawlGame.sound.play("CARD_EXHAUST");
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
            AbstractDungeon.player.masterDeck.removeCard(c);
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if ((counter == 0) && (AbstractDungeon.getCurrRoom() instanceof HeartRoom)) {
            AbstractMonster m = null;
            m.applyPowers(); // 搞个NPE，防止其他方式获取孤星。
        }
    }

    @Override
    public void atBattleStartPreDraw() {
        if ((counter > 0) && (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) {
            flash();
            int i;
            for (i = 0; i < counter; i++) {
                // 这里无论如何应该只有一个敌人。
                AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.monsters.get(0);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(AbstractDungeon.player, 1, DamageInfo.DamageType.HP_LOSS)));
                //TODO: 如果以后有技术力可以做一个星光特效
            }
            counter = -1;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LoneTrail();
    }
}
