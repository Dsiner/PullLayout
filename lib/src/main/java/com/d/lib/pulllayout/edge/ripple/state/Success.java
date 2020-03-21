package com.d.lib.pulllayout.edge.ripple.state;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.d.lib.pulllayout.util.Utils;

import java.lang.ref.WeakReference;

/**
 * Success
 * Created by D on 2018/3/30.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Success extends State {
    protected String mContent;

    protected State mSign;
    protected Paint mPaint;
    protected Paint mPaintText;
    protected float mTextHeight;

    private ValueAnimator mAnimator;
    private float mFactor;
    private boolean mTaskRunning;
    private int mStep;
    private Task mTask;
    private Handler mHandler;

    static class AnimListenerAdapter extends AnimatorListenerAdapter {
        private final WeakReference<Success> reference;

        AnimListenerAdapter(Success view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            Success view = reference.get();
            if (view == null || view.mContext == null
                    || view.mContext instanceof Activity && ((Activity) view.mContext).isFinishing()) {
                return;
            }
            view.mStep = 1;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            Success view = reference.get();
            if (view == null || view.mContext == null
                    || view.mContext instanceof Activity && ((Activity) view.mContext).isFinishing()) {
                return;
            }
            view.mStep = 0;
        }
    }

    static class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final WeakReference<Success> reference;

        AnimUpdateListener(Success view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Success view = reference.get();
            if (view == null || view.mContext == null
                    || view.mContext instanceof Activity && ((Activity) view.mContext).isFinishing()
                    || view.mView == null) {
                return;
            }
            view.mStep = 1;
            view.mFactor = (float) animation.getAnimatedValue();
            view.mView.invalidate();
        }
    }

    static class Task implements Runnable {

        private final WeakReference<Success> reference;

        Task(Success view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void run() {
            Success view = reference.get();
            if (view == null || view.mContext == null
                    || view.mContext instanceof Activity && ((Activity) view.mContext).isFinishing()
                    || !view.mTaskRunning) {
                return;
            }
            view.mAnimator.start();
        }
    }

    @Override
    public void reStart() {
        stop();
        mTaskRunning = true;
        mStep = 0;
        mView.invalidate();
        mHandler.postDelayed(mTask, 700);
    }

    @Override
    public void stop() {
        mAnimator.cancel();
        mTaskRunning = false;
        mStep = 0;
        mHandler.removeCallbacks(mTask);
    }

    public Success(View view) {
        super(view);
        init(mContext);
        initAttr(mContext);
    }

    private void init(Context context) {
        mTask = new Task(this);
        mHandler = new Handler();

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(450);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addListener(new AnimListenerAdapter(this));
        mAnimator.addUpdateListener(new AnimUpdateListener(this));
    }

    protected void initAttr(Context context) {
        mSign = new Done(mView);
        mContent = "刷新成功";

        mPaintText.setColor(mColorWhite);
        mPaintText.setTextSize(Utils.dp2px(context, 14));
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mTextHeight = Utils.getTextHeight(mPaintText);

        mPaint.setColor(mColor);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStep == 0) {
            mSign.onDraw(canvas);
            return;
        }
        float start = mHeight * 0.35f / 2f;
        float end = (float) Math.sqrt(mHeight / 2f * mHeight / 2f + mWidth / 2f * mWidth / 2f) + 1;
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, start + (end - start) * mFactor, mPaint);

        canvas.drawText(mContent, mWidth / 2f, mHeight / 2 + mTextHeight / 2, mPaintText);
    }

    @Override
    public void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        super.setMeasuredDimension(measuredWidth, measuredHeight);
        mSign.setMeasuredDimension(measuredWidth, measuredHeight);
    }
}
