package com.d.lib.pulllayout.edge;

import android.animation.TimeInterpolator;
import android.view.View;

import com.d.lib.pulllayout.Pullable;

/**
 * IEdgeView
 * Created by D on 2017/4/25.
 */
public interface IEdgeView extends IState {
    int getVisibleHeight();

    void setVisibleHeight(int height);

    int getExpandedOffset();

    void setPullFactor(float factor);

    void dispatchPulled(float dx, float dy);

    void setDuration(int duration);

    void setInterpolator(TimeInterpolator value);

    void startNestedAnim(int destX, int destY);

    void postNestedAnimDelayed(int destX, int destY, long delayMillis, int state);

    boolean stopNestedAnim();

    void onPulled(float dx, float dy);

    void setOnNestedAnimListener(IEdgeView.OnNestedAnimListener l);

    void setOnFooterClickListener(View.OnClickListener listener);

    abstract class OnNestedAnimListener extends Pullable.OnPullListener {
        public abstract int getStartX();

        public abstract int getStartY();
    }
}
