package rhinemod.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import rhinemod.cards.*;
import rhinemod.patches.*;
import rhinemod.powers.InvisibleGlobalAttributes;
import rhinemod.util.GlobalAttributes;

import java.util.ArrayList;

public class RhineLab extends CustomPlayer {

    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("rhinemod:RhineCharacter");
    public static final String NAME = characterStrings.NAMES[0];
    public static final String[] TEXT = characterStrings.TEXT;
    private static final Color RhineMatte = CardHelper.getColor(188, 187, 145);
    public static final String IDLE = "images/char/idle.png";
    public static final String DIE = "images/char/die.png";
    public static final String SHOULDER = "images/char/shoulder.png";
    public static final String[] orbTextures = {
        "images/char/orb/layer1.png",
        "images/char/orb/layer2.png",
        "images/char/orb/layer3.png",
        "images/char/orb/layer4.png",
        "images/char/orb/layer5.png",
        "images/char/orb/layer1d.png",
        "images/char/orb/layer2d.png",
        "images/char/orb/layer3d.png",
        "images/char/orb/layer4d.png"
    };
    public GlobalAttributes globalAttributes = new GlobalAttributes();

    public RhineLab(String name) {
        // 参数列表：角色名，角色类枚举，能量面板贴图路径列表，能量面板特效贴图路径，能量面板贴图旋转速度列表，能量面板，模型资源路径，动画资源路径
        super(name, RhineEnum.RHINE_CLASS, orbTextures, "images/char/orb/vfx.png", null, null, null);

        dialogX = this.drawX;
        dialogY = this.drawY + 200.0F * Settings.scale;

        // 参数列表：静态贴图路径，越肩视角2贴图路径，越肩视角贴图路径，失败时贴图路径，角色选择界面信息，碰撞箱XY宽高，初始能量数
        initializeClass(IDLE, SHOULDER, SHOULDER, DIE, getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(3));
//        loadAnimation("images/char/char_148_nearl.atlas", "images/char/char_148_nearl.json", 1.5F);
//        this.stateData.setMix("Idle", "Die", 0.1F);
//        this.state.setAnimation(0, "Idle", true);
    }

    @Override
    public void playDeathAnimation() {
        this.state.setAnimation(0, "Die", false);
    }

    @Override
    public Color getSlashAttackColor() {
        return RhineMatte;
    }

    @Override
    public Color getCardRenderColor() {
        return RhineMatte;
    }

    @Override
    public Color getCardTrailColor() {
        return RhineMatte;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.RHINE;
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new RhineLab(name);
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Destiny();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.sound.playA("ATTACK_FAST", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    public ArrayList<String> getStartingDeck() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(RhineStrike.ID);
        ret.add(RhineStrike.ID);
        ret.add(RhineStrike.ID);
        ret.add(RhineDefend.ID);
        ret.add(RhineDefend.ID);
        ret.add(RhineDefend.ID);
        ret.add(DefenseSection.ID);
        ret.add(ComponentsControlSection.ID);
        ret.add(EcologicalSection.ID);
        ret.add(Destiny.ID);
        return ret;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> ret = new ArrayList<>();
        // TODO
//        ret.add(CureUp.ID);
//        UnlockTracker.markRelicAsSeen(CureUp.ID);
        return ret;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, TEXT[0],
                81, 81, 0, 99, 5, //starting hp, max hp, max orbs, starting gold, starting hand size
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public void applyStartOfCombatLogic() {
        super.applyStartOfCombatLogic();
        powers.add(new InvisibleGlobalAttributes());
        globalAttributes.atStartOfCombat();
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        if (!Settings.FAST_MODE) {
            if (c.type == AbstractCard.CardType.ATTACK) {
                // TODO spine
            }
            AbstractDungeon.actionManager.addToTop(new WaitAction(1.0F));
        }
        super.useCard(c, monster, energyOnUse);
    }

    @Override
    public void renderPlayerBattleUi(SpriteBatch sb) {
        super.renderPlayerBattleUi(sb);
        globalAttributes.render(sb);
    }

    @Override
    public void applyStartOfTurnRelics() {
        super.applyStartOfTurnRelics();
        globalAttributes.atStartOfTurn();
    }

    @Override
    public void renderPowerTips(SpriteBatch sb) {
        ArrayList<PowerTip> tips = new ArrayList<>();
        if (!this.stance.ID.equals("Neutral")) {
            tips.add(new PowerTip(this.stance.name, this.stance.description));
        }
        for (AbstractPower p : this.powers) {
            if (p.ID.equals("rhinemod:InvisibleGlobalAttributes")) continue;
            if (p.region48 != null) {
                tips.add(new PowerTip(p.name, p.description, p.region48));
            } else {
                tips.add(new PowerTip(p.name, p.description, p.img));
            }
        }
        if (!tips.isEmpty()) {
            if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
                TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + TipHelper.calculateAdditionalOffset(tips, this.hb.cY), tips);
            } else {
                TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + TipHelper.calculateAdditionalOffset(tips, this.hb.cY), tips);
            }
        }
    }
}