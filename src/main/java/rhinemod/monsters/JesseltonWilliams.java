package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import rhinemod.actions.JesseltonUpdateHitboxAction;
import rhinemod.powers.WeaknessSmash;
import rhinemod.powers.WeaknessNonSmash;

public class JesseltonWilliams extends AbstractRhineMonster {
    public static final String ID = "rhinemod:JesseltonWilliams";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    public final int DazedNum;
    public boolean isStage2;
    private boolean notTriggered;

    public final int metalNum;

    public JesseltonWilliams(float x, float y) {
        super(NAME, ID, 300, 0, 0, 210.0F, 340.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(320);
            metalNum = 8;
        } else {
            metalNum = 6;
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 6));
            damage.add(new DamageInfo(this, 16));
            DazedNum = 3;
        }
        else if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 6));
            damage.add(new DamageInfo(this, 14));
            DazedNum = 2;
        }
        else {
            damage.add(new DamageInfo(this, 5));
            damage.add(new DamageInfo(this, 13));
            DazedNum = 2;
        }
        isStage2 = false;
        notTriggered = true;
        loadAnimation("resources/rhinemod/images/monsters/enemy_1516_jakill/enemy_1516_jakill37.atlas", "resources/rhinemod/images/monsters/enemy_1516_jakill/enemy_1516_jakill37.json", 1.5F);
        stateData.setMix("C1_Idle", "C1_Die", 0.1F);
        stateData.setMix("C2_Idle", "C2_Die", 0.1F);
        state.setAnimation(0, "C1_Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new WeaknessSmash(this)));
        CardCrawlGame.music.fadeOutTempBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly("m_bat_jakiller_combine.mp3", true);
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if ((currentHealth <= maxHealth / 2) && notTriggered) {
            setMove(MOVES[2], (byte)3, Intent.UNKNOWN);
            createIntent();
            notTriggered = false;
        }
    }

    public void changeState(String stateName) {
        if (stateName.equals("KILLER")) {
            state.setAnimation(0, "C1_Die", false);
            state.addAnimation(0, "C2_Idle", true, 0F);
            addToBot(new WaitAction(4));
            addToBot(new HealAction(this, this, maxHealth / 2 - currentHealth));
            addToBot(new RemoveSpecificPowerAction(this, this, WeaknessSmash.POWER_ID));
            addToBot(new ApplyPowerAction(this, this, new WeaknessNonSmash(this)));
            addToBot(new ApplyPowerAction(this, this, new MetallicizePower(this, metalNum)));
            addToBot(new JesseltonUpdateHitboxAction(this));
        }
    }

    public void updateHitbox() {
        hb_w *= 2;
        hb.width *= 2;
        healthHb.width *= 2;
        healthBarUpdatedEvent();
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (nextMove == 1) {
            addToBot(new DamageAction(p, damage.get(0)));
            addToBot(new MakeTempCardInDiscardAction(new Dazed(), DazedNum));
            state.setAnimation(0, "C1_Skill", false);
            state.addAnimation(0, "C1_Idle", true, 0F);
        } else if (nextMove == 2) {
            addToBot(new DamageAction(p, damage.get(1)));
            addToBot(new DamageAction(p, damage.get(1)));
            state.setAnimation(0, "C2_Skill", false);
            state.addAnimation(0, "C2_Idle", true, 0F);
        } else {
            isStage2 = true;
            addToBot(new ChangeStateAction(this, "KILLER"));
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        if (isStage2)
            setMove(MOVES[1], (byte)2, Intent.ATTACK, damage.get(1).base, 2, true);
        else
            setMove(MOVES[0], (byte)1, Intent.ATTACK_DEBUFF, damage.get(0).base);
    }
}
