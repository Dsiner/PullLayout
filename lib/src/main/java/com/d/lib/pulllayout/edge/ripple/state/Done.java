package com.d.lib.pulllayout.edge.ripple.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;

import com.d.lib.pulllayout.util.Utils;

/**
 * Done
 * Created by D on 2018/3/30.
 */
public class Done extends State {
    float mScaleAX = 0.2659f;
    float mScaleAY = 0.4588f;
    float mScaleBX = 0.4541f;
    float mScaleBY = 0.6306f;
    float mScaleCX = 0.7553f;
    float mScaleCY = 0.3388f;

    private float mStrokeWidth;
    private Path mPath;
    private Path mPathTick;
    private Paint mPaintTick;
    private Paint mPaintCircle;
    private PathMeasure mTickPathMeasure;

    public Done(View view) {
        super(view);
        init(mContext);
    }

    private void init(Context context) {
        mStrokeWidth = Utils.dp2px(context, 2.5f);

        mPath = new Path();
        mPathTick = new Path();

        mTickPathMeasure = new PathMeasure();

        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setColor(mColor);

        mPaintTick = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTick.setColor(mColorWhite);
        mPaintTick.setStrokeCap(Paint.Cap.ROUND);
        mPaintTick.setStyle(Paint.Style.STROKE);
        mPaintTick.setStrokeWidth(mStrokeWidth);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float h = mHeight * 0.35f;
        float w = h;
        float startX = (mWidth - w) / 2;
        float startY = (mHeight - h) / 2;
        mPathTick.moveTo(startX + w * mScaleAX, startY + h * mScaleAY);
        mPathTick.lineTo(startX + w * mScaleBX, startY + h * mScaleBY);
        mPathTick.lineTo(startX + w * mScaleCX, startY + h * mScaleCY);
        mTickPathMeasure.setPath(mPathTick, false);
        /*
         * On KITKAT and earlier releases, the resulting path may not display on a hardware-accelerated Canvas.
         * A simple workaround is to add a single operation to this path, such as dst.rLineTo(0, 0).
         */
        mTickPathMeasure.getSegment(0, mTickPathMeasure.getLength(), mPath, true);
        mPath.rLineTo(0, 0);
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, h / 2f, mPaintCircle);
        canvas.drawPath(mPath, mPaintTick);
    }
}
