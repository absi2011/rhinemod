package rhinemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;

import java.util.HashMap;

@SpirePatch(clz = SoundMaster.class, method = "<ctor>")
public class SoundTrackPatch {
    @SpireInsertPatch(rloc = 3, localvars = {"map"})
    public static void Insert(SoundMaster __instance, HashMap<String, Sfx> map) {
        map.put("BLOOD_KNIGHT_REBORN_NXT", new Sfx("audio/sound/e_skill_bldkgtswordon.mp3", false));
    }
}
