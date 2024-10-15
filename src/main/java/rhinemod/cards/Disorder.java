package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.patches.AbstractCardEnum;

import java.util.*;

public class Disorder extends AbstractRhineCard {
    public static final String ID = "rhinemod:Disorder";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/Memory.png";
    public static ArrayList<Integer> DATA = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
    public Disorder() {
        super(ID, NAME, IMG, DATA.get(0), DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.ENEMY);
        damage = baseDamage = DATA.get(3);
        block = baseBlock = DATA.get(2);
        magicNumber = baseMagicNumber = DATA.get(4);
        secondMagicNumber = baseSecondMagicNumber = DATA.get(1);
        isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < secondMagicNumber; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage)));
            addToBot(new GainBlockAction(p, block));
            addToBot(new DrawCardAction(magicNumber));
        }
    }

    @Override
    public void triggerWhenDrawn() {
        shuffle();
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        shuffle();
    }

    void shuffle(){
        Collections.shuffle(DATA, new Random(AbstractDungeon.cardRng.randomLong()));
        cost = costForTurn = DATA.get(0);
        damage = baseDamage = DATA.get(1);
        block = baseBlock = DATA.get(2);
        magicNumber = baseMagicNumber = DATA.get(3);
        secondMagicNumber = baseSecondMagicNumber = DATA.get(4);
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    isEthereal = false;
                    rawDescription = UPGRADE_DESCRIPTION;
                    initializeDescription();
                }
            });
        }};
    }
}
