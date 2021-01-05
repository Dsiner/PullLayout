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
    private Paint mPaint;
    private Paint mPaintCircle;
    private Rect mRect;
    private RectF mRectF;
    private float mStrokeWidth;

    public Discon(View view) {
        super(view);
        init(mContext);
    }

    private void init(Context context) {
        mStrokeWidth = Utils.dp2px(context, 2.5f);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColorWhite);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setColor(mColorError);

        mRect = new Rect();
        mRectF = new RectF();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float h = mHeight * 0.35f;
        float w = h;
        float startX = (mWidth - w) / 2;
        float startY = (mHeight - h) / 2;

        canvas.drawCircle(mWidth / 2f, mHeight / 2f, h / 2f, mPaintCircle);

        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.rotate(45);

        float factor = 0.51f;
        mRect.set((int) (-w / 2f * factor), (int) (-mStrokeWidth / 2f), (int) (w / 2f * factor), (int) (mStrokeWidth / 2f));
        mRectF.set(mRect);
        canvas.drawRect(mRectF, mPaint);

        canvas.rotate(90);
        canvas.drawRect(mRectF, mPaint);
        canvas.rotate(-135);
        canvas.translate(-mWidth / 2, -mHeight / 2);
    }
}
