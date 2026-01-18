package rhinemod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SelectBannerEffect extends AbstractGameEffect {
    private final Texture pixel;
    private float stateTime = 0f;

    // 1) 动得太快：把速度降下来（你也可以再调小，比如 45~80）
    public float speedPxPerSec = 60f;

    // 2) 线距太窄：把 spacing 调大（比如 16~28）
    public float spacing = 20f;

    public float minWidth = 1f;
    public float maxWidth = 15f;

    // 颜色与透明度要求
    private static final Color BASE = new Color(0xC24830FF); // #C24830
    public float centerAlpha = 0.8f;  // 中心 alpha
    public float edgeAlpha = 0.10f;   // 边缘最低 alpha（可调）

    // 3) 变暗太快：不要全程幂函数，而是“靠近边缘一小段才开始掉”
    //    edgeStart=0.80 表示：从中心往外走到 80% 半宽之前几乎不变暗，最后 20% 才快速变暗
    public float edgeStart = 0.80f;   // [0,1)，越大=越靠边才变暗
    public float edgeGamma = 2.2f;    // 最后那段的陡峭程度（建议 1.6~3）

    // 可选：让“粗细”也更偏向中心（非线性）
    public float widthGamma = 0.5f;

    public float x;
    public float y;
    public final float w;
    public final float h;

    public SelectBannerEffect(float x, float y, float w, float h) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        pixel = new Texture(pm);
        pm.dispose();

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void render(SpriteBatch sb) {
        render(sb, 1.0F);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void render(SpriteBatch sb, float baseA) {
        stateTime += Gdx.graphics.getDeltaTime();

        // offset 在 [0, spacing) 循环，整体向左移动
        float offset = (stateTime * speedPxPerSec) % spacing;

        int count = (int) Math.ceil(w / spacing) + 3;
        float left = x, right = x + w;

        Color old = sb.getColor();

        for (int i = -1; i < count; i++) {
            float cx = left + i * spacing - offset;

            // 循环回卷
            while (cx < left) cx += w;
            while (cx >= right) cx -= w;

            // t: 0(边缘) -> 1(中心)
            float d = Math.min(cx - left, right - cx);   // [0, w/2]
            float t = (w <= 0f) ? 0f : (2f * d / w);     // [0,1]
            t = Math.max(0f, Math.min(1f, t));

            // 1) 宽度：越靠中心越粗
            float tw = (float) Math.pow(t, widthGamma);
            float lineW = minWidth + (maxWidth - minWidth) * tw;

            // 2) 透明度：让“大部分区域都接近 centerAlpha”，只有靠边一段才明显下降
            //    u: 0(中心) -> 1(边缘)
            float u = 1f - t;

            float a;
            if (u <= edgeStart) {
                // 在 edgeStart 之前几乎不变暗（保持中心亮度）
                a = centerAlpha;
            } else {
                // 只在最后 (1-edgeStart) 这段内从 centerAlpha 掉到 edgeAlpha，并可用 gamma 控制陡峭
                float v = (u - edgeStart) / (1f - edgeStart); // [0,1]
                v = Math.max(0f, Math.min(1f, v));
                float drop = (float) Math.pow(v, edgeGamma);  // 0->1
                a = centerAlpha + (edgeAlpha - centerAlpha) * drop;
            }

            sb.setColor(BASE.r, BASE.g, BASE.b, a * baseA);
            sb.draw(pixel, cx - lineW * 0.5f, y, lineW, h);
        }

        sb.setColor(old);
    }

    @Override
    public void dispose() {
        // 你如果只创建一次并复用，这里也可以不 dispose；
        // 但如果每次都 new 一个效果对象，建议在 appropriate 的时机释放。
        pixel.dispose();
    }
}
