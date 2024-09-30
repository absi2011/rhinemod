package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import rhinemod.util.TheSky;

import java.util.HashMap;

public class MusicPatch {
    @SpirePatch(clz = SoundMaster.class, method = "<ctor>")
    public static class SoundTrackPatch {
        @SpireInsertPatch(rloc = 3, localvars = {"map"})
        public static void Insert(SoundMaster __instance, HashMap<String, Sfx> map) {
//            map.put("AMBIANCE_SKY", new Sfx("audio/music/m_sys_act25side_combine.mp3", false));
        }
    }

    @SpirePatch(clz = MainMusic.class, method = "getSong")
    public static class MainMusicPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(MainMusic _inst, String key) {
            if (key.equals(TheSky.ID)) {
                return SpireReturn.Return(MainMusic.newMusic("audio/music/m_sys_act25side_combine.mp3"));
            }
            return SpireReturn.Continue();
        }
    }
}
