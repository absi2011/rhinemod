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
            map.put("AS_YOU_WISH", new Sfx("audio/sound/p_skill_asyouwish.mp3", false));
            map.put("FS_SET", new Sfx("audio/sound/p_skill_wtrlimeset.mp3", false));
            map.put("ATTACK_WATER", new Sfx("audio/sound/p_imp_wtrlime_remote.mp3", false));
            map.put("SUBMERSION_TRIG", new Sfx("audio/sound/p_atk_wtrlimepull.mp3", false));
            map.put("STAR_RING_EXPLODE", new Sfx("audio/sound/e_imp_planetrockexplode.mp3", false));
            map.put("CALCIFICATION", new Sfx("audio/sound/p_field_calcinosis.mp3", false));
            map.put("SUMMON_STAR_RING", new Sfx("audio/sound/e_atk_planetrockres.mp3", false));
            map.put("SHATTERED_VISION", new Sfx("audio/sound/e_atk_earthimpact.mp3", false));
            map.put("JESSELTON_S2_SKILL", new Sfx("audio/sound/e_imp_magicsword_n.mp3", false));
            map.put("TURNPIKE_BOOM", new Sfx("audio/sound/e_imp_carcrush_1.mp3", false));
            map.put("SKY_INTRO", new Sfx("audio/sound/lonetrail_intro_01.wav", false));
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
