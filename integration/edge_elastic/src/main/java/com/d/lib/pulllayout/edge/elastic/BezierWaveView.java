package com.d.lib.pulllayout.edge.elastic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.pulllayout.util.Utils;

public class BezierWaveView extends View {
    private int mWidth, mHeight;
    private Paint mPaint;
    private Path mPath;
    private int mColor;
    private float mFactor;

    public BezierWaveView(Context context) {
        this(context, null);
    }

    public BezierWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        mColor = Color.parseColor("#F9F9F9");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWave(canvas);
    }

    private void drawWave(Canvas canvas) {
        final float height = Utils.dp2px(getContext(), 22);
        final float expandedOffset = Utils.dp2px(getContext(), 126);
        final float baseLineY = mHeight - height;
        final float offset = mHeight * mFactor;
        mPath.reset();
        mPath.moveTo(0, mHeight);
        mPath.lineTo(0, baseLineY - height * 2 * mFactor);
        mPath.quadTo(offset >= 0 ? mWidth / 3f + offset : mWidth / 2f,
                Math.min(mHeight, mHeight),
                mWidth, baseLineY - height * 2 * mFactor);
        mPath.lineTo(mWidth, mHeight);
        mPaint.setColor(mColor);
        canvas.drawPath(mPath, mPaint);
    }

    public void update(float factor) {
        mFactor = factor;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }
}
