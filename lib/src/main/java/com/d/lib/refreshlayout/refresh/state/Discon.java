package com.d.lib.refreshlayout.refresh.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.d.lib.refreshlayout.utils.Util;

/**
 * Discon
 * Created by D on 2018/3/30.
 */
public class Discon extends State {
    private Paint paint;
    private Paint paintCircle;
    private Rect rect;
    private RectF rectF;
    private float strokeWidth;

    public Discon(View view) {
        super(view);
        init(context);
    }

    private void init(Context context) {
        strokeWidth = Util.dip2px(context, 2.5f);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colorWhite);
        paint.setStrokeCap(Paint.Cap.ROUND);

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(colorError);

        rect = new Rect();
        rectF = new RectF();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float h = height * 0.35f;
        float w = h;
        float startX = (width - w) / 2;
        float startY = (height - h) / 2;

        canvas.drawCircle(width / 2f, height / 2f, h / 2f, paintCircle);

        canvas.translate(width / 2, height / 2);
        canvas.rotate(45);

        float factor = 0.51f;
        rect.set((int) (-w / 2f * factor), (int) (-strokeWidth / 2f), (int) (w / 2f * factor), (int) (strokeWidth / 2f));
        rectF.set(rect);
        canvas.drawRect(rectF, paint);

        canvas.rotate(90);
        canvas.drawRect(rectF, paint);
        canvas.rotate(-135);
        canvas.translate(-width / 2, -height / 2);
    }
}
