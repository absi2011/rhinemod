package rhinemod.patches;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import rhinemod.vfx.RhineStarEffect;

import java.util.ArrayList;

@SpirePatch(clz = VictoryScreen.class, method = "updateVfx")
public class VictoryScreenVfxPatch {
    @SpirePostfixPatch
    public static void Postfix(VictoryScreen _inst, @ByRef float[] ___effectTimer, ArrayList<AbstractGameEffect> ___effect) {
        if (AbstractDungeon.player.chosenClass == RhineEnum.RHINE_CLASS) {
            ___effectTimer[0] -= Gdx.graphics.getDeltaTime();
            if (___effectTimer[0] < 0.0F) {
                if (___effect.size() < 100) {
                    ___effect.add(new RhineStarEffect(true));
                    ___effect.add(new RhineStarEffect(true));
                    ___effect.add(new RhineStarEffect(true));
                    ___effect.add(new RhineStarEffect(true));
                }
                ___effectTimer[0] = 0.1F;
            }
        }
    }
}
