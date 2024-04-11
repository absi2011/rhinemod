package rhinemod.events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import rhinemod.cards.special.*;
import rhinemod.relics.LoneTrail;

import java.util.ArrayList;
import java.util.List;

public class ConvergenceOfPastAndPresent extends AbstractImageEvent {
    public static final String ID = "rhinemod:ConvergenceOfPastAndPresent";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurScreen screen = CurScreen.INTRO;
    private enum CurScreen {
        INTRO, FIGHT, LEAVE
    }
    int hp;
    public ConvergenceOfPastAndPresent() {
        super(NAME, DESCRIPTIONS[0], "resources/rhinemod/images/crads/BionicDevice.png");
        if (AbstractDungeon.ascensionLevel < 15) {
            hp = MathUtils.floor(AbstractDungeon.player.maxHealth * 0.3F);
        }
        else {
            hp = MathUtils.floor(AbstractDungeon.player.maxHealth * 0.2F);
        }
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1] + hp + OPTIONS[2]);
        this.imageEventText.setDialogOption(OPTIONS[3]);
        noCardsInRewards = true;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screen = CurScreen.LEAVE;
                        ArrayList<AbstractCard> c = new ArrayList<AbstractCard>();
                        c.add(new Egotist());
                        c.add(new Traitor());
                        c.add(new Seeker());
                        c.add(new Loner());
                        c.add(new Pioneer());
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c.get(0), (float)Settings.WIDTH / 2.0F -  700.0F * Settings.xScale, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c.get(1), (float)Settings.WIDTH / 2.0F -  350.0F * Settings.xScale, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c.get(2), (float)Settings.WIDTH / 2.0F -    0.0F * Settings.xScale, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c.get(3), (float)Settings.WIDTH / 2.0F +  350.0F * Settings.xScale, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c.get(4), (float)Settings.WIDTH / 2.0F +  700.0F * Settings.xScale, (float)Settings.HEIGHT / 2.0F));
                        imageEventText.updateDialogOption(0, OPTIONS[4]);
                        imageEventText.clearRemainingOptions();
                        List<String> cards = new ArrayList<String>();
                        int i;
                        for (i = 0; i < 5; i++) {
                            cards.add(c.get(i).cardID);
                        }
                        logMetricObtainCards(ID, "Follow", cards);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screen = CurScreen.LEAVE;
                        AbstractDungeon.player.heal(hp, true);
                        imageEventText.updateDialogOption(0, OPTIONS[4]);
                        imageEventText.clearRemainingOptions();
                        logMetricHeal(ID, "Restore", hp);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.screen = CurScreen.LEAVE;
                        logMetric(ID, "Leave");
                        imageEventText.updateDialogOption(0, OPTIONS[4]);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                this.imageEventText.clearRemainingOptions();
                return;
            default:
                openMap();
        }
        openMap();
    }
}
