package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class MilitaryBeckbeast extends CustomMonster {
    public static final String ID = "rhinemod:MilitaryBeckbeast";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public final int AttackBlock;
    public final int OnlyBlock;

    public MilitaryBeckbeast(float x, float y) {
        super(NAME, ID, 30, 0, 0, 150.0F, 320.0F, null, x, y);
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
        if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 5));
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 4));
        }
        else {
            damage.add(new DamageInfo(this, 3));
        }
        loadAnimation("images/monsters/enemy_1325_cbgpro/enemy_1325_cbgpro33.atlas", "images/monsters/enemy_1325_cbgpro/enemy_1325_cbgpro33.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        flipHorizontal = true;
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
