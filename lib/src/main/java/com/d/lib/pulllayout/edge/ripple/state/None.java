package com.d.lib.pulllayout.edge.ripple.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.d.lib.pulllayout.util.Utils;

/**
 * Loading
 * Created by D on 2018/3/30.
 */
public class None extends State {
    private Paint mPaint;
    private Paint mPaintCircle;

    private Rect mRect;
    private RectF mRectF;
    private int mSpace;

    public None(View view) {
        super(view);
        init(mContext);
    }

    private void init(Context context) {
        mSpace = Utils.dp2px(context, 2.5f);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColorWhite);
        mPaint.setStrokeWidth(Utils.dp2px(context, 2));
        mPaint.setStyle(Paint.Style.STROKE);

        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setColor(mColor);

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

        mRect.set((int) startX + mSpace, (int) startY + mSpace, (int) (startX + w) - mSpace, (int) (startY + h) - mSpace);
        mRectF.set(mRect);
        canvas.drawArc(mRectF, 225, 90, false, mPaint);
    }
}
