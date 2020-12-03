package com.d.lib.pulllayout.edge;

import android.animation.TimeInterpolator;
import android.util.Log;
import android.view.ViewGroup;

import com.d.lib.pulllayout.Pullable;
import com.d.lib.pulllayout.util.NestedAnimHelper;

public interface IExtendEdgeView extends IEdgeView {
    void setPullFactor(float factor);

    void setDuration(int duration);

    void setInterpolator(TimeInterpolator value);

    void postNestedAnim(int destX, int destY);

    void dispatchPulled(float dx, float dy);

    void setOnPullListener(Pullable.OnPullListener l);

    class NestedExtendChildHelper {
        private final EdgeView mEdgeView;
        private final NestedAnimHelper mNestedAnimHelper;
        private float mPullFactor = Pullable.PULL_FACTOR;

        public NestedExtendChildHelper(EdgeView view) {
            mEdgeView = view;
            mNestedAnimHelper = new NestedAnimHelper(view) {
                @Override
                public void onState(int state) {
                    mEdgeView.setState(state);
                }
            };
        }

        public void setVisibleHeight(int height) {
            height = Math.max(0, height);
            ViewGroup.LayoutParams lp = mEdgeView.mContainer.getLayoutParams();
            lp.height = height;
            mEdgeView.mContainer.setLayoutParams(lp);
        }

        public int getVisibleHeight() {
            ViewGroup.LayoutParams lp = mEdgeView.mContainer.getLayoutParams();
            return lp.height;
        }

        public void setPullFactor(float factor) {
            this.mPullFactor = factor;
        }

        public void setDuration(int duration) {
            this.mNestedAnimHelper.setDuration(duration);
        }

        public void setInterpolator(TimeInterpolator value) {
            this.mNestedAnimHelper.setInterpolator(value);
        }

        public void postNestedAnim(int destX, int destY) {
            this.mNestedAnimHelper.postNestedAnim(0, getVisibleHeight(), 0, destY);
        }

        public void resetDelayed(int delayMillis) {
            this.mNestedAnimHelper.postNestedAnimDelayed(0, getVisibleHeight(), 0, 0,
                    delayMillis, IState.STATE_NONE);
        }

        public void dispatchPulled(float dx, float dy) {
            Log.d("EdgeView", "dispatchPulled: " + dy);
            dy = dy * mPullFactor;
            if (getVisibleHeight() > 0 || dy > 0) {
                int height = Math.max(0, (int) dy + getVisibleHeight());
                setVisibleHeight(height);
                Log.d("EdgeView", "getVisibleHeight: " + getVisibleHeight());
            }
        }

        public void setOnPullListener(final Pullable.OnPullListener l) {
            this.mNestedAnimHelper.setOnPullListener(new Pullable.OnPullListener() {
                @Override
                public void onPullStateChanged(Pullable pullable, int newState) {
                    if (l != null) {
                        l.onPullStateChanged(pullable, newState);
                    }
                }

                @Override
                public void onPulled(Pullable pullable, int dx, int dy) {
                    setVisibleHeight(dy);
                    if (l != null) {
                        l.onPulled(pullable, dx, dy);
                    }
                }
            });
        }
    }
}