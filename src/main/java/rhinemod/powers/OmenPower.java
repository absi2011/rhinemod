package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.cards.Omen;

import java.util.ArrayList;
import java.util.logging.Logger;

public class OmenPower extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:OmenPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final ArrayList<AbstractCard> cards = new ArrayList<>();
    public boolean upgraded;
    public OmenPower(AbstractCreature owner, boolean upgraded) {
        this.ID = POWER_ID;
        this.name = NAME;
        if (upgraded) this.name += "+";
        this.type = PowerType.BUFF;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Omen 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Omen 48.png"), 0, 0, 48, 48);
        this.upgraded = upgraded;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        if (upgraded) sb.append(DESCRIPTIONS[1]);
        else sb.append(DESCRIPTIONS[0]);
        for (int i = 0; i < cards.size(); i++) {
            if (i != 0) sb.append(DESCRIPTIONS[2]);
            sb.append(" #y").append(cards.get(i).name).append(" ");
        }
        if (cards.isEmpty()) {
            sb.append(DESCRIPTIONS[4]);
        }
        sb.append(DESCRIPTIONS[3]);
        description = sb.toString();
    }

    @Override
    public void atStartOfTurn() {
        if (!upgraded) trigger();
    }

    public void trigger() {
        for (AbstractCard card : cards) {
            card.applyPowers();
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, true, EnergyPanel.getCurrentEnergy(), false, true), true);
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction action) {
        if (!c.isInAutoplay && c instanceof AbstractRhineCard &&
                ((AbstractRhineCard) c).realBranch == 1 && !(c instanceof Omen)) {
            AbstractCard card = c.makeStatEquivalentCopy();
            card.purgeOnUse = true;
            cards.add(card);
            Logger.getLogger(OmenPower.class.getName()).info("stack card " + card.cardID);
            updateDescription();
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class ApplyPowerPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(String.class.getName()) && m.getMethodName().equals("equals")) {
                        m.replace("$_ = (" + ApplyPowerPatch.class.getName() + ".checkReplacement($0, $1) || $proceed($$));");
                    }
                }
            };
        }

        public static boolean checkReplacement(String id, Object aim) {
            if (aim.equals("Night Terror")) {
                return id.equals(OmenPower.POWER_ID);
            } else {
                return false;
            }
        }
    }

    @SpirePatch(clz = EndTurnButton.class, method = "disable", paramtypez = {boolean.class})
    public static class EndTurnPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(EndTurnButton _inst, boolean isEnemyTurn) {
            for (AbstractPower po : AbstractDungeon.player.powers)
                if (po instanceof OmenPower && ((OmenPower) po).upgraded)
                    ((OmenPower) po).trigger();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "endTurnQueued");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
