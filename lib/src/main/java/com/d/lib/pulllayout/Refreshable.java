package com.d.lib.pulllayout;

import com.d.lib.pulllayout.edge.IEdgeView;

/**
 * Refreshable
 * Created by D on 2017/4/26.
 */
public interface Refreshable {

    IEdgeView getHeader();

    void setHeader(IEdgeView view);

    IEdgeView getFooter();

    void setFooter(IEdgeView view);

    boolean autoLoadMore();

    void setAutoLoadMore(boolean enable);

    void reset();

    void refresh();

    void loadMore();

    void refreshSuccess();

    void refreshError();

    void loadMoreSuccess();

    void loadMoreError();

    void loadMoreNoMore();

    void setVisibility(int visibility);

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    void setOnRefreshListener(OnRefreshListener listener);

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    interface OnRefreshListener {
        /**
         * Called when a swipe gesture triggers a refresh.
         */
        void onRefresh();

        void onLoadMore();
    }
}
