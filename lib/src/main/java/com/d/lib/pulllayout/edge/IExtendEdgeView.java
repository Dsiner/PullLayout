package com.d.lib.pulllayout.edge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.d.lib.pulllayout.Pullable;

import java.lang.ref.WeakReference;

public interface IExtendEdgeView extends IEdgeView {

    void dispatchPulled(float dx, float dy);

    void postNestedAnim(int destX, int destY);

    void setOnPullListener(Pullable.OnPullListener l);

    class NestedExtendChildHelper {
        private EdgeView mEdgeView;
        private Handler mHandler = new Handler(Looper.getMainLooper());
        private WeakRunnable mWeakRunnable;
        private int mDuration = 450;
        private ValueAnimator mAnimation;
        private AnimUpdateListener mAnimUpdateListener;
        private AnimListenerAdapter mAnimListenerAdapter;
        private Pullable.OnPullListener mOnPullListener;

        static class WeakRunnable implements Runnable {
            private final WeakReference<NestedExtendChildHelper> reference;
            private int x, y, destX, destY;
            private boolean reset;

            WeakRunnable(NestedExtendChildHelper view) {
                this.reference = new WeakReference<>(view);
            }

            void ofInt(int x, int y, int destX, int destY, boolean reset) {
                this.x = x;
                this.y = y;
                this.destX = destX;
                this.destY = destY;
                this.reset = reset;
            }

            @Override
            public void run() {
                if (isFinish(reference)) {
                    return;
                }
                NestedExtendChildHelper view = reference.get();
                view.startNestedAnim(destX, destY, reset);
            }
        }

        static class AnimListenerAdapter extends AnimatorListenerAdapter {
            private final WeakReference<NestedExtendChildHelper> reference;
            private boolean reset;

            AnimListenerAdapter(NestedExtendChildHelper view) {
                this.reference = new WeakReference<>(view);
            }

            void reset(boolean reset) {
                this.reset = reset;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (isFinish(reference)) {
                    return;
                }
                NestedExtendChildHelper view = reference.get();
                if (view.mOnPullListener != null) {
                    view.mOnPullListener.onPullStateChanged(null, Pullable.PULL_STATE_IDLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isFinish(reference)) {
                    return;
                }
                NestedExtendChildHelper view = reference.get();
                if (reset) {
                    view.mEdgeView.setState(STATE_NONE);
                }
                if (view.mOnPullListener != null) {
                    view.mOnPullListener.onPullStateChanged(null, Pullable.PULL_STATE_IDLE);
                }
            }
        }

        static class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
            private final WeakReference<NestedExtendChildHelper> reference;
            private int x, y, destX, destY;

            AnimUpdateListener(NestedExtendChildHelper view) {
                this.reference = new WeakReference<>(view);
            }

            void ofInt(int x, int y, int destX, int destY) {
                this.x = x;
                this.y = y;
                this.destX = destX;
                this.destY = destY;
            }

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isFinish(reference)) {
                    return;
                }
                NestedExtendChildHelper view = reference.get();
                final float factor = (float) animation.getAnimatedValue();
                final int scrollY = (int) (y + (destY - y) * factor);
                view.setVisibleHeight(scrollY);
                if (view.mOnPullListener != null) {
                    view.mOnPullListener.onPullStateChanged(null, Pullable.PULL_STATE_SETTLING);
                    view.mOnPullListener.onPulled(null, 0, scrollY);
                }
            }
        }

        private static boolean isFinish(WeakReference<NestedExtendChildHelper> reference) {
            NestedExtendChildHelper view = reference.get();
            return view == null || view.mEdgeView.getContext() == null
                    || view.mEdgeView.getContext() instanceof Activity
                    && ((Activity) view.mEdgeView.getContext()).isFinishing();
        }

        public NestedExtendChildHelper(EdgeView view) {
            mEdgeView = view;
            mWeakRunnable = new WeakRunnable(this);
            mAnimation = ValueAnimator.ofFloat(0f, 1f);
            mAnimation.setDuration(mDuration);
            mAnimation.setInterpolator(new DecelerateInterpolator());
            mAnimUpdateListener = new AnimUpdateListener(this);
            mAnimListenerAdapter = new AnimListenerAdapter(this);
        }

        public void dispatchPulled(float dx, float dy) {
            Log.d("EdgeView", "dispatchPulled: " + dy);
            dy = dy * Pullable.PULL_FACTOR;
            if (getVisibleHeight() > 0 || dy > 0) {
                int height = Math.max(0, (int) dy + getVisibleHeight());
                setVisibleHeight(height);
                Log.d("EdgeView", "getVisibleHeight: " + getVisibleHeight());
            }
        }

        public void reset() {
            postNestedAnim(0, 0, mDuration, true);
        }

        public void postNestedAnim(int destX, int destY) {
            postNestedAnim(destX, destY, 0, false);
        }

        private void postNestedAnim(int destX, int destY, long delayMillis, boolean reset) {
            stopNestedAnim();
            if (getVisibleHeight() == destY) {
                if (mOnPullListener != null) {
                    mOnPullListener.onPullStateChanged(null, Pullable.PULL_STATE_IDLE);
                }
                return;
            }
            mWeakRunnable.ofInt(0, getVisibleHeight(), 0, destY, reset);
            mHandler.postDelayed(mWeakRunnable, delayMillis);
        }

        private void startNestedAnim(int destX, int destY, boolean reset) {
            stopNestedAnim();
            mAnimUpdateListener.ofInt(0, getVisibleHeight(), 0, destY);
            mAnimation.addUpdateListener(mAnimUpdateListener);
            mAnimListenerAdapter.reset(reset);
            mAnimation.addListener(mAnimListenerAdapter);
            mAnimation.start();
        }

        private boolean stopNestedAnim() {
            mHandler.removeCallbacksAndMessages(null);
            boolean running = mAnimation.isRunning();
            mAnimation.removeAllUpdateListeners();
            mAnimation.removeAllListeners();
            mAnimation.cancel();
            return running;
        }

        public void setVisibleHeight(int height) {
            if (height < 0) {
                height = 0;
            }
            ViewGroup.LayoutParams lp = mEdgeView.mContainer.getLayoutParams();
            lp.height = height;
            mEdgeView.mContainer.setLayoutParams(lp);
        }

        public int getVisibleHeight() {
            ViewGroup.LayoutParams lp = mEdgeView.mContainer.getLayoutParams();
            return lp.height;
        }

        public void setOnPullListener(Pullable.OnPullListener l) {
            this.mOnPullListener = l;
        }
    }
}