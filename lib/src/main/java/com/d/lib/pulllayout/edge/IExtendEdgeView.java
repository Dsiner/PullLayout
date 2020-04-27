package com.d.lib.pulllayout.edge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

public interface IExtendEdgeView extends IEdgeView {
    void onDispatchPulled(float dx, float dy);

    void onExtendTo(int destX, int destY);

    class NestedExtendChildHelper {
        private EdgeView mEdgeView;

        public NestedExtendChildHelper(EdgeView view) {
            mEdgeView = view;
        }

        public void dispatchPulled(float dx, float dy) {
            Log.d("EdgeView", "dispatchPulled: " + dy);
            dy = dy / IEdgeView.DRAG_FACTOR;
            if (getVisibleHeight() > 0 || dy > 0) {
                int height = Math.max(0, (int) dy + getVisibleHeight());
                if (mEdgeView.mState == STATE_NO_MORE) {
                    height = mEdgeView.getExpandedOffset();
                }
                setVisibleHeight(height);
                Log.d("EdgeView", "getVisibleHeight: " + getVisibleHeight());
            }
        }

        public void onExtendTo(int destX, int destY) {
            ValueAnimator anim = ValueAnimator.ofInt(getVisibleHeight(), destY);
            anim.setDuration(300);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setVisibleHeight((int) animation.getAnimatedValue());
                }
            });
            anim.start();
        }

        public void onReset() {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ValueAnimator anim = ValueAnimator.ofInt(getVisibleHeight(), 0);
                    anim.setDuration(300);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            setVisibleHeight((int) animation.getAnimatedValue());
                        }
                    });
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mEdgeView.setState(STATE_NONE);
                        }
                    });
                    anim.start();
                }
            }, 300);
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
    }
}