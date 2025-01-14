package rhinemod.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import rhinemod.actions.GalaxyWithVfxAction;
import rhinemod.characters.RhineLab;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class Galaxy extends AbstractRhineCard {
    public static final String ID = "rhinemod:Galaxy";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/Galaxy.png";
    public static final int COST = 4;
    public static final int ATTACK_DMG = 6;
    public static final int UPGRADE_PLUS_DMG = 2;

    public Galaxy() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        realBranch = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (starRingCnt() >= 3 && !Settings.FAST_MODE && !isInAutoplay) {
            addToBot(new GalaxyWithVfxAction(m, damage));
        } else {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    @Override
    public void applyPowers() {
        int cnt = starRingCnt();
        baseDamage <<= cnt;
        super.applyPowers();
        baseDamage >>= cnt;
        isDamageModified = (baseDamage != damage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int cnt = starRingCnt();
        baseDamage <<= cnt;
        super.calculateCardDamage(mo);
        baseDamage >>= cnt;
        isDamageModified = (baseDamage != damage);
    }

    public static int starRingCnt() {
        if (AbstractDungeon.player instanceof RhineLab) {
            return ((RhineLab) AbstractDungeon.player).currentRings.size();
        } else {
            return 0;
        }
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeDamage(UPGRADE_PLUS_DMG);
                    initializeDescription();
                }
            });
        }};
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderEnergyPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"costColor", "text"})
        public static void Insert(AbstractCard _inst, SpriteBatch sb, @ByRef Color[] costColor, @ByRef String[] text) {
            if (_inst instanceof Galaxy) {
                int cnt = starRingCnt();
                if (cnt > 0) {
                    if (costColor[0].g > 0.5F) { // still white
                        costColor[0] = new Color(0.4F, 1.0F, 0.4F, _inst.transparency);
                    }
                    text[0] = Integer.toString(Math.max(0, Integer.parseInt(text[0]) - cnt));
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "getEnergyFont");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class HasEnoughEnergyPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getFieldName().equals("costForTurn"))
                        m.replace("$_ = -" + Galaxy.class.getName() + ".calcCostDec($0) + $proceed($$);");
                }
            };
        }
    }

    public static int calcCostDec(AbstractCard c) {
        if (c instanceof Galaxy) {
            return starRingCnt();
        } else {
            return 0;
        }
    }
}
