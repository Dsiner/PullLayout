package com.d.lib.pulllayout.edge.ripple.state;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.d.lib.pulllayout.util.Utils;

import java.lang.ref.WeakReference;

/**
 * Loading
 * Created by D on 2018/3/30.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Loading extends State {
    private Paint mPaint;
    private Paint mPaintCircle;

    private Rect mRect;
    private RectF mRectF;
    private int mSpace;

    private ValueAnimator mAnimator;
    private float mFactor;
    private boolean mTaskRunning;

    public Loading(View view) {
        super(view);
        init(mContext);
    }

    @Override
    public void reStart() {
        stop();
        mTaskRunning = true;
        mAnimator.start();
    }

    @Override
    public void stop() {
        mAnimator.cancel();
        mTaskRunning = false;
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

        mAnimator = ValueAnimator.ofFloat(225f, 585f);
        mAnimator.setDuration(1000);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new AnimUpdateListener(this));
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
        canvas.drawArc(mRectF, mFactor, 90, false, mPaint);
    }

    static class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final WeakReference<Loading> reference;

        AnimUpdateListener(Loading view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Loading view = reference.get();
            if (view == null || view.mContext == null
                    || view.mContext instanceof Activity && ((Activity) view.mContext).isFinishing()
                    || view.mView == null) {
                return;
            }
            view.mFactor = (float) animation.getAnimatedValue();
            view.mView.invalidate();
        }
    }
}
