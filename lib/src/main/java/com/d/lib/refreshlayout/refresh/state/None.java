package com.d.lib.refreshlayout.refresh.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.d.lib.refreshlayout.utils.Util;

/**
 * Loading
 * Created by D on 2018/3/30.
 */
public class None extends State {
    private Paint paint;
    private Paint paintCircle;

    private Rect rect;
    private RectF rectF;
    private int space;

    public None(View view) {
        super(view);
        init(context);
    }

    private void init(Context context) {
        space = Util.dip2px(context, 2.5f);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colorWhite);
        paint.setStrokeWidth(Util.dip2px(context, 2));
        paint.setStyle(Paint.Style.STROKE);

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(color);

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

        rect.set((int) startX + space, (int) startY + space, (int) (startX + w) - space, (int) (startY + h) - space);
        rectF.set(rect);
        canvas.drawArc(rectF, 225, 90, false, paint);
    }
}
