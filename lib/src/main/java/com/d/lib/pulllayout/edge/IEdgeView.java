package com.d.lib.pulllayout.edge;

import android.view.View;

/**
 * IEdgeView
 * Created by D on 2017/4/25.
 */
public interface IEdgeView extends IState {
    float DRAG_FACTOR = 3.0f;

    void onPulled(float dx, float dy);

    int getExpandedOffset();

    interface OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        void onClick(View v);
    }
}
