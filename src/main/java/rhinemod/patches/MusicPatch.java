package rhinemod.patches;

import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import rhinemod.monsters.Dorothy;
import rhinemod.monsters.StarPod;
import rhinemod.util.TheSky;

import java.util.HashMap;
import java.util.logging.Logger;

public class MusicPatch {
    public static final String STAR_POD_BGM_INTRO = "m_bat_cstlrs_intro.mp3";
    public static final String STAR_POD_BGM_LOOP = "m_bat_cstlrs_loop.mp3";
    public static final String DOROTHY_BGM_INTRO = "m_bat_peo_intro.mp3";
    public static final String DOROTHY_BGM_LOOP = "m_bat_peo_loop.mp3";
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

    @SpirePatch(clz = TempMusic.class, method = "<ctor>", paramtypez = {String.class, boolean.class, boolean.class})
    public static class UpdateTempBGMPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = "music")
        public static void Insert(TempMusic _inst, Music music) {
            if (_inst.key.equals(STAR_POD_BGM_INTRO)) {
                music.setOnCompletionListener(music1 -> {
                    Logger.getLogger(MusicPatch.class.getName()).info("StarPod BGM intro done!");
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                            if (m.id.equals(StarPod.ID)) {
                                CardCrawlGame.music.playTempBgmInstantly(STAR_POD_BGM_LOOP, true);
                                break;
                            }
                    }
                });
            } else if (_inst.key.equals(DOROTHY_BGM_INTRO)) {
                music.setOnCompletionListener(music1 -> {
                    Logger.getLogger(MusicPatch.class.getName()).info("Dorothy BGM intro done!");
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                            if (m.id.equals(Dorothy.ID)) {
                                CardCrawlGame.music.playTempBgmInstantly(DOROTHY_BGM_LOOP, true);
                                break;
                            }
                    }
                });
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(Music.class, "play");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }
}
