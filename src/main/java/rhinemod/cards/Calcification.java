package rhinemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rhinemod.actions.AddCalciumAction;
import rhinemod.characters.AbstractCharacterSpine;
import rhinemod.characters.RhineLab;
import rhinemod.patches.AbstractCardEnum;
import rhinemod.interfaces.UpgradeBranch;
import rhinemod.powers.ReduceCalcium;

import java.util.ArrayList;
import java.util.List;

public class Calcification extends AbstractRhineCard {
    public static final String ID = "rhinemod:Calcification";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "resources/rhinemod/images/cards/Calcification.png";
    public static final int COST = 3;
    public static final int CA_ADD = 18;
    public static final int CA_RED = 3;
    public static final int UPGRADE_PLUS_CA = 6;
    public Calcification() {
        super(ID, NAME, IMG, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.RHINE_MATTE,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = CA_ADD;
        secondMagicNumber = baseSecondMagicNumber = CA_RED;
        realBranch = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardCrawlGame.sound.play("CALCIFICATION");
        if (p instanceof RhineLab) {
            AbstractCharacterSpine sp = ((RhineLab) p).spines.get("S");
            sp.idle = "Skill_Loop";
            sp.state.setAnimation(0, "Skill_Begin", false);
            sp.state.addAnimation(0, sp.idle, true, 0);
        }
        addToBot(new AddCalciumAction(magicNumber));
        addToBot(new ApplyPowerAction(p, p, new ReduceCalcium(p, CA_RED)));
    }

    @Override
    public List<UpgradeBranch> possibleBranches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                if (!upgraded) {
                    upgradeName(0);
                    upgradeMagicNumber(UPGRADE_PLUS_CA);
                    initializeDescription();
                }
            });
        }};
    }
}
