package rhinemod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;

public class DreamBreakPower extends AbstractRhinePower {
    public static final String POWER_ID = "rhinemod:DreamBreakPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public DreamBreakPower(AbstractCreature owner,int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Armor 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/rhinemod/images/powers/Armor 32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onDeath() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.maxHealth <= amount) {
            p.isDead = true;
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            p.maxHealth = 0;
            p.currentHealth = 0;
            if (p.currentBlock > 0) {
                p.loseBlock();
            }
            return;
        }
        AbstractDungeon.player.currentHealth -= amount;
        AbstractDungeon.player.decreaseMaxHealth(amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
