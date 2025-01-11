package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.characters.RhineLab;
import rhinemod.powers.NoStun;
import rhinemod.vfx.R11MoveEffect;

public class R11AssaultPowerArmor extends AbstractRhineMonster {
    public static final String ID = "rhinemod:R11";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static float moveX;

    public R11AssaultPowerArmor(float x, float y) {
        super(NAME, ID, 140, 0, 0, 220.0F, 360.0F, null, x, y);
        type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(150);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            damage.add(new DamageInfo(this, 12));
            damage.add(new DamageInfo(this, 24));
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            damage.add(new DamageInfo(this, 11));
            damage.add(new DamageInfo(this, 22));
        } else {
            damage.add(new DamageInfo(this, 10));
            damage.add(new DamageInfo(this, 20));
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1256_lyacpa/enemy_1256_lyacpa33.atlas", "resources/rhinemod/images/monsters/enemy_1256_lyacpa/enemy_1256_lyacpa33.json", 1.5F);
        state.setAnimation(0, "Idle", true);
        moveX = (drawX - hb.width * 1.5F - (AbstractDungeon.player.hb.cX + AbstractDungeon.player.hb.width / 2)) / 6;
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new NoStun(this)));
    }

    @Override
    public void takeTurn() {
        if (nextMove == 7) {
            state.setAnimation(0, "Attack_2", false);
            state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(1)));
        } else {
            state.setAnimation(0, "Attack", false);
            moveX = (drawX - hb.width * 0.6F - (AbstractDungeon.player.hb.cX + AbstractDungeon.player.hb.width * 0.6F)) / (7 - nextMove);
            AbstractCreature lastOne = AbstractDungeon.player;
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m == this) {
                    float limit;
                    limit = lastOne.drawX + lastOne.hb.width * 0.6F + hb.width * 0.6F;
                    if (lastOne instanceof RhineLab) {
                        limit += lastOne.hb.width * 0.1F;
                    }
                    moveX = Math.min(moveX, drawX - limit);
                    break;
                } else {
                    if (!m.isDeadOrEscaped()) {
                        lastOne = m;
                    }
                }
            }
            if (moveX > 0) {
                AbstractDungeon.effectList.add(new R11MoveEffect(this, moveX));
            }
            state.addAnimation(0, "Move", false, 0);
            state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        }
        if (nextMove <= 5) {
            setMove(MOVES[0], (byte)(nextMove + 1), Intent.ATTACK, damage.get(0).base);
        } else {
            setMove((byte)7, Intent.ATTACK, damage.get(1).base);
        }
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte)1, Intent.ATTACK, damage.get(0).base);
    }
}
