package rhinemod;

import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import rhinemod.characters.RhineLab;
import rhinemod.patches.RhineEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rhinemod.cards.*;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static rhinemod.patches.AbstractCardEnum.RHINE;

@SpireInitializer
public class RhineMod implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostBattleSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, AddCustomModeModsSubscriber, OnStartBattleSubscriber, OnPlayerLoseBlockSubscriber, RelicGetSubscriber {

    public static final Color RhineMatte = CardHelper.getColor(188, 187, 145);
    private static final String attackCard = "images/512/bg_attack_rhine.png";
    private static final String skillCard = "images/512/bg_skill_rhine.png";
    private static final String powerCard = "images/512/bg_power_rhine.png";
    private static final String energyOrb = "images/512/card_nearl_orb.png";
    private static final String attackCardPortrait = "images/1024/bg_attack_rhine.png";
    private static final String skillCardPortrait = "images/1024/bg_skill_rhine.png";
    private static final String powerCardPortrait = "images/1024/bg_power_rhine.png";
    private static final String energyOrbPortrait = "images/1024/card_nearl_orb.png";
    private static final String charButton = "images/charSelect/button.png";
    private static final String charPortrait = "images/charSelect/portrait.png";
    private static final String miniManaSymbol = "images/manaSymbol.png";
    private static final Logger logger = LogManager.getLogger(RhineMod.class.getName());

    public RhineMod() {
        BaseMod.subscribe(this);

        logger.info("addColor RHINE");
        BaseMod.addColor(RHINE,
                RhineMatte, RhineMatte, RhineMatte, RhineMatte, RhineMatte, RhineMatte, RhineMatte,   //Background color, back color, frame color, frame outline color, description box color, glow color
                attackCard, skillCard, powerCard, energyOrb,                                   //attack background image, skill background image, power background image, energy orb image
                attackCardPortrait, skillCardPortrait, powerCardPortrait, energyOrbPortrait,   //as above, but for card inspect view
                miniManaSymbol);
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        new RhineMod();
    }

    @Override
    public void receivePostInitialize() {
        initializeEvents();
        initializePotions();
    }

    private void initializeEvents() {
    }

    private void initializePotions() {
//        BaseMod.addPotion(BottledCommand.class, Color.SKY, null, null, BottledCommand.ID, nearlmod.patches.RhineEnum.NEARL_CLASS);
    }

    @Override
    public void receivePostDungeonInitialize() {}

    @Override
    public void receiveCustomModeMods(List<CustomMod> modList) {
    }

    @Override
    public int receiveOnPlayerLoseBlock(int cnt) {
        return cnt;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {}

    @Override
    public void receivePostBattle(final AbstractRoom p0) {}

    @Override
    public void receiveRelicGet(AbstractRelic r) {}

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new AbstractRhineCard.SecondMagicNumber());

        // Basic.
        BaseMod.addCard(new RhineStrike()); // 打击
        BaseMod.addCard(new RhineDefend()); // 防御
        BaseMod.addCard(new Destiny()); // 命运
        BaseMod.addCard(new DefenseSection()); // 防卫科
        BaseMod.addCard(new ComponentsControlSection()); // 总辖构件科
        BaseMod.addCard(new EcologicalSection()); // 生态科

        // Common.
        BaseMod.addCard(new DangerousEntityRemoval()); // 危险目标清除
        BaseMod.addCard(new ProgressiveMoisturization()); // 渐进性润化

        // Uncommon.
        BaseMod.addCard(new PlanetaryDebris()); // 行星碎屑
        BaseMod.addCard(new HighSpeedRT()); // 高速共振排障
        BaseMod.addCard(new Starfall()); // 星辰坠落
        BaseMod.addCard(new PureWaterIsLife()); // 净水即生命

        // Rare.
        BaseMod.addCard(new Calcification()); // 钙质化
        BaseMod.addCard(new Solidify()); // 凝固

        // Special

    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new RhineLab(CardCrawlGame.playerName), charButton, charPortrait, RhineEnum.RHINE_CLASS);
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String keywordStrings = Gdx.files.internal("strings/" + getLang() + "/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

        Map<String, Keyword> keywords = gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
            logger.info("Adding Keyword - " + v.NAMES[0]);
            BaseMod.addKeyword("rhinemod:", v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveEditRelics() {
        // starter.
//        BaseMod.addRelicToCustomPool(new CureUp(), RHINE);

        // common.

        // uncommon.

        // rare.

        // boss.

        // event.
    }

    @Override
    public void receiveEditStrings() {
        String lang = getLang();
        String cardStrings = Gdx.files.internal("strings/" + lang + "/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String characterStrings = Gdx.files.internal("strings/" + lang + "/character.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
        String powerStrings = Gdx.files.internal("strings/" + lang + "/powers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String relicStrings = Gdx.files.internal("strings/" + lang + "/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String eventStrings = Gdx.files.internal("strings/" + lang + "/events.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
        String monsterStrings = Gdx.files.internal("strings/" + lang + "/monsters.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        String uiStrings = Gdx.files.internal("strings/" + lang + "/ui.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
        String potionStrings = Gdx.files.internal("strings/" + lang + "/potions.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
    }

    private String getLang() {
//        String lang = "eng";
//        if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
//            lang = "zhs";
//        }
//        return lang;
        return "zhs";
    }
}