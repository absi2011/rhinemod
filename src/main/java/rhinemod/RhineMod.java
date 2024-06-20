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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rhinemod.cards.special.*;
import rhinemod.cards.*;
import rhinemod.characters.RhineLab;
import rhinemod.events.ConvergenceOfPastAndPresent;
import rhinemod.events.HeartEvent;
import rhinemod.events.MysteriousInvent;
import rhinemod.events.SkyEvent;
import rhinemod.monsters.*;
import rhinemod.patches.RhineEnum;
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
    private static final String energyOrb = "resources/rhinemod/images/512/card_nearl_orb.png";
    private static final String attackCardPortrait = "resources/rhinemod/images/1024/bg_attack_rhine.png";
    private static final String skillCardPortrait = "resources/rhinemod/images/1024/bg_skill_rhine.png";
    private static final String powerCardPortrait = "resources/rhinemod/images/1024/bg_power_rhine.png";
    private static final String energyOrbPortrait = "resources/rhinemod/images/1024/card_nearl_orb.png";
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
                new Exploder(-300.0F, 400.0F),
                new Repulsor(-120.0F, 360.0F),
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
        if (isDemo) return;
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

    }

    private void initializePotions() {
//        BaseMod.addPotion(BottledCommand.class, Color.SKY, null, null, BottledCommand.ID, nearlmod.patches.RhineEnum.NEARL_CLASS);
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
        BaseMod.addCard(new DependentVariable()); // 因变量
        BaseMod.addCard(new FirstAid()); // 急救
        BaseMod.addCard(new GalaxyOrbit()); // 星系轨道
        BaseMod.addCard(new CrackedSkill()); // 一起研究的公式/被破解的技术
        BaseMod.addCard(new ReflectionInWater()); // 水中倒影

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
        BaseMod.addCard(new ResourceEconomization()); // 开源节流
        BaseMod.addCard(new FullBlow()); // 全力一击
        BaseMod.addCard(new HeadquarterBuilding()); // 总部大楼
        BaseMod.addCard(new LightPenetratingClouds()); // 穿云之光
        BaseMod.addCard(new LoneLight()); // 寂寥的光
        BaseMod.addCard(new FlowCombo()); // 流形连击
        BaseMod.addCard(new AcademicResearch()); // 学术研究
        BaseMod.addCard(new EliminateThreat()); // 解除威胁
        BaseMod.addCard(new RedShiftExperience()); // 红移实验
        BaseMod.addCard(new GalleriaStellaria()); // 万星园
        BaseMod.addCard(new Truth()); // 真理
        BaseMod.addCard(new FreeFromDream()); // 挣脱美梦
        BaseMod.addCard(new Dreamer()); // 梦想家
        BaseMod.addCard(new CriticalPoint()); // 临界点

        // Rare.
        BaseMod.addCard(new Calcification()); // 钙质化
        BaseMod.addCard(new Solidify()); // 凝固
        BaseMod.addCard(new HallOfStasis()); // 静滞所
        BaseMod.addCard(new HeatDeath()); // 热寂
        BaseMod.addCard(new DreadnoughtProtocol()); // 无畏者协议
        BaseMod.addCard(new SuperficialRegulation()); // 浅层非熵适应
        BaseMod.addCard(new DancingInThrees()); // 三个人的舞
        BaseMod.addCard(new HAMHRR()); // 聚焦发生器
        BaseMod.addCard(new StartUpCapital()); // 启动资金
        BaseMod.addCard(new Galaxy()); // 银河
        BaseMod.addCard(new WaveBarrier()); // 水波壁障
        BaseMod.addCard(new PureDewOfFreshBlossoms()); // 纯净鲜花露
        BaseMod.addCard(new LikeMind()); // 意气相投
        BaseMod.addCard(new Refreshment()); // 精神回复

        // Special.
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

        BaseMod.addCard(new GravityDown()); // 超重
        BaseMod.addCard(new GravityNone()); // 重置
        BaseMod.addCard(new GravityUp()); // 失重

        // Colourless Rare.
        BaseMod.addCard(new AttackInsteadOfDefend()); // 以攻代守
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

        // uncommon.

        // rare.
        BaseMod.addRelic(new Awaken(), RelicType.SHARED);
        BaseMod.addRelic(new Stargate(), RelicType.SHARED);
        BaseMod.addRelic(new FlameEmitter(), RelicType.SHARED);

        // boss.

        // event.
        BaseMod.addRelic(new LoneTrail(), RelicType.SHARED);
        BaseMod.addRelicToCustomPool(new Melt(), RHINE_MATTE);

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