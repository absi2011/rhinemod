package rhinemod.monsters;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rhinemod.RhineMod;
import rhinemod.actions.MonsterMoveAction;
import rhinemod.powers.CrossroadsThorns;

public class Crossroads extends AbstractRhineMonster {
    public static final String ID = "rhinemod:Crossroads";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public int blockNum;
    public int strNum;
    public boolean firstTime = true;

    public Crossroads(float x, float y) {
        super(NAME, ID, 80, 0, 0, 150.0F, 220.0F, null, x, y);
        type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(88);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            blockNum = 8;
            strNum = 2;
        }
        else if (AbstractDungeon.ascensionLevel >= 3) {
            blockNum = 8;
            strNum = 1;
        }
        else {
            blockNum = 6;
            strNum = 1;
        }
        loadAnimation("resources/rhinemod/images/monsters/enemy_1331_cbsisy/enemy_1331_cbsisy33.atlas", "resources/rhinemod/images/monsters/enemy_1331_cbsisy/enemy_1331_cbsisy33.json", 2F);
        state.setAnimation(0, "Idle", true);
        flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (RhineMod.tagLevel >= 2) {
            addToBot(new ApplyPowerAction(this, this, new CrossroadsThorns(this)));
        }
        if (RhineMod.tagLevel == 3) {
            addToBot(new MonsterMoveAction(this, 5));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (hasPower(CrossroadsThorns.POWER_ID)) {
            getPower(CrossroadsThorns.POWER_ID).updateDescription();
        }
    }

    @Override
    public void takeTurn() {
        for (AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDeadOrEscaped()) {
                addToBot(new GainBlockAction(m, this, blockNum));
                addToBot(new ApplyPowerAction(m,this, new StrengthPower(m, strNum)));
            }
        }
        AbstractPlayer p = AbstractDungeon.player;
        if (p instanceof Defect) {
            addToBot(new GainBlockAction(p, this, blockNum));
            addToBot(new ApplyPowerAction(p,this, new FocusPower(p, strNum)));
            if (firstTime) {
                addToBot(new TalkAction(p, DIALOG[0]));
                firstTime = false;
            }
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.BUFF);
    }
}
