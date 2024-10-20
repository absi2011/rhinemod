package rhinemod;

import basemod.*;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
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
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rhinemod.cards.special.*;
import rhinemod.cards.*;
import rhinemod.characters.RhineLab;
import rhinemod.events.*;
import rhinemod.monsters.*;
import rhinemod.patches.RhineEnum;
import rhinemod.potions.*;
import rhinemod.powers.InvisibleGlobalAttributes;
import rhinemod.relics.*;
import rhinemod.util.TheSky;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static basemod.BaseMod.addMonster;
import static rhinemod.patches.AbstractCardEnum.RHINE_MATTE;

@SpireInitializer
public class RhineMod implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostBattleSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, AddCustomModeModsSubscriber, OnStartBattleSubscriber, OnPlayerLoseBlockSubscriber, RelicGetSubscriber {

    public static final Color RhineMatte = CardHelper.getColor(188, 187, 145);
    private static final String attackCard = "resources/rhinemod/images/512/bg_attack_rhine.png";
    private static final String skillCard = "resources/rhinemod/images/512/bg_skill_rhine.png";
    private static final String powerCard = "resources/rhinemod/images/512/bg_power_rhine.png";
    private static final String energyOrb = "resources/rhinemod/images/512/card_rhine_orb.png";
    private static final String attackCardPortrait = "resources/rhinemod/images/1024/bg_attack_rhine.png";
    private static final String skillCardPortrait = "resources/rhinemod/images/1024/bg_skill_rhine.png";
    private static final String powerCardPortrait = "resources/rhinemod/images/1024/bg_power_rhine.png";
    private static final String energyOrbPortrait = "resources/rhinemod/images/1024/card_rhine_orb.png";
    private static final String charButton = "resources/rhinemod/images/charSelect/button.png";
    private static final String charPortrait = "resources/rhinemod/images/charSelect/portrait.png";
    private static final String miniManaSymbol = "resources/rhinemod/images/manaSymbol.png";
    private static final Logger logger = LogManager.getLogger(RhineMod.class.getName());

    public static final ArrayList<TextureAtlas.AtlasRegion> specialImg = new ArrayList<>();

    private static final float NewMonsterMulti = 1.0F;
    public boolean isDemo = true;

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
        initializeSpecialImg();
    }

    public void initializeMonsters() {
        String[] names = CardCrawlGame.languagePack.getUIString("rhinemod:RunHistoryMonsterNames").TEXT;
        addMonster("Single ArcCommando", names[0], () -> new MonsterGroup(new AbstractMonster[] {new ArclightCommando(0.0F, 0.0F)}));
        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo("Single ArcCommando", 2.0F * NewMonsterMulti));
        addMonster("ArcCommando And Beckbeast",  names[1], () -> new MonsterGroup(new AbstractMonster[] {new ArclightCommando(-150.0F, 0.0F), new MilitaryBeckbeast(150.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("ArcCommando And Beckbeast", 1.5F * NewMonsterMulti));
        addMonster("Two Commando",  names[2], () -> new MonsterGroup(new AbstractMonster[] {new ArclightCommando(-150.0F, 0.0F), new ArclightCommando(150.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("Two Commando", 1.5F * NewMonsterMulti));
        addMonster("ArcMirrorGuard",  names[3], () -> new MonsterGroup(new AbstractMonster[] {new ArclightMirrorguard(0.0F, 0.0F)}));
        BaseMod.addEliteEncounter(Exordium.ID, new MonsterInfo("ArcMirrorGuard", NewMonsterMulti));

        addMonster("ArcVanguard",  names[4], () -> new MonsterGroup(new AbstractMonster[] {new ArclightVanguard(-150.0F, 0.0F),new ArclightVanguard(150.0F, 0.0F)}));
        BaseMod.addMonsterEncounter(TheCity.ID, new MonsterInfo("ArcVanguard", 2.0F * NewMonsterMulti));
        addMonster("ArcCommando And ArcVanguard",names[5],   () -> new MonsterGroup(new AbstractMonster[] {new ArclightCommando(-450.0F, 0.0F), new ArclightVanguard(-150.0F, 0.0F), new ArclightVanguard(150.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo("ArcCommando And ArcVanguard", 4.5F * NewMonsterMulti));
        addMonster("Perpetrator",  names[6], () -> new MonsterGroup(new AbstractMonster[] {new Perpetrator(0.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo("Perpetrator", 4.5F * NewMonsterMulti));
        addMonster("R PowerArmor", names[7],  () -> new MonsterGroup(new AbstractMonster[] {new R31HeavyPowerArmor(-500.F, 0.0F), new R11AssaultPowerArmor(-200.0F, 0.0F), }));
        BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo("R PowerArmor", NewMonsterMulti));

        addMonster("LightArmor and CityGuard",  names[8], () -> new MonsterGroup(new AbstractMonster[] {new TrimountsCityGuard(-150.F, 0.0F), new ExperimentalPowerArmor(150.0F, 0.0F)}));
        BaseMod.addMonsterEncounter(TheBeyond.ID, new MonsterInfo("LightArmor and CityGuard", 2.0F * NewMonsterMulti));
        addMonster("ArcTeam", names[9],  () -> new MonsterGroup(new AbstractMonster[] {new ArclightMirrorguard(-450.F, 0.0F), new ArclightCommando(-150.0F, 0.0F), new ArclightVanguard(150.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo("ArcTeam", NewMonsterMulti));
        addMonster("Jesselton", names[10],  () -> new MonsterGroup(new AbstractMonster[] {new JesseltonWilliams(0.0F, 0.0F)}));
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo("Jesselton", NewMonsterMulti));
        addMonster("Disaster of Machine", names[11], () -> new MonsterGroup(new AbstractMonster[] {
                new Perpetrator(-440.0F, 0.0F) {
                    {setHp(maxHealth / 2);}
                },
                new Exploder(-320.0F, 280.0F),
                new Repulsor(-140.0F, 300.0F),
                new Sentry(0.0F, 0.0F) {
                    {setHp(maxHealth - 10);}
                    @Override
                    protected void getMove(int num) {
                        if (this.lastMove((byte)3)) {
                            this.setMove((byte)4, Intent.ATTACK, this.damage.get(0).base);
                        } else {
                            this.setMove((byte)3, Intent.DEBUFF);
                        }
                    }
                },
                new BronzeOrb(200.0F, 270.0F, 0) {
                    {setHp(maxHealth - 10);}
                },
                new Spiker(240.0F, 0.0F),
                new Crossroads(-220.0F, -50.0F)
        }));
        /* 联机时不能削弱，不然会NPE
        addMonster("Disaster of Machine", names[11], () -> new MonsterGroup(new AbstractMonster[] {
                new Perpetrator(-440.0F, 0.0F),
                new Exploder(-300.0F, 400.0F),
                new Repulsor(-120.0F, 360.0F),
                new Sentry(0.0F, 0.0F),
                new BronzeOrb(200.0F, 270.0F, 0),
                new Spiker(240.0F, 0.0F),
                new Crossroads(-220.0F, -50.0F)
        }));
        */
        BaseMod.addEliteEncounter(TheBeyond.ID, new MonsterInfo("Disaster of Machine", 2.0F * NewMonsterMulti));

        // Add a name.
        addMonster("Awaken", names[12], () -> new MonsterGroup(new Awaken_Monster(180.0F, 0.0F)));
        addMonster("Turnpike", names[13], () -> new MonsterGroup(new Turnpike(0.0F, 0.0F)));
        addMonster("Traffic Police", names[14], () -> new MonsterGroup(new TrafficPolice(0.0F, 0.0F)));
        addMonster("The Sky", names[15], () -> new MonsterGroup(new AbstractMonster[] {new StarPod(0.0F, 0.0F)}));
        // Just Testing
        BaseMod.addEliteEncounter(TheEnding.ID, new MonsterInfo("Awaken", 3.0F * NewMonsterMulti));
        BaseMod.addEliteEncounter(TheSky.ID, new MonsterInfo("Turnpike", 3.0F * NewMonsterMulti));
        BaseMod.addStrongMonsterEncounter(TheSky.ID, new MonsterInfo("Traffic Police", 3.0F * NewMonsterMulti));
        // BaseMod.addBoss(TheSky.ID, "The Sky", "icon", "icon");

    }

    private void initializeEvents() {
        BaseMod.addEvent(new AddEventParams.Builder(ConvergenceOfPastAndPresent.ID, ConvergenceOfPastAndPresent.class).
                eventType(EventUtils.EventType.NORMAL).
                dungeonID("TheCity").
                dungeonID("TheBeyond").
                endsWithRewardsUI(false).
                spawnCondition(() -> ((AbstractDungeon.player instanceof RhineLab))).
                create());
        BaseMod.addEvent(new AddEventParams.Builder(MysteriousInvent.ID, MysteriousInvent.class).
                eventType(EventUtils.EventType.NORMAL).
                dungeonID("TheCity").
                dungeonID("TheBeyond").
                endsWithRewardsUI(false).
                spawnCondition(() -> (!(AbstractDungeon.player instanceof RhineLab))).
                create());
        BaseMod.addEvent(new AddEventParams.Builder(UnsecuredCorridors.ID, UnsecuredCorridors.class).
                eventType(EventUtils.EventType.NORMAL).
                dungeonIDs("TheCity", "TheBeyond").
                endsWithRewardsUI(true).
                create());
        if (isDemo) {
            BaseMod.addEvent(new AddEventParams.Builder(HeartEvent.ID, HeartEvent.class).
                    eventType(EventUtils.EventType.NORMAL).
                    dungeonID("Special").
                    endsWithRewardsUI(false).
                    create());
            BaseMod.addEvent(new AddEventParams.Builder(SkyEvent.ID, SkyEvent.class).
                    eventType(EventUtils.EventType.NORMAL).
                    dungeonID("Special").
                    endsWithRewardsUI(false).
                    create());
            BaseMod.addEvent(new AddEventParams.Builder(TheCure.ID, TheCure.class).
                    eventType(EventUtils.EventType.NORMAL).
                    dungeonIDs("Exordium", "TheCity", "TheBeyond").
                    endsWithRewardsUI(false).
                    create());
            BaseMod.addEvent(new AddEventParams.Builder(StarlitNight.ID, StarlitNight.class).
                    eventType(EventUtils.EventType.NORMAL).
                    dungeonIDs("Exordium", "TheCity", "TheBeyond").
                    endsWithRewardsUI(false).
                    create());
        }
    }

    private void initializePotions() {
        BaseMod.addPotion(BlackBeanTea.class, Color.GRAY, Color.DARK_GRAY, null, BlackBeanTea.ID, RhineEnum.RHINE_CLASS);
    }

    private void initializeSpecialImg() {
        specialImg.add(new TextureAtlas.AtlasRegion(new Texture("resources/rhinemod/images/512/card_saria.png"), 0, 0, 512, 512));
        specialImg.add(new TextureAtlas.AtlasRegion(new Texture("resources/rhinemod/images/512/card_kristen.png"), 0, 0, 512, 512));
        specialImg.add(new TextureAtlas.AtlasRegion(new Texture("resources/rhinemod/images/512/card_muelsyse.png"), 0, 0, 512, 512));
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
        
        ArrayList<AbstractCard> cards = new ArrayList<>();

        // Basic.
        cards.add(new RhineStrike()); // 打击
        cards.add(new RhineDefend()); // 防御
        cards.add(new Destiny()); // 命运
        cards.add(new DefenseSection()); // 防卫科
        cards.add(new ComponentsControlSection()); // 总辖构件科
        cards.add(new EcologicalSection()); // 生态科

        // Common.
        cards.add(new DangerousEntityRemoval()); // 危险目标清除
        cards.add(new ProgressiveMoisturization()); // 渐进性润化
        cards.add(new TransmitterResearch()); // 递质研究
        cards.add(new IcefieldsAdventure()); // 冰原探险
        cards.add(new BookOfFairyTales()); // 童话书
        cards.add(new StarlightIntersection()); // 星束交汇
        cards.add(new StarSculpt()); // 塑星
        cards.add(new ShatteredVision()); // 破碎愿景
        cards.add(new HighEfficiencyFreezingModule()); // 高效制冷模块
        cards.add(new DependentVariable()); // 因变量
        cards.add(new FirstAid()); // 急救
        cards.add(new GalaxyOrbit()); // 星系轨道
        cards.add(new CrackedSkill()); // 一起研究的公式/被破解的技术
        cards.add(new ReflectionInWater()); // 水中倒影
        cards.add(new CriticalPoint()); // 临界点
        cards.add(new DressingFlowingShape()); // 梳妆流形
        cards.add(new LokenWatertank()); // 洛肯水箱

        // Uncommon.
        cards.add(new PlanetaryDebris()); // 行星碎屑
        cards.add(new HighSpeedRT()); // 高速共振排障
        cards.add(new Starfall()); // 星辰坠落
        cards.add(new PureWaterIsLife()); // 净水即生命
        cards.add(new MedicineDispensing()); // 药物配置
        cards.add(new Enkephalin()); // 脑啡肽
        cards.add(new SHAFT()); // 能量井
        cards.add(new EmergencyDefenseProcedures()); // 紧急防卫程序
        cards.add(new TechnologyRisingStar()); // 科技新星
        cards.add(new UnusedBoxingGloves()); // 闲置拳击手套
        cards.add(new BionicDevice()); // 迷惑装置
        cards.add(new NovaEruption()); // 新星爆发
        cards.add(new ApplyFullForce()); // 放开手脚
        cards.add(new TwoToOne()); // 二比一
        cards.add(new EcologicalInteraction()); // 生态耦合
        cards.add(new QuicksandGeneration()); // 流沙区域生成
        cards.add(new Memory()); // 回忆
        cards.add(new ResourceEconomization()); // 开源节流
        cards.add(new FullBlow()); // 全力一击
        cards.add(new HeadquarterBuilding()); // 总部大楼
        cards.add(new LightPenetratingClouds()); // 穿云之光
        cards.add(new LoneLight()); // 寂寥的光
        cards.add(new FlowCombo()); // 流形连击
        cards.add(new AcademicResearch()); // 学术研究
        cards.add(new EliminateThreat()); // 解除威胁
        cards.add(new RedShiftExperiment()); // 红移实验
        cards.add(new GalleriaStellaria()); // 万星园
        cards.add(new Truth()); // 真理
        cards.add(new FreeFromDream()); // 挣脱美梦
        cards.add(new Dreamer()); // 梦想家
        cards.add(new AprilShowers()); // 无根之雨
        cards.add(new ProjectHorizonArc()); // 地平弧光计划
        cards.add(new EmergencyEscapeDoor()); // 紧急逃生门
        cards.add(new IntricateReconstruction()); // 精密重构

        // Rare.
        cards.add(new Calcification()); // 钙质化
        cards.add(new Solidify()); // 凝固
        cards.add(new HallOfStasis()); // 静滞所
        cards.add(new HeatDeath()); // 热寂
        cards.add(new DreadnoughtProtocol()); // 无畏者协议
        cards.add(new SuperficialRegulation()); // 浅层非熵适应
        cards.add(new DancingInThrees()); // 三个人的舞
        cards.add(new HAMHRR()); // 聚焦发生器
        cards.add(new StartUpCapital()); // 启动资金
        cards.add(new Galaxy()); // 银河
        cards.add(new WaveBarrier()); // 水波壁障
        cards.add(new PureDewOfFreshBlossoms()); // 纯净鲜花露
        cards.add(new LikeMind()); // 意气相投
        cards.add(new Refreshment()); // 精神回复
        cards.add(new IdealistForm()); // 理想者形态
        cards.add(new MultiEcho()); // 多重分身
        cards.add(new Disorder()); // “失序”
        cards.add(new Omen()); // 山雨欲来

        // Special.
        cards.add(new Unscrupulous()); // 出格
        cards.add(new Egotist()); // 自私者
        cards.add(new Traitor()); // 背叛者
        cards.add(new Seeker()); // 求道者
        cards.add(new Loner()); // 独行者
        cards.add(new Pioneer()); // 先驱者
        cards.add(new GiantRing()); // 巨大环
        cards.add(new BipolarNebula()); // 双极星云
        cards.add(new StellarRing()); // 恒星环
        cards.add(new SquareSunflower()); // 方形葵
        cards.add(new IcefieldsCottongrass()); // 冰原棉草
        cards.add(new Sarracenia()); // 雪瓶子草
        cards.add(new SheathedBeech()); // 鞘叶榉
        cards.add(new PaleFir()); // 淡杉
        cards.add(new TracingOrigins()); // 追溯本源

        cards.add(new GravityDown()); // 超重
        cards.add(new GravityNone()); // 重置
        cards.add(new GravityUp()); // 失重

        // Colourless Uncommon
        cards.add(new AuxiliaryDrone()); // 夜灯

        // Colourless Rare
        cards.add(new AttackInsteadOfDefend()); // 以攻代守

        for (AbstractCard c:cards) {
            BaseMod.addCard(c);
            UnlockTracker.seenPref.putInteger(c.cardID, 1); // 开局直接解锁全部卡牌，不用一个个unlock了
        }
        UnlockTracker.seenPref.flush();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new RhineLab(CardCrawlGame.playerName), charButton, charPortrait, RhineEnum.RHINE_CLASS);
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String keywordStrings = Gdx.files.internal("resources/rhinemod/strings/" + getLang() + "/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
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
        BaseMod.addRelicToCustomPool(new TITStudentIdCard(), RHINE_MATTE);

        // common.
        BaseMod.addRelicToCustomPool(new OrangeStorm(), RHINE_MATTE);

        // uncommon.
        BaseMod.addRelic(new PittsAssortedFruits(), RelicType.SHARED);
        BaseMod.addRelicToCustomPool(new RhineChargeSuit(), RHINE_MATTE);

        // rare.
        BaseMod.addRelic(new AwakenModel(), RelicType.SHARED);
        BaseMod.addRelic(new Stargate(), RelicType.SHARED);
        BaseMod.addRelic(new FlameEmitter(), RelicType.SHARED);
        BaseMod.addRelicToCustomPool(new PeppermintChapstick(), RHINE_MATTE);

        // boss.
        BaseMod.addRelicToCustomPool(new Future(), RHINE_MATTE);
        BaseMod.addRelicToCustomPool(new RhineLabEmployeeCard(), RHINE_MATTE);

        // event.
        BaseMod.addRelic(new LoneTrail(), RelicType.SHARED);
        BaseMod.addRelicToCustomPool(new Melt(), RHINE_MATTE);
        BaseMod.addRelic(new Deal(), RelicType.SHARED);
        BaseMod.addRelic(new Dor3Bionic(), RelicType.SHARED);
        BaseMod.addRelic(new Awaken(), RelicType.SHARED);
        BaseMod.addRelic(new R31HeavyPowerArmorRelic(), RelicType.SHARED);
        BaseMod.addRelic(new ExperimentalPowerArmorRelic(), RelicType.SHARED);
        if (isDemo) {
            BaseMod.addRelicToCustomPool(new WaterdropManifestation(), RHINE_MATTE);
            BaseMod.addRelicToCustomPool(new Imperishable(), RHINE_MATTE);
            BaseMod.addRelicToCustomPool(new CalcareousStamp(), RHINE_MATTE);
        }

        // shop.
        BaseMod.addRelic(new ThreeDimensionArtDisplay(), RelicType.SHARED);
    }

    @Override
    public void receiveEditStrings() {
        String lang = getLang();
        String cardStrings = Gdx.files.internal("resources/rhinemod/strings/" + lang + "/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String characterStrings = Gdx.files.internal("resources/rhinemod/strings/" + lang + "/character.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
        String powerStrings = Gdx.files.internal("resources/rhinemod/strings/" + lang + "/powers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String relicStrings = Gdx.files.internal("resources/rhinemod/strings/" + lang + "/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String eventStrings = Gdx.files.internal("resources/rhinemod/strings/" + lang + "/events.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
        String monsterStrings = Gdx.files.internal("resources/rhinemod/strings/" + lang + "/monsters.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        String uiStrings = Gdx.files.internal("resources/rhinemod/strings/" + lang + "/ui.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
        String potionStrings = Gdx.files.internal("resources/rhinemod/strings/" + lang + "/potions.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
        String scoreBonusesStrings = Gdx.files.internal("resources/rhinemod/strings/" + lang + "/score_bonuses.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(ScoreBonusStrings.class, scoreBonusesStrings);
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

        for (AbstractPower p : target.powers) {
            tmp = p.atDamageReceive(tmp, info.type);
        }

        for (AbstractPower p : target.powers) {
            if (info.owner.equals(target) && p instanceof InvisibleGlobalAttributes) continue;
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

    public static int getBranch(AbstractCard c) {
        if (c instanceof AbstractRhineCard) return ((AbstractRhineCard) c).realBranch;
        else return 0;
    }
}