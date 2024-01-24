package rhinemod.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import javassist.CtBehavior;
import rhinemod.actions.StarRingBlastAction;
import rhinemod.powers.EgotistPower;

public class StarRing extends AbstractCreature {
    public static final String ID = "rhinemod:StarRing";
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String NAME = uiStrings.TEXT[0];
    public static final Texture img = new Texture("images/char/StarRing.png");
    public int blastDamage;
    public float hoverTimer;
    public Color nameColor;
    public Color nameBgColor;

    public StarRing(int maxHealth, float x, float y) {
        super();
        name = NAME;
        id = ID;
        this.maxHealth = currentHealth = maxHealth;
        hb_w = 150.0F * Settings.scale;
        hb_h = 150.0F * Settings.scale;
        drawX = AbstractDungeon.player.drawX + x * Settings.scale;
        drawY = AbstractDungeon.player.drawY + y * Settings.scale;
        hb = new Hitbox(hb_w, hb_h);
        healthHb = new Hitbox(hb_w, 72.0F * Settings.scale);
        hb.move(this.drawX + this.hb_x, this.drawY + this.hb_y + this.hb_h / 2.0F);
        healthHb.move(this.hb.cX, this.hb.cY - this.hb_h / 2.0F - this.healthHb.height / 2.0F);
        isPlayer = true;
        hoverTimer = 0.0F;
        nameColor = new Color();
        nameBgColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        healthBarUpdatedEvent();
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.output > 0 && hasPower("IntangiblePlayer")) {
            info.output = 1;
        }

        int damageAmount = info.output;
        if (!isDying) {
            if (damageAmount < 0) damageAmount = 0;
            damageAmount = decrementBlock(info, damageAmount);

            if (info.owner != null) {
                for (AbstractPower p : info.owner.powers)
                    damageAmount = p.onAttackToChangeDamage(info, damageAmount);
            }

            for (AbstractPower p : powers) damageAmount = p.onAttackedToChangeDamage(info, damageAmount);
            for (AbstractPower p : powers) p.wasHPLost(info, damageAmount);

            if (info.owner != null) {
                for (AbstractPower p : info.owner.powers)
                    p.onAttack(info, damageAmount, this);
            }

            for (AbstractPower p : powers) p.onAttacked(info, damageAmount);

            lastDamageTaken = damageAmount;
            if (damageAmount > 0) {
                if (info.owner != this) useStaggerAnimation();
                currentHealth -= damageAmount;
                AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, damageAmount));

                if (currentHealth <= 0) {
                    blastDamage = maxHealth - currentHealth;
                    currentHealth = 0;
                }

                this.healthBarUpdatedEvent();
            }

            if (this.currentHealth <= 0) {
                die();
                if (this.currentBlock > 0) {
                    this.loseBlock();
                    AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
                }
            }
        }
    }

    public void die() {
        if (!isDead) {
            isDead = true;
            AbstractDungeon.actionManager.addToTop(new StarRingBlastAction(blastDamage, true));
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.5F));
        }
    }

    public void blast() {
        AbstractDungeon.actionManager.addToTop(new StarRingBlastAction(maxHealth, true));
    }

    public int calculateDmg(float dmg) {
        for (AbstractPower p : powers) dmg = p.atDamageGive(dmg, DamageInfo.DamageType.NORMAL);
        for (AbstractPower p : powers) dmg = p.atDamageFinalGive(dmg, DamageInfo.DamageType.NORMAL);
        return (int)Math.floor(dmg);
    }

    public void takeTurn() {
        int dmg = calculateDmg(1);
        AbstractDungeon.actionManager.addToTop(new StarRingBlastAction(dmg, false));
        AbstractDungeon.actionManager.addToTop(new WaitAction(0.5F));
    }

    @Override
    public void applyStartOfTurnPowers() {
        super.applyStartOfTurnPowers();
        takeTurn();
    }

    @Override
    public void render(SpriteBatch sb) {
        if (isDead) return;
        sb.setColor(this.tint.color);
        sb.draw(img, this.drawX - (float)img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, (float)img.getWidth() * Settings.scale, (float)img.getHeight() * Settings.scale, 0, 0, img.getWidth(), img.getHeight(), false, false);
        renderHealth(sb);
        renderName(sb);
        hb.render(sb);
        healthHb.render(sb);
        hb.update();
        healthHb.update();
        if (hb.hovered || healthHb.hovered) renderPowerTips(sb);
    }

    private void renderName(SpriteBatch sb) {
        if (!this.hb.hovered) {
            hoverTimer = MathHelper.fadeLerpSnap(hoverTimer, 0.0F);
        } else {
            hoverTimer += Gdx.graphics.getDeltaTime();
        }

        if ((!AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.hoveredCard == null) && !isDead) {
            if (hoverTimer != 0.0F) {
                nameColor.a = Math.min(hoverTimer * 2.0F, 1.0F);
            } else {
                nameColor.a = MathHelper.slowColorLerpSnap(nameColor.a, 0.0F);
            }

            float tmp = Interpolation.exp5Out.apply(1.5F, 2.0F, hoverTimer);
            nameColor.r = Interpolation.fade.apply(Color.DARK_GRAY.r, Settings.CREAM_COLOR.r, hoverTimer * 10.0F);
            nameColor.g = Interpolation.fade.apply(Color.DARK_GRAY.g, Settings.CREAM_COLOR.g, hoverTimer * 3.0F);
            nameColor.b = Interpolation.fade.apply(Color.DARK_GRAY.b, Settings.CREAM_COLOR.b, hoverTimer * 3.0F);
            float y = Interpolation.exp10Out.apply(healthHb.cY, healthHb.cY - 8.0F * Settings.scale, nameColor.a);
            float x = hb.cX - animX;
            nameBgColor.a = nameColor.a / 2.0F * hbAlpha;
            sb.setColor(nameBgColor);
            TextureAtlas.AtlasRegion img = ImageMaster.MOVE_NAME_BG;
            sb.draw(img, x - img.packedWidth / 2.0F, y - img.packedHeight / 2.0F, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, Settings.scale * tmp, Settings.scale * 2.0F, 0.0F);
            nameColor.a *= hbAlpha;
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, name, x, y, nameColor);
        }

    }

    public void update() {
        for (AbstractPower p : powers)
            p.updateParticles();
        updateHealthBar();
        tint.update();
    }

    @SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
    public static class EndBattlePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(AbstractMonster _inst) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p instanceof RhineLab && !p.hasPower(EgotistPower.POWER_ID)) {
                for (StarRing r : ((RhineLab) p).currentRings)
                    r.blast();
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "overlayMenu");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
