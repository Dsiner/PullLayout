package com.d.lib.pulllayout.edge.ripple.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.d.lib.pulllayout.util.Utils;

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
        init(mContext);
    }

    private void init(Context context) {
        strokeWidth = Utils.dp2px(context, 2.5f);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mColorWhite);
        paint.setStrokeCap(Paint.Cap.ROUND);

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(mColorError);

        rect = new Rect();
        rectF = new RectF();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float h = mHeight * 0.35f;
        float w = h;
        float startX = (mWidth - w) / 2;
        float startY = (mHeight - h) / 2;

        canvas.drawCircle(mWidth / 2f, mHeight / 2f, h / 2f, paintCircle);

        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.rotate(45);

        float factor = 0.51f;
        rect.set((int) (-w / 2f * factor), (int) (-strokeWidth / 2f), (int) (w / 2f * factor), (int) (strokeWidth / 2f));
        rectF.set(rect);
        canvas.drawRect(rectF, paint);

        canvas.rotate(90);
        canvas.drawRect(rectF, paint);
        canvas.rotate(-135);
        canvas.translate(-mWidth / 2, -mHeight / 2);
    }
}
