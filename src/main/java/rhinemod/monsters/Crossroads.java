package rhinemod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Crossroads extends CustomMonster {
    public static final String ID = "rhinemod:Crossroads";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public int blockNum;
    public int strNum;
    public boolean firstTime = true;

    public Crossroads(float x, float y) {
        super(NAME, ID, 80, 0, 0, 150.0F, 320.0F, null, x, y);
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
        loadAnimation("images/monsters/enemy_2056_smedzi/enemy_2056_smedzi.atlas", "images/monsters/enemy_2056_smedzi/enemy_2056_smedzi33.json", 2F);
        this.stateData.setMix("Idle", "Move_Begin", 0.1F);
        this.state.setAnimation(0, "Move_End", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    @Override
    public void takeTurn() {
        for (AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new GainBlockAction(m, this, blockNum));
            addToBot(new ApplyPowerAction(m,this, new StrengthPower(m, strNum)));
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
