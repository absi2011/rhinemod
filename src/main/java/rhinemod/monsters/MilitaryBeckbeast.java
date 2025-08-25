package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.BarricadePower;
import rhinemod.RhineMod;
import rhinemod.powers.DefendBackPower;

public class MilitaryBeckbeast extends AbstractRhineMonster {
    public static final String ID = "rhinemod:MilitaryBeckbeast";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public int AttackBlock;
    public int OnlyBlock;

    public MilitaryBeckbeast(float x, float y) {
        super(NAME, ID, 30, 0, 0, 170.0F, 200.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(32);
            AttackBlock = 6;
            OnlyBlock = 12;
        }
        else {
            AttackBlock = 5;
            OnlyBlock = 10;
        }
        if (RhineMod.tagLevel >= 1) {
            AttackBlock += 2;
            OnlyBlock += 2;
        }
        if (RhineMod.tagLevel >= 2) {
            AttackBlock += 2;
            OnlyBlock += 2;
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 5));
        }
        else if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 4));
        }
        else {
            damage.add(new DamageInfo(this, 3));
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1325_cbgpro/enemy_1325_cbgpro33.atlas", "resources/rhinemod/images/monsters/enemy_1325_cbgpro/enemy_1325_cbgpro33.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (RhineMod.tagLevel >= 3) {
            addToBot(new ApplyPowerAction(this, this, new BarricadePower(this)));
            addToBot(new ApplyPowerAction(this, this, new DefendBackPower(this)));
        }
    }

    @Override
    public void addCCTags() {
        if (RhineMod.tagLevel >= 2) {
            addTag(2);
        }
        else if (RhineMod.tagLevel == 1) {
            addTag(1);
        }
    }

    @Override
    public void takeTurn() {
        if (this.nextMove == 2) {
            state.setAnimation(0, "Attack", false);
            state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            addToBot(new GainBlockAction(this, AttackBlock));
        }
        else {
            addToBot(new GainBlockAction(this, OnlyBlock));
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        int nextMove = AbstractDungeon.aiRng.random(0, 1);
        if (nextMove == 1) {
            setMove((byte)1, Intent.DEFEND);
        }
        else {
            setMove((byte)2, Intent.ATTACK_DEFEND, damage.get(0).base);
        }
    }
}
