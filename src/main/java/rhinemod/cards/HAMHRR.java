package rhinemod.cards;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import rhinemod.actions.HAMHRRAction;
import rhinemod.actions.SHAFTAction;
import rhinemod.cards.special.Traitor;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class HAMHRR extends AbstractRhineCard {
    public static final String ID = "rhinemod:HAMHRR";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/HAMHRR.png";
    public static final int COST = 3;
    public static final int BASIC_DAMAGE = 18;
    public static final int EXTRA_DAMAGE = 36;
    public static final int UPGRADE_PLUS_EXTRA = 18;
    public HAMHRR() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = BASIC_DAMAGE;
        baseMagicNumber = magicNumber = EXTRA_DAMAGE;
        realBranch = 2; // 先做成克总的牌吧
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Copied from 扫荡射线
        this.addToBot(new SFXAction("ATTACK_DEFECT_BEAM"));
        this.addToBot(new VFXAction(p, new SweepingBeamEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractDungeon.player.flipHorizontal), 0.4F));
        addToBot(new HAMHRRAction(p, m, damage, magicNumber));
    }

    @Override
    public void applyPowers() {
        int temp = baseDamage;
        baseDamage = baseMagicNumber;
        super.applyPowers();
        magicNumber = damage;
        isMagicNumberModified = isDamageModified;
        baseDamage = temp;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int temp = baseDamage;
        baseDamage = baseMagicNumber;
        super.calculateCardDamage(mo);
        magicNumber = damage;
        isMagicNumberModified = isDamageModified;
        baseDamage = temp;
        super.calculateCardDamage(mo);
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_PLUS_EXTRA);
                    initializeDescription();
                }
            });
        }};
    }
}
