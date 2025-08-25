package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import rhinemod.RhineMod;
import rhinemod.actions.AddMaxHpAction;

public class TrimountsCityGuard extends AbstractRhineMonster {
    public static final String ID = "rhinemod:TrimountsCityGuard";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;

    public int debuffNum = 1;
    public TrimountsCityGuard(float x, float y) {
        super(NAME, ID, 39, 0, 0, 150.0F, 260.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (RhineMod.tagLevel >= 3) {
            debuffNum ++;
        }
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
        loadAnimation("resources/rhinemod/images/monsters/enemy_1326_cbagen/enemy_1326_cbagen33.atlas", "resources/rhinemod/images/monsters/enemy_1326_cbagen/enemy_1326_cbagen33.json", 1.5F);
        state.addAnimation(0, "Idle", true, 0.0F);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (RhineMod.tagLevel >= 2) {
            addToBot(new AddMaxHpAction(this, 40));
        }
        else if (RhineMod.tagLevel >= 1) {
            addToBot(new AddMaxHpAction(this, 20));
        }
    }

    @Override
    public void takeTurn() {
        state.setAnimation(0, "Attack", false);
        state.addAnimation(0, "Idle", true, 0);
        addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        int maxDebuff = 5;
        if (!AbstractDungeon.player.orbs.isEmpty()) {
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
            addToBot(new ApplyPowerAction(p, this, new WeakPower(p, debuffNum, true)));
        }
        else if (id == 2) {
            addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, debuffNum, true)));
        }
        else if (id == 3) {
            addToBot(new ApplyPowerAction(p, this, new FrailPower(p, debuffNum, true)));
        }
        else if (id == 4) {
            addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, -debuffNum)));
        }
        else if (id == 5) {
            addToBot(new ApplyPowerAction(p, this, new DexterityPower(p, -debuffNum)));
        }
        else {
            addToBot(new ApplyPowerAction(p, this, new FocusPower(p, -debuffNum)));
        }
    }

    @Override
    public void addCCTags() {
        if (RhineMod.tagLevel == 1) {
            addTag(1);
        }
        if (RhineMod.tagLevel == 2) {
            addTag(2);
        }
        if (RhineMod.tagLevel == 3) {
            addTag(2);
            addTag(3);
        }
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK_DEBUFF, damage.get(0).base);
    }
}
