package rhinemod.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import rhinemod.actions.StarRingBlastAction;
import rhinemod.cards.AbstractRhineCard;
import rhinemod.powers.GalleriaStellariaPower;

public class StarRing extends AbstractMonster {
    public static final String ID = "rhinemod:StarRing";
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final Texture img = new Texture("resources/rhinemod/images/char/StarRing.png");
    public static final String IMG = "resources/rhinemod/images/char/StarRing.png";
    public int blastDamage;
    public float hoverTimer;
    public Color nameColor;
    public Color nameBgColor;

    public StarRing(int maxHealth, float x, float y) {
        super(TEXT[0], ID, maxHealth, 0.0F, 0.0F, 150.0F, 150.0F, IMG, 0.0F, 0.0F);
        drawX = AbstractDungeon.player.drawX + x * Settings.scale;
        drawY = AbstractDungeon.player.drawY + y * Settings.scale;
        refreshHitboxLocation();
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
        die(true);
    }

    public void die(boolean includeSelf) {
        if (!isDead) {
            isDead = true;
            if (blastDamage == 0) blastDamage = maxHealth - currentHealth;
            blastDamage /= 2;
            AbstractDungeon.actionManager.addToTop(new StarRingBlastAction(blastDamage, includeSelf));
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.5F));
            for (AbstractPower p : powers) {
                p.onDeath();
            }
        }
    }

    public void blast() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(p.hb.cX, p.hb.cY, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        DamageInfo info = new DamageInfo(null, 5, DamageInfo.DamageType.THORNS);
        info.name = "StarRing";
        p.damage(info);
    }

    public int calculateDmg(float dmg) {
        for (AbstractPower p : powers) dmg = p.atDamageGive(dmg, DamageInfo.DamageType.NORMAL);
        for (AbstractPower p : powers) dmg = p.atDamageFinalGive(dmg, DamageInfo.DamageType.NORMAL);
        return (int)Math.floor(dmg);
    }

    @Override
    public void applyStartOfTurnPowers() {
        super.applyStartOfTurnPowers();
        int dmg = calculateDmg(5);
        int cnt = 1;
        if (AbstractDungeon.player.hasPower(GalleriaStellariaPower.POWER_ID)) {
            cnt += AbstractDungeon.player.getPower(GalleriaStellariaPower.POWER_ID).amount;
        }
        for (int i = 0; i < cnt; i ++) {
            AbstractDungeon.actionManager.addToTop(new StarRingBlastAction(dmg, false));
        }
        AbstractDungeon.actionManager.addToTop(new WaitAction(0.5F));
        loseBlock();
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

    @Override
    public void renderPowerTips(SpriteBatch sb) {
        tips.clear();
        tips.add(new PowerTip(TEXT[0], TEXT[1] + calculateDmg(5) + TEXT[2]));
        for (AbstractPower p : powers) {
            if (p.region48 != null) {
                tips.add(new PowerTip(p.name, p.description, p.region48));
            } else {
                tips.add(new PowerTip(p.name, p.description, p.img));
            }
        }

        if (hb.cX + hb.width / 2.0F < TIP_X_THRESHOLD) {
            TipHelper.queuePowerTips(hb.cX + hb.width / 2.0F + TIP_OFFSET_R_X, hb.cY + TipHelper.calculateAdditionalOffset(tips, hb.cY), tips);
        } else {
            TipHelper.queuePowerTips(hb.cX - hb.width / 2.0F + TIP_OFFSET_L_X, hb.cY + TipHelper.calculateAdditionalOffset(tips, hb.cY), tips);
        }
    }


    private void renderName(SpriteBatch sb) {
        if (!this.hb.hovered) {
            hoverTimer = MathHelper.fadeLerpSnap(hoverTimer, 0.0F);
        } else {
            hoverTimer += Gdx.graphics.getDeltaTime();
        }

        if (!(AbstractDungeon.player.hoveredCard instanceof AbstractRhineCard) ||
                !((AbstractRhineCard) AbstractDungeon.player.hoveredCard).isTargetStarRing) return;
        if (!AbstractDungeon.player.isDraggingCard && !isDead) {
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

    @Override
    public void takeTurn() {}

    @Override
    protected void getMove(int i) {}

    public void update() {
        for (AbstractPower p : powers)
            p.updateParticles();
        updateHealthBar();
        tint.update();
    }
}
