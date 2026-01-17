package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import rhinemod.util.RhineImageMaster;

public class PileFilesEffect extends AbstractGameEffect {
    private final float sx, sy;
    private final float ex, ey;

    // 文件数量与间距（沿路径方向的弧长间距）
    private final int fileCount = 20;
    private final float spacing = 100.0F;

    // 1秒内匀减速到0（1秒后停）
    private final float durationSec = 1.0f;

    // 初速度（像素/秒），1秒内走的总距离 = 0.5 * v0
    private final float v0 = 4000.0F;

    // 图标绘制尺寸
    private final float drawW = 500.0F * Settings.scale;

    // 圆弧“弧高”比例：弧高 = sagittaRatio * 弦长。越大越弯；可为负表示向另一侧弯
    // 建议 0.05 ~ 0.35
    private final float sagittaRatio;

    private float stateTime = 0f;

    // 圆参数
    private final boolean useArc;
    private final float cx, cy;     // 圆心
    private final float radius;     // 半径
    private final float ang0;       // 起点角
    private final float dir;        // 沿弧方向：+1 或 -1

    public PileFilesEffect(float startX, float startY, float endX, float endY, float sagittaRatio) {
        this.sx = startX;
        this.sy = startY;
        this.ex = endX;
        this.ey = endY;
        this.sagittaRatio = sagittaRatio;

        this.duration = durationSec;

        // ====== 计算圆弧几何（由起点、终点、弧高决定一段圆）======
        float dx = ex - sx;
        float dy = ey - sy;
        float chord = (float) Math.sqrt(dx * dx + dy * dy);

        if (chord < 1e-4f || Math.abs(sagittaRatio) < 1e-4f) {
            // 退化：弦太短 或 弧高≈0，就用直线（这里仍按圆参数填默认，不使用）
            useArc = false;
            cx = cy = radius = ang0 = dir = 0f;
            return;
        }

        // 弦中点
        float mx = (sx + ex) * 0.5f;
        float my = (sy + ey) * 0.5f;

        // 弦的单位法向（把 (dx,dy) 旋转 90°）
        float nx = -dy / chord;
        float ny =  dx / chord;

        // 弧高（带符号，决定向哪边弯）
        float hSigned = sagittaRatio * chord;
        float h = Math.abs(hSigned);
        if (h < 1e-4f) {
            useArc = false;
            cx = cy = radius = ang0 = dir = 0f;
            return;
        }

        // 半径：R = c^2/(8h) + h/2
        float R = (chord * chord) / (8f * h) + h * 0.5f;

        // 圆心到弦中点的距离：d = R - h
        float d = R - h;

        float sign = (hSigned >= 0f) ? 1f : -1f; // 正负决定圆心在法向哪一侧

        float cxx = mx + nx * d * sign;
        float cyy = my + ny * d * sign;

        float a0 = (float) Math.atan2(sy - cyy, sx - cxx);
        float a1 = (float) Math.atan2(ey - cyy, ex - cxx);

        // 选择“哪条弧”（短弧/长弧）以及方向，使其经过我们期望的弧中点（mx + n*hSigned）
        float targetMx = mx + nx * hSigned;
        float targetMy = my + ny * hSigned;

        float deltaShort = normalizeAngle(a1 - a0);              // (-pi, pi]
        float deltaLong  = deltaShort - (float)(Math.signum(deltaShort) * Math.PI * 2); // 另一条弧

        // 计算两条候选弧的“中点”位置，选更接近 targetMid 的那条
        float mid1ang = a0 + 0.5f * deltaShort;
        float mid2ang = a0 + 0.5f * deltaLong;

        float mid1x = cxx + R * (float)Math.cos(mid1ang);
        float mid1y = cyy + R * (float)Math.sin(mid1ang);
        float mid2x = cxx + R * (float)Math.cos(mid2ang);
        float mid2y = cyy + R * (float)Math.sin(mid2ang);

        float dist1 = (mid1x - targetMx)*(mid1x - targetMx) + (mid1y - targetMy)*(mid1y - targetMy);
        float dist2 = (mid2x - targetMx)*(mid2x - targetMx) + (mid2y - targetMy)*(mid2y - targetMy);

        float chosenDelta = (dist1 <= dist2) ? deltaShort : deltaLong;

        useArc = true;
        cx = cxx;
        cy = cyy;
        radius = R;
        ang0 = a0;
        dir = (chosenDelta >= 0f) ? 1f : -1f;
    }

    private static float normalizeAngle(float a) {
        float twoPi = (float)(Math.PI * 2.0);
        while (a <= -Math.PI) a += twoPi;
        while (a >  Math.PI) a -= twoPi;
        return a;
    }

    @Override
    public void render(SpriteBatch sb) {
        stateTime += Gdx.graphics.getDeltaTime();
        if (stateTime >= durationSec) stateTime = durationSec; // 1秒后停住（但不强制 clamp 到终点）

        sb.setColor(Color.WHITE);

        float t01 = stateTime / durationSec; // [0,1]
        float s = v0 * t01 - 0.5f * v0 * t01 * t01; // 匀减速位移（弧长距离），1秒后固定

        // 直线 fallback（弧高=0 或太短）
        if (!useArc) {
            float dx = ex - sx;
            float dy = ey - sy;
            float L = (float)Math.sqrt(dx * dx + dy * dy);
            if (L <= 1e-4f) return;
            float ux = dx / L, uy = dy / L;

            for (int i = fileCount - 1; i >= 0; i--) {
                float di = s - i * spacing;
                if (di < 0f) continue;

                float px = sx + ux * di;
                float py = sy + uy * di;

                sb.draw(RhineImageMaster.singleFile,
                        px - drawW * 0.5f, py - drawW * 0.5f,
                        px, py,
                        drawW, drawW,
                        Settings.scale, Settings.scale,
                        0.0F);
            }
            return;
        }

        // 圆弧：di 是沿弧的弧长距离，可以超过“终点弧长”，继续绕圆走（不会停在终点堆住）
        for (int i = fileCount - 1; i >= 0; i--) {
            float di = s - i * spacing;
            if (di < 0f) continue;

            float theta = (di / radius) * dir;  // 角度推进量（弧长/半径）
            float ang = ang0 + theta;

            float px = cx + radius * (float)Math.cos(ang);
            float py = cy + radius * (float)Math.sin(ang);

            sb.draw(RhineImageMaster.singleFile,
                    px - drawW * 0.5f, py - drawW * 0.5f,
                    px, py,
                    drawW, drawW,
                    Settings.scale, Settings.scale,
                    0.0F);
        }
    }

    @Override
    public void dispose() {}
}
