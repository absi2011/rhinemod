package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import rhinemod.powers.Fragile;

public class TrimountsCityGuard extends CustomMonster {
    public static final String ID = "rhinemod:TrimountsCityGuard";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public TrimountsCityGuard(float x, float y) {
        super(NAME, ID, 39, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(42);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 5));
        }
        else if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 5));
        }
        else {
            damage.add(new DamageInfo(this, 4));
        }
        loadAnimation("images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    @Override
    public void usePreBattleAction() {
    }

    @Override
    public void takeTurn() {
        addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        int maxDebuff = 5;
        if (AbstractDungeon.player.hasOrb()) {
            maxDebuff ++;
        }
        int debuff = AbstractDungeon.aiRng.random(1, maxDebuff);
        int anotherDebuff = AbstractDungeon.aiRng.random(1, maxDebuff-1);
        if (anotherDebuff >= debuff) {
            anotherDebuff++;
        }
        applyDebuff(debuff);
        if (AbstractDungeon.ascensionLevel >= 17) {
            applyDebuff(anotherDebuff);
        }
        getMove(0);
    }

    void applyDebuff(int id) {
        AbstractPlayer p = AbstractDungeon.player;
        if (id == 1) {
            addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, true)));
        }
        else if (id == 2) {
            addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, 1, true)));
        }
        else if (id == 3) {
            addToBot(new ApplyPowerAction(p, this, new FrailPower(p, 1, true)));
        }
        else if (id == 4) {
            addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, -1)));
        }
        else if (id == 5) {
            addToBot(new ApplyPowerAction(p, this, new DexterityPower(p, -1)));
        }
        else {
            addToBot(new ApplyPowerAction(p, this, new FocusPower(p, -1)));
        }
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK_DEBUFF, damage.get(0).base);
    }
}
