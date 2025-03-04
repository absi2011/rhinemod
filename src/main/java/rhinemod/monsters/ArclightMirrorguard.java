package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ShiftingPower;
import rhinemod.powers.DamageOutPower;

public class ArclightMirrorguard extends AbstractRhineMonster {
    public static final String ID = "rhinemod:ArclightMirrorguard";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;

    public final int damageOut;

    public ArclightMirrorguard(float x, float y) {
        super(NAME, ID, 70, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(76);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            damage.add(new DamageInfo(this, 20));
            damageOut = 66;
        }
        else if (AbstractDungeon.ascensionLevel >= 3) {
            damage.add(new DamageInfo(this, 20));
            damageOut = 60;
        }
        else {
            damage.add(new DamageInfo(this, 18));
            damageOut = 60;
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1329_cbshld/enemy_1329_cbshld.atlas", "resources/rhinemod/images/monsters/enemy_1329_cbshld/enemy_1329_cbshld37.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new ShiftingPower(this)));
        addToBot(new ApplyPowerAction(this, this, new DamageOutPower(this, damageOut, 3)));
    }

    @Override
    public void takeTurn() {
        state.setAnimation(0, "Attack", false);
        state.addAnimation(0, "Idle", true, 0);
        addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }
}
