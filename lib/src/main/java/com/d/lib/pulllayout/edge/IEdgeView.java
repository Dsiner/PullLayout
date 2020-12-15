package com.d.lib.pulllayout.edge;

import android.view.View;

import com.d.lib.pulllayout.Pullable;

/**
 * IEdgeView
 * Created by D on 2017/4/25.
 */
public interface IEdgeView extends IState, INestedAnim, INestedExtend {
    int getVisibleHeight();

    void setVisibleHeight(int height);

    int getExpandedOffset();

    void onPulled(float dx, float dy);

    void setOnNestedAnimListener(IEdgeView.OnNestedAnimListener l);

    void setOnFooterClickListener(View.OnClickListener listener);

    abstract class OnNestedAnimListener extends Pullable.OnPullListener {
        public abstract int getStartX();

        public abstract int getStartY();
    }
}
