package com.d.lib.pulllayout.edge;

import android.view.View;

/**
 * IEdgeView
 * Created by D on 2017/4/25.
 */
public interface IEdgeView extends IState {

    void onPulled(float dx, float dy);

    int getExpandedOffset();

    void setOnFooterClickListener(OnClickListener listener);

    interface OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        void onClick(View v);
    }
}
