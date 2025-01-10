package rhinemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.RhineMod;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

public class TwoToOne extends AbstractRhineCard {
    public static final String ID = "rhinemod:TwoToOne";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/TwoToOne.png";
    public static final String IMG_K = "resources/rhinemod/images/cards/OneToTwo.png";
    public static final int COST = 1;
    public static final int ATTACK_DMG = 11;
    public static final int UPGRADE_PLUS_DMG = 5;
    public TwoToOne() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (!super.canPlay(card)) return false;
        if (!(card instanceof TwoToOne)) return true;
        int[] cnt = new int[4];
        for (AbstractCard c : AbstractDungeon.player.hand.group) cnt[RhineMod.getBranch(c)]++;
        boolean hasHalf = false;
        for (int i = 1; i <= 3; i++)
            if (cnt[i] * 2 > AbstractDungeon.player.hand.group.size()) {
                hasHalf = true;
                break;
            }
        if (chosenBranch == 0) return !hasHalf;
        else return hasHalf;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeDamage(UPGRADE_PLUS_DMG);
                    initializeDescription();
                }
            });
            add(() -> {
                if (!upgraded) {
                    upgradeName(2, EXTENDED_DESCRIPTION[0]);
                    rawDescription = EXTENDED_DESCRIPTION[1];
                    upgradeDamage(UPGRADE_PLUS_DMG);
                    textureImg = IMG_K;
                    loadCardImage(textureImg);
                    initializeDescription();
                }
            });
        }};
    }
}
