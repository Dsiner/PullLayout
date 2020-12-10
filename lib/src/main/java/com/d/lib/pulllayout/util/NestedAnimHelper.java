package com.d.lib.pulllayout.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.d.lib.pulllayout.Pullable;

import java.lang.ref.WeakReference;

public class NestedAnimHelper {
    private final View mView;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final WeakRunnable mWeakRunnable;
    private final ValueAnimator mAnimation;
    private final AnimUpdateListener mAnimUpdateListener;
    private final AnimListenerAdapter mAnimListenerAdapter;
    private int mDuration = 250;
    private Pullable.OnPullListener mOnNestedAnimListener;

    static class WeakRunnable implements Runnable {
        private final WeakReference<NestedAnimHelper> reference;
        private int startX, startY, destX, destY;
        private int state;

        WeakRunnable(NestedAnimHelper view) {
            this.reference = new WeakReference<>(view);
        }

        void ofInt(int startX, int startY, int destX, int destY, int state) {
            this.startX = startX;
            this.startY = startY;
            this.destX = destX;
            this.destY = destY;
            this.state = state;
        }

        @Override
        public void run() {
            if (isFinished(reference)) {
                return;
            }
            NestedAnimHelper view = reference.get();
            view.startNestedAnim(startX, startY, destX, destY, state);
        }
    }

    static class AnimListenerAdapter extends AnimatorListenerAdapter {
        private final WeakReference<NestedAnimHelper> reference;
        private int state;

        AnimListenerAdapter(NestedAnimHelper view) {
            this.reference = new WeakReference<>(view);
        }

        void ofEnd(int state) {
            this.state = state;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (isFinished(reference)) {
                return;
            }
            final NestedAnimHelper view = reference.get();
            view.onAnimStateChanged(null, Pullable.PULL_STATE_IDLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (isFinished(reference)) {
                return;
            }
            final NestedAnimHelper view = reference.get();
            view.onAnimStateChanged(null, Pullable.PULL_STATE_IDLE);
            if (state != -1) {
                view.onState(state);
            }
        }
    }

    static class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final WeakReference<NestedAnimHelper> reference;
        private int startX, startY, destX, destY;

        AnimUpdateListener(NestedAnimHelper view) {
            this.reference = new WeakReference<>(view);
        }

        void ofInt(int startX, int startY, int destX, int destY) {
            this.startX = startX;
            this.startY = startY;
            this.destX = destX;
            this.destY = destY;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (isFinished(reference)) {
                return;
            }
            final NestedAnimHelper view = reference.get();
            final float factor = (float) animation.getAnimatedValue();
            final int scrollX = (int) (startX + (destX - startX) * factor);
            final int scrollY = (int) (startY + (destY - startY) * factor);

            view.onAnimStateChanged(null, Pullable.PULL_STATE_SETTLING);
            view.onAnimUpdate(null, scrollX, scrollY);
        }
    }

    private static boolean isFinished(WeakReference<NestedAnimHelper> reference) {
        final View view = reference.get() != null ? reference.get().mView : null;
        return view == null || view.getContext() == null
                || view.getContext() instanceof Activity
                && ((Activity) view.getContext()).isFinishing();
    }

    public NestedAnimHelper(View view) {
        mView = view;
        mWeakRunnable = new WeakRunnable(this);
        mAnimation = ValueAnimator.ofFloat(0f, 1f);
        mAnimation.setDuration(mDuration);
        mAnimation.setInterpolator(new DecelerateInterpolator());
        mAnimUpdateListener = new AnimUpdateListener(this);
        mAnimListenerAdapter = new AnimListenerAdapter(this);
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
        this.mAnimation.setDuration(duration);
    }

    public void setInterpolator(TimeInterpolator value) {
        this.mAnimation.setInterpolator(value);
    }

    public void startNestedAnim(int startX, int startY, int destX, int destY) {
        postNestedAnimDelayed(startX, startY, destX, destY, 0, -1);
    }

    public void postNestedAnimDelayed(int startX, int startY, int destX, int destY,
                                      long delayMillis,
                                      int state) {
        stopNestedAnim();
        if (startX == destX && startY == destY) {
            onAnimStateChanged(null, Pullable.PULL_STATE_IDLE);
            return;
        }
        if (delayMillis <= 0) {
            startNestedAnim(startX, startY, destX, destY, state);
            return;
        }
        mWeakRunnable.ofInt(startX, startY, destX, destY, state);
        mHandler.postDelayed(mWeakRunnable, delayMillis);
    }

    private void startNestedAnim(int startX, int startY, int destX, int destY,
                                 int state) {
        stopNestedAnim();
        mAnimUpdateListener.ofInt(startX, startY, destX, destY);
        mAnimListenerAdapter.ofEnd(state);
        mAnimation.addUpdateListener(mAnimUpdateListener);
        mAnimation.addListener(mAnimListenerAdapter);
        mAnimation.start();
    }

    public boolean stopNestedAnim() {
        mHandler.removeCallbacksAndMessages(null);
        boolean running = mAnimation.isRunning();
        mAnimation.removeAllUpdateListeners();
        mAnimation.removeAllListeners();
        mAnimation.cancel();
        return running;
    }

    public void onState(int state) {
    }

    private void onAnimStateChanged(Pullable pullable, int newState) {
        if (mOnNestedAnimListener != null) {
            mOnNestedAnimListener.onPullStateChanged(pullable, newState);
        }
    }

    private void onAnimUpdate(Pullable pullable, int x, int y) {
        if (mOnNestedAnimListener != null) {
            mOnNestedAnimListener.onPulled(pullable, x, y);
        }
    }

    public void setOnNestedAnimListener(Pullable.OnPullListener l) {
        this.mOnNestedAnimListener = l;
    }
}
