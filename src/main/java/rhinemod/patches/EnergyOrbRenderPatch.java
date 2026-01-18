package rhinemod.patches;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rhinemod.RhineMod;
import rhinemod.characters.RhineLab;

public class EnergyOrbRenderPatch {
    public static float ORB_IMG_SCALE = 1.15F * Settings.scale;
    // ===== 小摆动：anchor + AMP * sin(phase) =====
    static final float AMP = 3.0f;       // ±3
    static final float PERIOD = 1.2f;    // 1.2s
    static float anchor = 0f;
    static float phase = MathUtils.PI / 2f; // 初始在最右点

    // ===== 大摆动：插值“实际角度”（会走到 anchor±AMP）=====
    static boolean moving = false;
    static float moveTime = 0f;
    static final float MOVE_DUR = 1.0f;
    static float moveStart = 0f;
    static float moveEnd = 0f;

    // moving 结束后再应用的 idle 状态（避免启动当帧闪）
    static float nextAnchor = 0f;
    static float nextPhase = 0f;
    static int nextPos = 1;

    // ===== 触发逻辑 =====
    static float idleTime = 0f;
    static final float IDLE_WAIT = 3.0f;

    static boolean pendingMove = false;
    static float pendingAnchor = 0f;
    static int pendingPos = 1;

    static int cur_pos = 1;
    static final float PHASE_EPS = 0.10f; // 启动相位容忍（0.06~0.14 可调）

    @SpirePatch(clz = CustomEnergyOrb.class, method = "renderOrb")
    public static class RenderOrbPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(CustomEnergyOrb _inst, SpriteBatch sb, boolean enabled, float current_x, float current_y, Texture[] ___energyLayers, Texture[] ___noEnergyLayers, float[] ___angles, Texture ___baseLayer) {
            if (AbstractDungeon.player instanceof RhineLab && RhineMod.useLoneTrail) {
                sb.setColor(Color.WHITE);
                if (enabled) {
                    for (int i = 0; i < ___energyLayers.length - 1; ++i) {
                        sb.draw(___energyLayers[i], current_x - RhineLab.layerWidth[i] / 2, current_y - 64.0F, RhineLab.layerWidth[i] / 2, 64.0F, RhineLab.layerWidth[i], 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, ___angles[i], 0, 0, (int)RhineLab.layerWidth[i], (int)RhineLab.layerWidth[i], false, false);
                    }
                } else {
                    for (int i = 0; i < ___noEnergyLayers.length - 1; ++i) {
                        sb.draw(___noEnergyLayers[i], current_x - RhineLab.layerWidth[i] / 2, current_y - 64.0F, RhineLab.layerWidth[i] / 2, 64.0F, RhineLab.layerWidth[i], 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, ___angles[i], 0, 0, (int)RhineLab.layerWidth[i], (int)RhineLab.layerWidth[i], false, false);
                    }
                }

                sb.draw(___baseLayer, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);

                int i = ___energyLayers.length - 1;
                if (enabled) {
                    sb.draw(___energyLayers[i], current_x - RhineLab.layerWidth[i] / 2, current_y - 64.0F, RhineLab.layerWidth[i] / 2, 64.0F, RhineLab.layerWidth[i], 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, ___angles[i], 0, 0, (int)RhineLab.layerWidth[i], (int)RhineLab.layerWidth[i], false, false);
                } else {
                    sb.draw(___noEnergyLayers[i], current_x - RhineLab.layerWidth[i] / 2, current_y - 64.0F, RhineLab.layerWidth[i] / 2, 64.0F, RhineLab.layerWidth[i], 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, ___angles[i], 0, 0, (int)RhineLab.layerWidth[i], (int)RhineLab.layerWidth[i], false, false);
                }

                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = CustomEnergyOrb.class, method = "updateOrb")
    public static class UpdateOrbPatch {
        @SpirePostfixPatch
        public static void Postfix(CustomEnergyOrb _inst, int energyCount, float[] ___angles) {
            if (!(AbstractDungeon.player instanceof RhineLab) || !RhineMod.useLoneTrail) return;
            if (___angles == null || ___angles.length == 0) return;

            int last = ___angles.length - 1;
            float dt = Gdx.graphics.getDeltaTime();

            if (!moving) {
                // 小摆动相位持续推进
                phase += dt * MathUtils.PI2 / PERIOD;
                idleTime += dt;

                // energyCount==0：不启动新的大摆动，但小摆动继续
                if (energyCount == 0) {
                    pendingMove = false;
                    ___angles[last] = idleAngle();
                    return;
                }

                // 到时间点：准备一次大摆动（先不启动）
                if (!pendingMove && idleTime >= IDLE_WAIT) {
                    int targetPos = pickNewPos(cur_pos);
                    pendingPos = targetPos;
                    pendingAnchor = (targetPos - 1) * 60f;
                    pendingMove = true;
                }

                // pending：等到指定极值点再启动（你要的“走到 anchor±AMP 再接小摆动”）
                if (pendingMove) {
                    int dir = sign(pendingAnchor - anchor); // +1 向右（0->60），-1 向左
                    if (dir != 0) {
                        // 向右：从最左点起步（sin=-1 => phase=3π/2）
                        // 向左：从最右点起步（sin=+1 => phase=π/2）
                        float startPhase = (dir > 0) ? (3f * MathUtils.PI / 2f) : (MathUtils.PI / 2f);

                        float d = wrapToPi(phase - startPhase);
                        if (Math.abs(d) < PHASE_EPS) {
                            // 钉到精确相位：保证起步速度=0
                            phase = startPhase;

                            // 大摆动起点/终点：终点落在新 anchor 的同向极值
                            moveStart = idleAngle();              // anchor + dir*(-AMP)
                            moveEnd   = pendingAnchor + dir * AMP;

                            // 结束后再切换 idle 的中心/相位（别在这一帧改！）
                            nextAnchor = pendingAnchor;
                            nextPhase  = (dir > 0) ? (MathUtils.PI / 2f) : (3f * MathUtils.PI / 2f); // 向右落最右；向左落最左
                            nextPos    = pendingPos;

                            moving = true;
                            moveTime = 0f;
                            pendingMove = false;
                            idleTime = 0f;

                            // ✅ 关键：启动当帧直接输出 moveStart，避免“闪到不相干角度”
                            ___angles[last] = moveStart;
                            return;
                        }
                    }
                }

                ___angles[last] = idleAngle();
                return;
            }

            // moving：走完这一次（不管 energyCount）
            moveTime += dt;
            float u = Math.min(moveTime / MOVE_DUR, 1f);
            float t = Interpolation.sine.apply(u);
            ___angles[last] = moveStart + (moveEnd - moveStart) * t;

            if (moveTime >= MOVE_DUR) {
                moving = false;
                moveTime = 0f;

                // 现在再切换到新的 idle 中心/相位/pos
                anchor = nextAnchor;
                phase  = nextPhase;
                cur_pos = nextPos;

                // 这一帧保证落在极值点（=moveEnd）
                ___angles[last] = idleAngle();
            }
        }

        private static int sign(float x) {
            return Float.compare(x, 0f);
        }

        private static int pickNewPos(int cur) {
            int p = MathUtils.random(0, 1);
            if (p >= cur) p++;
            return p;
        }

        private static float idleAngle() {
            return anchor + AMP * MathUtils.sin(phase);
        }

        private static float wrapToPi(float x) {
            // wrap 到 [-pi, pi]
            x = (x + MathUtils.PI) % MathUtils.PI2;
            if (x < 0) x += MathUtils.PI2;
            return x - MathUtils.PI;
        }
    }
}
