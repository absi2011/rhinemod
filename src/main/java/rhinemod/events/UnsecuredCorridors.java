package rhinemod.events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rhinemod.monsters.R11AssaultPowerArmor;
import rhinemod.monsters.R31HeavyPowerArmor;
import rhinemod.relics.Dor3Bionic;

public class UnsecuredCorridors extends AbstractImageEvent {
    public static final String ID = "rhinemod:UnsecuredCorridors";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurScreen screen = CurScreen.INTRO;
    private enum CurScreen {
        INTRO, FIGHT, LEAVE
    }
    private final int damage;
    public UnsecuredCorridors() {
        super(NAME, DESCRIPTIONS[0], "resources/rhinemod/images/event/UnsecuredCorridors.png");
        damage = MathUtils.floor(AbstractDungeon.player.maxHealth * 0.25F);
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1] + damage + OPTIONS[2]);
        this.imageEventText.setDialogOption(OPTIONS[3]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screen = CurScreen.FIGHT;
                        imageEventText.updateDialogOption(0, OPTIONS[4]);
                        logMetric(ID, "Recollections");
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screen = CurScreen.LEAVE;
                        AbstractDungeon.player.damage(new DamageInfo(null, damage));
                        AbstractRelic r = new Dor3Bionic();
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, r);
                        logMetricObtainRelicAndDamage(ID, "Onward", r, damage);
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.screen = CurScreen.LEAVE;
                        logMetric(ID, "Leave");
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        break;
                }
                this.imageEventText.clearRemainingOptions();
                return;
            case FIGHT:
                this.screen = CurScreen.LEAVE;
                if (AbstractDungeon.id.equals("TheCity")) {
                    (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[] {new R31HeavyPowerArmor(-500.0F, 0.0F), new R11AssaultPowerArmor(-200.0F, 0.0F), new R11AssaultPowerArmor(100.0F, 0.0F)});
                } else {
                    (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[] {new R31HeavyPowerArmor(-500.0F, 0.0F), new R31HeavyPowerArmor(-300.0F, 0.0F), new R11AssaultPowerArmor(-100.0F, 0.0F), new R11AssaultPowerArmor(100.0F, 0.0F), new R11AssaultPowerArmor(300.0F, 0.0F)});
                }
                AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(100));
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
                (AbstractDungeon.getCurrRoom()).eliteTrigger = true;
                enterCombatFromImage();
                AbstractDungeon.lastCombatMetricKey = "Unsecured Corridors";
                return;
            case LEAVE:
                openMap();
                return;
        }
        openMap();
    }
}
