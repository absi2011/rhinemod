package rhinemod.vfx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TargetingUiEffect {
    public static Texture WHITE_PIXEL;
    public static float THICKNESS = 6f;
    public static float HALF_SQUARE = 12f;
    public static void init() {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, 1f);
        pm.fill();
        WHITE_PIXEL = new Texture(pm);
        WHITE_PIXEL.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        pm.dispose();
    }

    // 画一条“从起点到终点中心”的折线：先45°斜，再水平/竖直
    // 并在终点画一个10x10空心方框（线宽同thickness），且连线停在方框外边缘。
    public static void drawLinkWithEndSquare(SpriteBatch sb, float x0, float y0, float x1, float y1) {
        if (WHITE_PIXEL == null) init();

        // 1) 决定走“斜+水平”还是“斜+竖直”
        // 斜线方向取向终点（dx,dy 的符号），斜线每走1，x和y同幅变化（45°）。
        float dx = x1 - x0;
        float dy = y1 - y0;
        float sx = Math.signum(dx == 0 ? 1f : dx);
        float sy = Math.signum(dy == 0 ? 1f : dy);

        // 两个候选拐点：
        // A：先斜到 y=y1，再水平到 x=x1
        // B：先斜到 x=x1，再竖直到 y=y1
        Vector2 cornerA = new Vector2(
                x0 + sx * Math.abs(y1 - y0),
                y1
        );
        Vector2 cornerB = new Vector2(
                x1,
                y0 + sy * Math.abs(x1 - x0)
        );

        // 如果拐点落进终点方框内部，会导致第二段从方框内开始画，不好看；优先避开
        boolean aInside = isInsideSquare(cornerA.x, cornerA.y, x1, y1);
        boolean bInside = isInsideSquare(cornerB.x, cornerB.y, x1, y1);

        Vector2 corner;
        boolean secondIsHorizontal; // 第二段是否水平（否则竖直）
        if (!aInside && (bInside || totalLen(x0, y0, cornerA, x1, y1) <= totalLen(x0, y0, cornerB, x1, y1))) {
            corner = cornerA;
            secondIsHorizontal = true;
        } else {
            corner = cornerB;
            secondIsHorizontal = false;
        }

        // 2) 计算第二段“到方框边缘”的停止点（延长线指向终点中心）
        Vector2 stop = new Vector2();
        if (secondIsHorizontal) {
            float dir = Math.signum(x1 - corner.x);
            if (dir == 0) dir = 1f;
            stop.set(x1 - dir * HALF_SQUARE, y1);
        } else {
            float dir = Math.signum(y1 - corner.y);
            if (dir == 0) dir = 1f;
            stop.set(x1, y1 - dir * HALF_SQUARE);
        }

        // 3) 画两段线：起点->corner (45°斜线)，corner->stop（水平/竖直）
        drawLine(sb, x0, y0, corner.x, corner.y);
        drawLine(sb, corner.x, corner.y, stop.x, stop.y);
        drawJoin(sb, corner.x, corner.y);

        // 4) 画终点空心正方形（中心在 x1,y1，边长10）
        drawSquareOutline(sb, x1, y1);
    }

    private static float totalLen(float x0, float y0, Vector2 c, float x1, float y1) {
        return Vector2.dst(x0, y0, c.x, c.y) + Vector2.dst(c.x, c.y, x1, y1);
    }

    private static boolean isInsideSquare(float x, float y, float cx, float cy) {
        return Math.abs(x - cx) <= HALF_SQUARE && Math.abs(y - cy) <= HALF_SQUARE;
    }

    // 用 SpriteBatch 画“厚线段”：本质是画一条旋转后的细长矩形
    private static void drawLine(SpriteBatch sb, float x0, float y0, float x1, float y1) {
        float dx = x1 - x0;
        float dy = y1 - y0;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len < 0.0001f) return;

        float angleDeg = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;
        angleDeg = Math.round(angleDeg / 45f) * 45f;

        // 以 (x0,y0) 为起点，从左端开始画：THICKNESS=len，height=THICKNESS
        sb.draw(WHITE_PIXEL,
                x0, y0 - THICKNESS * 0.5f,          // x, y（让线段以 y0 为中线）
                0f, THICKNESS * 0.5f,               // originX, originY（绕左端旋转）
                len, THICKNESS,                     // THICKNESS, height（缩放到目标长度/厚度）
                1f, 1f,
                angleDeg,
                0, 0, 1, 1,
                false, false);
    }

    private static void drawJoin(SpriteBatch sb, float x, float y) {
        // 让方块中心在拐点
        float half = THICKNESS * 0.5f;
        sb.draw(WHITE_PIXEL, x - half, y - half, THICKNESS, THICKNESS);
    }

    // 画空心方框（中心(cx,cy)，边长size，线宽thickness）
    private static void drawSquareOutline(SpriteBatch sb, float cx, float cy) {
        float left = cx - HALF_SQUARE;
        float right = cx + HALF_SQUARE;
        float bottom = cy - HALF_SQUARE;
        float top = cy + HALF_SQUARE;

        // 上/下边
        drawLine(sb, left - THICKNESS * 0.5f, top, right + THICKNESS * 0.5f, top);
        drawLine(sb, left - THICKNESS * 0.5f, bottom, right + THICKNESS * 0.5f, bottom);
        // 左/右边
        drawLine(sb, left, bottom - THICKNESS * 0.5f, left, top + THICKNESS * 0.5f);
        drawLine(sb, right, bottom - THICKNESS * 0.5f, right, top + THICKNESS * 0.5f);
    }
}
