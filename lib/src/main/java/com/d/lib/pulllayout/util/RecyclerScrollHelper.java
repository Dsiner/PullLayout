package com.d.lib.pulllayout.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerScrollHelper
 * Created by D on 2020/4/25.
 */
public class RecyclerScrollHelper {
    private final int LAST_VISIBLE_POSITION = 2;

    private final View mView;
    private List<OnScrollListener> mScrollListeners;
    private AbsListView.OnScrollListener mOnListViewScrollListener;
    private RecyclerView.OnScrollListener mOnRecyclerViewScrollListener;

    public RecyclerScrollHelper(View view) {
        mView = view;
    }

    private AbsListView.OnScrollListener getOnListViewScrollListener() {
        if (mOnListViewScrollListener == null) {
            mOnListViewScrollListener = new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (mScrollListeners != null) {
                        for (int i = mScrollListeners.size() - 1; i >= 0; i--) {
                            if (mScrollListeners.get(i) instanceof OnListViewScrollListener) {
                                ((OnListViewScrollListener) mScrollListeners.get(i))
                                        .onScrollStateChanged(view, scrollState);
                            }
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    if (mScrollListeners != null) {
                        for (int i = mScrollListeners.size() - 1; i >= 0; i--) {
                            if (mScrollListeners.get(i) instanceof OnListViewScrollListener) {
                                ((OnListViewScrollListener) mScrollListeners.get(i))
                                        .onScroll(view, firstVisibleItem,
                                                visibleItemCount, totalItemCount);
                            }
                        }
                    }
                    if (view.getCount() > 0
                            && view.getLastVisiblePosition() >= view.getCount() - LAST_VISIBLE_POSITION) {
                        if (mScrollListeners != null) {
                            for (int i = mScrollListeners.size() - 1; i >= 0; i--) {
                                if (mScrollListeners.get(i) instanceof OnBottomScrollListener) {
                                    ((OnBottomScrollListener) mScrollListeners.get(i)).onBottom();
                                }
                            }
                        }
                    }
                }
            };
        }
        return mOnListViewScrollListener;
    }

    private RecyclerView.OnScrollListener getOnRecyclerViewScrollListener() {
        if (mOnRecyclerViewScrollListener == null) {
            mOnRecyclerViewScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int lastVisibleItemPosition;
                    if (layoutManager instanceof GridLayoutManager) {
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                        lastVisibleItemPosition = findMax(into);
                    } else {
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }
                    if (layoutManager.getChildCount() > 0
                            && lastVisibleItemPosition >= layoutManager.getItemCount() - LAST_VISIBLE_POSITION) {
                        if (mScrollListeners != null) {
                            for (int i = mScrollListeners.size() - 1; i >= 0; i--) {
                                if (mScrollListeners.get(i) instanceof OnBottomScrollListener) {
                                    ((OnBottomScrollListener) mScrollListeners.get(i)).onBottom();
                                }
                            }
                        }
                    }
                }
            };
        }
        return mOnRecyclerViewScrollListener;
    }

    public void addOnScrollListener(final OnScrollListener listener) {
        if (mScrollListeners == null) {
            mScrollListeners = new ArrayList<>();
        }
        mScrollListeners.add(listener);
        if (mView instanceof ListView) {
            ((ListView) mView).setOnScrollListener(getOnListViewScrollListener());
        } else if (mView instanceof RecyclerView) {
            ((RecyclerView) mView).removeOnScrollListener(getOnRecyclerViewScrollListener());
            ((RecyclerView) mView).addOnScrollListener(getOnRecyclerViewScrollListener());
        }
    }

    public void removeOnScrollListener(final OnScrollListener listener) {
        if (mScrollListeners != null) {
            mScrollListeners.remove(listener);
        }
        if (mView instanceof ListView) {
            // ignore
        } else if (mView instanceof RecyclerView) {
            ((RecyclerView) mView).removeOnScrollListener(getOnRecyclerViewScrollListener());
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    interface OnScrollListener {
    }

    public abstract static class OnBottomScrollListener implements OnScrollListener {
        public abstract void onBottom();
    }

    public abstract static class OnListViewScrollListener
            implements OnScrollListener, AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
}
