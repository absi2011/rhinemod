package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import rhinemod.RhineMod;

public class EndingElitePatch {
    @SpirePatch(clz = TheEnding.class, method = "generateMonsters")
    public static class GenerateMonstersPatch {
        @SpirePostfixPatch
        public static void Postfix(TheEnding _inst) {
            if (AbstractDungeon.mapRng.random(0, 1 + RhineMod.newMonsterMulti) > 1) {
                TheEnding.monsterList.clear();
                TheEnding.eliteMonsterList.clear();
                TheEnding.monsterList.add("Awaken");
                TheEnding.monsterList.add("Awaken");
                TheEnding.monsterList.add("Awaken");
                TheEnding.eliteMonsterList.add("Awaken");
                TheEnding.eliteMonsterList.add("Awaken");
                TheEnding.eliteMonsterList.add("Awaken");
            }
        }
    }
}
