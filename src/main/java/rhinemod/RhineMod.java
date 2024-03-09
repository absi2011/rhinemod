package rhinemod;

import basemod.*;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import com.megacrit.cardcrawl.monsters.beyond.Repulsor;
import com.megacrit.cardcrawl.monsters.beyond.Spiker;
import com.megacrit.cardcrawl.monsters.city.BronzeOrb;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rhinemod.cards.special.*;
import rhinemod.cards.*;
import rhinemod.characters.RhineLab;
import rhinemod.events.HeartEvent;
import rhinemod.events.MysteriousInvent;
import rhinemod.monsters.*;
import rhinemod.patches.RhineEnum;
import rhinemod.relics.*;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static basemod.BaseMod.addMonster;
import static rhinemod.patches.AbstractCardEnum.RHINE_MATTE;

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

    private static final float NewMonsterMulti = 4.0F;

    public RhineMod() {
        BaseMod.subscribe(this);

        logger.info("addColor RHINE_MATTE");
        BaseMod.addColor(RHINE_MATTE,
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
        initializeMonsters();
    }

    public void initializeMonsters() {
        String[] names = CardCrawlGame.languagePack.getUIString("RunHistoryMonsterNames").TEXT;
        addMonster("Single ArcCommando", names[0], () -> new MonsterGroup(new AbstractMonster[] {new ArclightCommando(0.0F, 0.0F)}));
        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo("Single ArcCommando", 2.0F * NewMonsterMulti));
        addMonster("ArcCommando And Beckbeast",  names[1], () -> new MonsterGroup(new AbstractMonster[] {new ArclightCommando(-150.0F, 0.0F), new MilitaryBeckbeast(150.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("ArcCommando And Beckbeast", 1.5F * NewMonsterMulti));
        addMonster("Two Commando",  names[2], () -> new MonsterGroup(new AbstractMonster[] {new ArclightCommando(-150.0F, 0.0F), new ArclightCommando(150.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("Two Commando", 1.5F * NewMonsterMulti));
        addMonster("ArcMirrorGuard",  names[3], () -> new MonsterGroup(new AbstractMonster[] {new ArclightMirrorguard(0.0F, 0.0F)}));
        BaseMod.addEliteEncounter(Exordium.ID, new MonsterInfo("ArcMirrorGuard", 1.0F * NewMonsterMulti));

        addMonster("ArcVanguard",  names[4], () -> new MonsterGroup(new AbstractMonster[] {new ArclightVanguard(-150.0F, 0.0F),new ArclightVanguard(150.0F, 0.0F)}));
        BaseMod.addMonsterEncounter(TheCity.ID, new MonsterInfo("ArcVanguard", 2.0F * NewMonsterMulti));
        addMonster("ArcCommando And ArcVanguard",names[5],   () -> new MonsterGroup(new AbstractMonster[] {new ArclightCommando(-450.0F, 0.0F), new ArclightVanguard(-150.0F, 0.0F), new ArclightVanguard(150.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo("ArcCommando And ArcVanguard", 4.5F * NewMonsterMulti));
        addMonster("Perpetrator",  names[6], () -> new MonsterGroup(new AbstractMonster[] {new Perpetrator(0.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo("Perpetrator", 4.5F * NewMonsterMulti));
        addMonster("R PowerArmor", names[7],  () -> new MonsterGroup(new AbstractMonster[] {new R31HeavyPowerArmor(-500.F, 0.0F), new R11AssaultPowerArmor(-200.0F, 0.0F), }));
        BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo("R PowerArmor", 1.0F * NewMonsterMulti));

        addMonster("LightArmor and CityGuard",  names[8], () -> new MonsterGroup(new AbstractMonster[] {new TrimountsCityGuard(-150.F, 0.0F), new ExperimentalPowerArmor(150.0F, 0.0F)}));
        BaseMod.addMonsterEncounter(TheBeyond.ID, new MonsterInfo("LightArmor and CityGuard", 2.0F * NewMonsterMulti));
        addMonster("ArcTeam", names[9],  () -> new MonsterGroup(new AbstractMonster[] {new ArclightMirrorguard(-450.F, 0.0F), new ArclightCommando(-150.0F, 0.0F), new ArclightVanguard(150.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo("ArcTeam", 1.0F * NewMonsterMulti));
        addMonster("Jesselton", names[10],  () -> new MonsterGroup(new AbstractMonster[] {new JesseltonWilliams(0.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo("Jesselton", 1.0F * NewMonsterMulti));
        addMonster("Diaster of Machine", names[11], () -> new MonsterGroup(new AbstractMonster[] {new Perpetrator(-440.0F, 0.0F),new Exploder(-300.0F, 400.0F),  new Repulsor(-120.0F, 360.0F),  new Sentry(0.0F, 0.0F), new BronzeOrb(200.0F, 270.0F, 0), new Spiker(240.0F, 0.0F), new Crossroads(-220.0F, -50.0F)}));
        BaseMod.addEliteEncounter(TheBeyond.ID, new MonsterInfo("Diaster of Machine", 2.0F * NewMonsterMulti));

        addMonster("Awaken", names[12], () -> new MonsterGroup(new Awaken_Monster(0.0F, 0.0F)));
        BaseMod.addEliteEncounter(TheEnding.ID, new MonsterInfo("Awaken", 3.0F * NewMonsterMulti));
    }

    private void initializeEvents() {
        BaseMod.addEvent(new AddEventParams.Builder(MysteriousInvent.ID, MysteriousInvent.class).
                eventType(EventUtils.EventType.NORMAL).
                dungeonID("TheCity").
                dungeonID("TheBeyond").
                endsWithRewardsUI(false).
                spawnCondition(() -> (!(AbstractDungeon.player instanceof RhineLab))).
                create());
        BaseMod.addEvent(new AddEventParams.Builder(HeartEvent.ID, HeartEvent.class).
                eventType(EventUtils.EventType.NORMAL).
                dungeonID("TheSky").
                endsWithRewardsUI(false).
                create());
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
        BaseMod.addCard(new TransmitterResearch()); // 递质研究
        BaseMod.addCard(new IcefieldsAdventure()); // 冰原探险
        BaseMod.addCard(new BookOfFairyTales()); // 童话书
        BaseMod.addCard(new StarlightIntersection()); // 星束交汇
        BaseMod.addCard(new StarSculpt()); // 塑星
        BaseMod.addCard(new ShatteredVision()); // 破碎愿景
        BaseMod.addCard(new HighEfficiencyFreezingModule()); // 高效制冷模块

        // Uncommon.
        BaseMod.addCard(new PlanetaryDebris()); // 行星碎屑
        BaseMod.addCard(new HighSpeedRT()); // 高速共振排障
        BaseMod.addCard(new Starfall()); // 星辰坠落
        BaseMod.addCard(new PureWaterIsLife()); // 净水即生命
        BaseMod.addCard(new MedicineDispensing()); // 药物配置
        BaseMod.addCard(new Enkephalin()); // 脑啡肽
        BaseMod.addCard(new SHAFT()); // 能量井
        BaseMod.addCard(new EmergencyDefenseProcedures()); // 紧急防卫程序
        BaseMod.addCard(new TechnologyRisingStar()); // 科技新星
        BaseMod.addCard(new UnusedBoxingGloves()); // 闲置拳击手套
        BaseMod.addCard(new BionicDevice()); // 迷惑装置
        BaseMod.addCard(new NovaEruption()); // 新星爆发
        BaseMod.addCard(new ApplyFullForce()); // 放开手脚
        BaseMod.addCard(new TwoToOne()); // 二比一
        BaseMod.addCard(new EcologicalInteraction()); // 生态耦合
        BaseMod.addCard(new QuicksandGeneration()); // 流沙区域生成
        BaseMod.addCard(new Memory()); // 回忆

        // Rare.
        BaseMod.addCard(new Calcification()); // 钙质化
        BaseMod.addCard(new Solidify()); // 凝固
        BaseMod.addCard(new HallOfStasis()); // 静滞所
        BaseMod.addCard(new HeatDeath()); // 热寂
        BaseMod.addCard(new DreadnoughtProtocol()); // 无畏者协议
        BaseMod.addCard(new SuperficialRegulation()); // 浅层非熵适应
        BaseMod.addCard(new DancingInThrees()); // 三个人的舞

        // Special
        BaseMod.addCard(new Unscrupulous()); // 出格
        BaseMod.addCard(new Egotist()); // 自私者
        BaseMod.addCard(new Traitor()); // 背叛者
        BaseMod.addCard(new Seeker()); // 求道者
        BaseMod.addCard(new Loner()); // 独行者
        BaseMod.addCard(new Pioneer()); // 先驱者
        BaseMod.addCard(new GiantRing()); // 巨大环
        BaseMod.addCard(new BipolarNebula()); // 双极星云
        BaseMod.addCard(new StellarRing()); // 恒星环
        BaseMod.addCard(new SquareSunflower()); // 方形葵
        BaseMod.addCard(new IcefieldsCottongrass()); // 冰原棉草
        BaseMod.addCard(new Sarracenia()); // 雪瓶子草
        BaseMod.addCard(new SheathedBeech()); // 鞘叶榉
        BaseMod.addCard(new PaleFir()); // 淡杉
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

        // common.

        // uncommon.

        // rare.
        BaseMod.addRelicToCustomPool(new Awaken(), RHINE_MATTE);
        BaseMod.addRelicToCustomPool(new Stargate(), RHINE_MATTE);

        // boss.

        // event.
        BaseMod.addRelicToCustomPool(new LoneTrail(), RHINE_MATTE);

        // shop.
        BaseMod.addRelicToCustomPool(new ThreeDimensionArtDisplay(), RHINE_MATTE);
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

    public static ArrayList<AbstractCard> getPlantCards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new SquareSunflower());
        list.add(new SheathedBeech());
        list.add(new IcefieldsCottongrass());
        list.add(new Sarracenia());
        list.add(new PaleFir());
        return list;
    }

    public static void applyEnemyPowersOnly(DamageInfo info, AbstractCreature target) {
        info.isModified = false;
        float tmp = (float)info.base;

        for (AbstractPower p:target.powers) {
            tmp = p.atDamageReceive(tmp, info.type);
        }

        for (AbstractPower p:target.powers) {
            tmp = p.atDamageFinalReceive(tmp, info.type);
        }

        if (tmp < 0.0F) {
            tmp = 0.0F;
        }
        info.output = MathUtils.floor(tmp);
        if (info.base != info.output) {
            info.isModified = true;
        }
    }
}