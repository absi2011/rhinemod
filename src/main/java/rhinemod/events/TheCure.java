package rhinemod.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FairyPotion;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import rhinemod.cards.DreadnoughtProtocol;
import rhinemod.cards.special.*;
import rhinemod.characters.RhineLab;
import rhinemod.relics.*;

import java.util.ArrayList;
import java.util.List;

public class TheCure extends AbstractImageEvent {
    public static final String ID = "rhinemod:TheCure";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurScreen screen = CurScreen.INTRO;
    private int basic = 0;
    private enum CurScreen {
        INTRO, LEAVE
    }
    public TheCure() {
        super(NAME, DESCRIPTIONS[0], "resources/rhinemod/images/event/TheCure.png");
        if (AbstractDungeon.player instanceof RhineLab) {
            if (AbstractDungeon.player.gold >= 75) {
                this.imageEventText.setDialogOption(OPTIONS[0], false, new AuxiliaryDrone());
            } else {
                this.imageEventText.setDialogOption(OPTIONS[4], true);
            }
            this.imageEventText.setDialogOption(OPTIONS[1]);
            this.imageEventText.setDialogOption(OPTIONS[2], false, new TracingOrigins());
        } else {
            basic = 4;
            body = DESCRIPTIONS[basic];
            if (AbstractDungeon.player.gold >= 75) {
                this.imageEventText.setDialogOption(OPTIONS[5], false, new AuxiliaryDrone());
            } else {
                this.imageEventText.setDialogOption(OPTIONS[4], true);
            }
            this.imageEventText.setDialogOption(OPTIONS[6]);
            AbstractPlayer p = AbstractDungeon.player;
            if (p.hasRelic(LoneTrail.ID) || p.hasRelic(Deal.ID)) {
                this.imageEventText.setDialogOption(OPTIONS[7], new DreadnoughtProtocol());
            }
        }
        noCardsInRewards = true;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                List<String> tempList = new ArrayList<>();
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[basic + 1]);
                        this.screen = CurScreen.LEAVE;
                        AbstractDungeon.player.loseGold(75);
                        AbstractCard auxiliaryDrone = new AuxiliaryDrone();
                        tempList.add(auxiliaryDrone.cardID);
                        logMetric(ID, "DETERMINATION", tempList, null, null, null, null, null, null, 0, 0, 0, 0, 0, 75);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(auxiliaryDrone, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[basic + 2]);
                        this.screen = CurScreen.LEAVE;
                        AbstractPotion potion = new FairyPotion();
                        tempList.add(potion.ID);
                        logMetric(ID, "VIGIL", null, null, null, null, null, tempList, null, 0, 0, 0, 0, 0, 0);
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(potion));
                        AbstractDungeon.combatRewardScreen.open();
                        break;
                    case 2:
                        if (AbstractDungeon.player instanceof RhineLab) {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            this.screen = CurScreen.LEAVE;
                            logMetric(ID, "Leave");
                            AbstractCard tracingOrigins = new TracingOrigins();
                            logMetricObtainCard(ID, "HESITATION", tracingOrigins);
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(tracingOrigins, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        }
                        else {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[7]);
                            this.screen = CurScreen.LEAVE;
                            logMetric(ID, "Leave");
                            AbstractCard dreadnoughtProtocol = new DreadnoughtProtocol();
                            logMetricObtainCard(ID, "HESITATION", dreadnoughtProtocol);
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(dreadnoughtProtocol, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        }
                        break;
                }
                this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                this.imageEventText.clearRemainingOptions();
                return;
            default:
                openMap();
        }
        openMap();
    }
}
