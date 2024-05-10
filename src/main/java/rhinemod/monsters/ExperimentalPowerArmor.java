package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import rhinemod.powers.HalfDamage;

public class ExperimentalPowerArmor extends CustomMonster {
    public static final String ID = "rhinemod:ExperimentalPowerArmor";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private boolean moveAnim;

    public ExperimentalPowerArmor(float x, float y) {
        super(NAME, ID, 50, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(54);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 8));
        }
        else if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 7));
        }
        else {
            damage.add(new DamageInfo(this, 6));
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1254_lypa/enemy_1254_lypa33.atlas", "resources/rhinemod/images/monsters/enemy_1254_lypa/enemy_1254_lypa33.json", 1.5F);
        state.addAnimation(0, "Idle", true, 0.0F);
        flipHorizontal = true;
        moveAnim = false;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new HalfDamage(this)));
    }

    @Override
    public void takeTurn() {
        if (moveAnim) {
            state.setAnimation(0, "Attack_B", false);
        } else {
            state.setAnimation(0, "Attack_A", false);
        }
        moveAnim = !moveAnim;
        state.addAnimation(0, "Idle", true, 0);
        addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }
}
