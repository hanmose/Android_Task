package kr.ac.kopo.todotimer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class MainActivity4 extends View {
    private Paint bgPaint, sectorPaint, tickPaint, textPaint, centerPaint;
    private RectF rectF;
    private float progress = 0.0f;

    public MainActivity4(Context context) { super(context); init(); }
    public MainActivity4(Context context, AttributeSet attrs) { super(context, attrs); init(); }

    private void init() {

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.parseColor("#2C2C2E"));


        sectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sectorPaint.setStyle(Paint.Style.FILL);


        tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tickPaint.setColor(Color.parseColor("#48484A"));
        tickPaint.setStrokeWidth(4f);


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#8E8E93"));
        textPaint.setTextSize(32f);
        textPaint.setTextAlign(Paint.Align.CENTER);


        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setColor(Color.WHITE);

        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY) * 0.75f;
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        canvas.drawCircle(centerX, centerY, radius, bgPaint);


        if (progress > 0) {
            sectorPaint.setShader(new LinearGradient(0, 0, getWidth(), getHeight(),
                    Color.parseColor("#FF453A"), Color.parseColor("#FF9F0A"), Shader.TileMode.CLAMP));
            canvas.drawArc(rectF, -90f, progress * 360f, true, sectorPaint);
        }


        for (int i = 0; i < 60; i += 5) {
            double angleRad = Math.toRadians(-90 + (i * 6));
            float x = (float) (centerX + radius * 1.22f * Math.cos(angleRad));
            float y = (float) (centerY + radius * 1.22f * Math.sin(angleRad)) + 12f;


            if (i % 15 == 0) {
                textPaint.setColor(Color.WHITE);
                textPaint.setFakeBoldText(true);
            } else {
                textPaint.setColor(Color.parseColor("#8E8E93"));
                textPaint.setFakeBoldText(false);
            }
            canvas.drawText(String.valueOf(i), x, y, textPaint);

            float sx = (float) (centerX + radius * 0.92f * Math.cos(angleRad));
            float sy = (float) (centerY + radius * 0.92f * Math.sin(angleRad));
            float ex = (float) (centerX + radius * Math.cos(angleRad));
            float ey = (float) (centerY + radius * Math.sin(angleRad));
            canvas.drawLine(sx, sy, ex, ey, tickPaint);
        }


        canvas.drawCircle(centerX, centerY, 8f, centerPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}