package rhinemod.util;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class TheSky extends AbstractDungeon {
    private static final Logger logger = LogManager.getLogger(TheSky.class.getName());
    public static final String ID = "rhinemod:TheSky";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];
    public TheSky(AbstractPlayer p, ArrayList<String> theList) {
        super(NAME, ID, p, theList);
        if (scene != null) {
            scene.dispose();
        }
        scene = new TheSkyScene();
        fadeColor = Color.valueOf("140a1eff");
        sourceFadeColor = Color.valueOf("140a1eff");

        initializeLevelSpecificChances();
        mapRng = new Random(Settings.seed + (AbstractDungeon.actNum * 400L));
        generateSpecialMap();
        CardCrawlGame.music.changeBGM("TheEnding");
    }

    public TheSky(AbstractPlayer p, SaveFile saveFile) {
        super(NAME, p, saveFile);
        CardCrawlGame.dungeon = this;
        if (scene != null) {
            scene.dispose();
        }
        scene = new TheSkyScene();
        fadeColor = Color.valueOf("140a1eff");
        sourceFadeColor = Color.valueOf("140a1eff");

        initializeLevelSpecificChances();
        miscRng = new Random(Settings.seed + saveFile.floor_num);
        CardCrawlGame.music.changeBGM("TheEnding");
        mapRng = new Random(Settings.seed + (saveFile.act_num * 400L));
        generateSpecialMap();
        firstRoomChosen = true;
        populatePathTaken(saveFile);
    }

    private void generateSpecialMap() {
        long startTime = System.currentTimeMillis();
        map = new ArrayList<>();
        ArrayList<MapRoomNode> col = new ArrayList<>();
        ArrayList<MapRoomNode> row;
        MapRoomNode shopNode = new MapRoomNode(3, 0);
        shopNode.room = new ShopRoom();
        MapRoomNode enemyNode = new MapRoomNode(3, 1);
        enemyNode.room = new MonsterRoomElite();
        MapRoomNode eventNode = new MapRoomNode(3, 2);
        eventNode.room = new EventRoom();
        MapRoomNode bossNode = new MapRoomNode(3, 3);
        bossNode.room = new MonsterRoomBoss();
        MapRoomNode victoryNode = new MapRoomNode(3, 4);
        victoryNode.room = new TrueVictoryRoom();
        connectNode(shopNode, enemyNode);
        connectNode(enemyNode, eventNode);
        connectNode(eventNode, bossNode);
        col.add(shopNode);
        col.add(enemyNode);
        col.add(eventNode);
        col.add(bossNode);
        col.add(victoryNode);
        for (int i = 0; i < 5; i++) {
            row = new ArrayList<>();
            for (int j = 0; j < 3; j++) row.add(new MapRoomNode(j, i));
            row.add(col.get(i));
            for (int j = 4; j < 7; j++) row.add(new MapRoomNode(j, i));
            map.add(row);
        }

        logger.info("Generated the following dungeon map:");
        logger.info(MapGenerator.toString(map, true));
        logger.info("Game Seed: " + Settings.seed);
        logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
        firstRoomChosen = false;
        fadeIn();
    }

    private void connectNode(MapRoomNode src, MapRoomNode dst) {
        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
    }

    @Override
    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.25F;
        restRoomChance = 0.0F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.25F;
        eliteRoomChance = 0.25F;

        smallChestChance = 0;
        mediumChestChance = 100;
        largeChestChance = 0;

        commonRelicChance = 0;
        uncommonRelicChance = 100;
        rareRelicChance = 0;

        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.25F;
        } else {
            cardUpgradedChance = 0.5F;
        }
    }

    @Override
    protected ArrayList<String> generateExclusions() {
        return null;
    }

    @Override
    protected void generateMonsters() {
        monsterList = new ArrayList<>();
        monsterList.add("Turnpike");
        monsterList.add("Turnpike");
        monsterList.add("Turnpike");

        eliteMonsterList = new ArrayList<>();
        eliteMonsterList.add("Turnpike");
        eliteMonsterList.add("Turnpike");
        eliteMonsterList.add("Turnpike");
    }

    @Override
    protected void generateWeakEnemies(int i) {}

    @Override
    protected void generateStrongEnemies(int i) {}

    @Override
    protected void generateElites(int i) {}

    @Override
    protected void initializeBoss() {
        bossList.add("The Sky");
    }

    @Override
    protected void initializeEventList() {
        shrineList.clear();
        eventList.clear();
        specialOneTimeEventList.clear();
        eventList.add("rhinemod:HeartEvent");
    }

    @Override
    protected void initializeEventImg() {}

    @Override
    protected void initializeShrineList() {}


}
