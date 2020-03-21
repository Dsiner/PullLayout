package com.d.lib.pulllayout.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.d.lib.pulllayout.AppBarStateChangeListener;
import com.d.lib.pulllayout.Pullable;
import com.d.lib.pulllayout.edge.IEdgeView;
import com.d.lib.pulllayout.edge.IState;
import com.d.lib.pulllayout.edge.arrow.FooterView;
import com.d.lib.pulllayout.edge.arrow.HeaderView;
import com.d.lib.pulllayout.rv.adapter.WrapAdapter;
import com.d.lib.pulllayout.rv.adapter.WrapAdapterDataObserver;

import java.util.ArrayList;
import java.util.List;

public class PullRecyclerView extends RecyclerView implements Pullable {
    private static final int INVALID_POINTER = -1;

    @NonNull
    private final IEdgeView mHeaderView;
    @NonNull
    private final IEdgeView mFooterView;
    @NonNull
    private final HeaderList mHeaderList;

    private boolean mCanPullDown = true;
    private boolean mCanPullUp = true;
    private int mPullPointerId = INVALID_POINTER;
    private float mLastY = -1;
    private int mPullState = Pullable.PULL_STATE_IDLE;
    private AppBarStateChangeListener.State mAppbarState = AppBarStateChangeListener.State.EXPANDED;
    private WrapAdapter mWrapAdapter;
    private OnRefreshListener mOnRefreshListener;
    private List<OnPullListener> mOnPullListeners;

    public PullRecyclerView(Context context) {
        this(context, null);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHeaderView = getHeader();
        mFooterView = getFooter();
        mHeaderList = new HeaderList();
    }

    @NonNull
    protected IEdgeView getHeader() {
        return new HeaderView(getContext());
    }

    @NonNull
    protected IEdgeView getFooter() {
        FooterView view = new FooterView(getContext());
        view.setOnFooterClickListener(new IEdgeView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canPullUp() || isLoading()) {
                    return;
                }
                loadMore();
            }
        });
        return view;
    }

    public void reset() {
        loadMoreSuccess();
        refreshSuccess();
    }

    public void refresh() {
        if (!canPullDown() || isLoading()) {
            return;
        }
        mHeaderView.setState(IState.STATE_LOADING);
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    public void loadMore() {
        if (!canPullUp() || isLoading()) {
            return;
        }
        mFooterView.setState(IState.STATE_LOADING);
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onLoadMore();
        }
    }

    public void refreshSuccess() {
        mHeaderView.setState(IState.STATE_SUCCESS);
        mFooterView.setState(IState.STATE_NONE);
    }

    public void refreshError() {
        mHeaderView.setState(IState.STATE_SUCCESS);
        mFooterView.setState(IState.STATE_NONE);
    }

    public void loadMoreSuccess() {
        mFooterView.setState(IState.STATE_SUCCESS);
    }

    public void loadMoreError() {
        mFooterView.setState(IState.STATE_ERROR);
    }

    public void loadMoreNoMore() {
        mFooterView.setState(IState.STATE_NO_MORE);
    }

    public boolean isLoading() {
        return mHeaderView.getState() == IState.STATE_LOADING
                || mFooterView.getState() == IState.STATE_LOADING;
    }

    private boolean isOnTop() {
        boolean attached = ((View) mHeaderView).getParent() != null;
        int offset = ((View) mHeaderView).getBottom();
        return attached && offset >= 0;
    }

    private boolean isOnBottom() {
        boolean attached = ((View) mFooterView).getParent() != null;
        int offset = ((View) mFooterView).getTop() - getHeight();
        return attached && offset <= 0;
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

    public void addHeaderView(@NonNull View view) {
        mHeaderList.add(view);
        if (mWrapAdapter != null) {
            mWrapAdapter.notifyDataSetChanged();
        }
    }

    public boolean removeHeaderView(View v) {
        return mHeaderList.remove(v);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter, mHeaderView, mFooterView, mHeaderList);
        mWrapAdapter.setCanPullDown(mCanPullDown);
        mWrapAdapter.setCanPullUp(mCanPullUp);
        WrapAdapterDataObserver adapterDataObserver = new WrapAdapterDataObserver(mWrapAdapter);
        adapter.registerAdapterDataObserver(adapterDataObserver);
        super.setAdapter(mWrapAdapter);
    }

    /**
     * Retrieves the previously set wrap adapter or null if no adapter is set.
     */
    @Override
    public Adapter getAdapter() {
        if (mWrapAdapter != null) {
            return mWrapAdapter.getOriginalAdapter();
        } else {
            return null;
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (mWrapAdapter != null) {
            if (layout instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) layout);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (mWrapAdapter.isHeaderList(position)
                                || mWrapAdapter.isFooter(position)
                                || mWrapAdapter.isHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (true) {
            return;
        }
        if (state != RecyclerView.SCROLL_STATE_IDLE
                || !canPullUp() || isLoading()
                || mFooterView.getState() == IState.STATE_NO_MORE) {
            return;
        }
        LayoutManager layoutManager = getLayoutManager();
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
                && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
            loadMore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final int actionIndex = ev.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPullPointerId = ev.getPointerId(actionIndex);
                mLastY = ev.getY();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mPullPointerId = ev.getPointerId(actionIndex);
                mLastY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getY() - mLastY;
                mLastY = ev.getY();
                if (canPullDown() && isOnTop()
                        && mAppbarState == AppBarStateChangeListener.State.EXPANDED) {
                    mHeaderView.dispatchPulled(0, deltaY);
                    int offset = ((View) mHeaderView).getBottom();
                    if (offset > 0 && mHeaderView.getState() < IState.STATE_LOADING) {
                        mHeaderView.onPulled(0, -offset);
                        dispatchOnPullStateChanged(PULL_STATE_DRAGGING);
                        dispatchOnPullScrolled(0, -offset);
                        return false;
                    }
                } else if (canPullUp() && isOnBottom()) {
                    mFooterView.dispatchPulled(0, -deltaY);
                    int offset = getHeight() - ((View) mFooterView).getTop();
                    if (offset > 0 && mFooterView.getState() < IState.STATE_LOADING) {
                        mFooterView.onPulled(0, offset);
                        dispatchOnPullStateChanged(PULL_STATE_DRAGGING);
                        dispatchOnPullScrolled(0, offset);
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastY = -1;
                if (canPullDown() && isOnTop()
                        && mAppbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mHeaderView.getState() == IState.STATE_EXPANDED) {
                        if (mOnRefreshListener != null) {
                            mOnRefreshListener.onRefresh();
                        }
                    }
                    mHeaderView.onPullStateChanged(PULL_STATE_IDLE);
                    dispatchOnPullStateChanged(PULL_STATE_IDLE);
                } else if (canPullUp() && isOnBottom()) {
                    if (mFooterView.getState() == IState.STATE_EXPANDED) {
                        if (mOnRefreshListener != null) {
                            mOnRefreshListener.onLoadMore();
                        }
                    }
                    mFooterView.onPullStateChanged(PULL_STATE_IDLE);
                    dispatchOnPullStateChanged(PULL_STATE_IDLE);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void onPointerUp(MotionEvent e) {
        final int actionIndex = e.getActionIndex();
        if (e.getPointerId(actionIndex) == mPullPointerId) {
            // Pick a new pointer to pick up the slack.
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mPullPointerId = e.getPointerId(newIndex);
            mLastY = e.getY(newIndex);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Solve the conflict with CollapsingToolbarLayout
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        mAppbarState = state;
                    }
                });
            }
        }
    }

    @Override
    public int getPullState() {
        return mPullState;
    }

    @Override
    public void setPullState(int state) {
        mPullState = state;
    }

    @Override
    public boolean canPullDown() {
        return mCanPullDown;
    }

    @Override
    public boolean canPullUp() {
        return mCanPullUp;
    }

    @Override
    public void setCanPullDown(boolean enable) {
        this.mCanPullDown = enable;
        if (!enable) {
            mHeaderView.setState(IState.STATE_NONE);
        }
        if (mWrapAdapter != null) {
            mWrapAdapter.setCanPullDown(enable);
            mWrapAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setCanPullUp(boolean enable) {
        this.mCanPullUp = enable;
        if (!enable) {
            mFooterView.setState(IState.STATE_NONE);
        }
        if (mWrapAdapter != null) {
            mWrapAdapter.setCanPullUp(enable);
            mWrapAdapter.notifyDataSetChanged();
        }
    }

    void dispatchOnPullStateChanged(int state) {
        // Listeners go last. All other internal state is consistent by this point.
        if (mOnPullListeners != null) {
            for (int i = mOnPullListeners.size() - 1; i >= 0; i--) {
                mOnPullListeners.get(i).onPullStateChanged(this, state);
            }
        }
    }

    private void dispatchOnPullScrolled(int hresult, int vresult) {
        // Pass the real deltas to onScrolled
        if (mOnPullListeners != null) {
            for (int i = mOnPullListeners.size() - 1; i >= 0; i--) {
                mOnPullListeners.get(i).onPulled(this, hresult, vresult);
            }
        }
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    public void setOnPullListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    /**
     * Add a listener that will be notified of any changes in pull state or position.
     *
     * <p>Components that add a listener should take care to remove it when finished.
     * Other components that take ownership of a view may call {@link #clearOnScrollListeners()}
     * to remove all attached listeners.</p>
     *
     * @param listener listener to set or null to clear
     */
    public void addOnPullScrollListener(OnPullListener listener) {
        if (mOnPullListeners == null) {
            mOnPullListeners = new ArrayList<>();
        }
        mOnPullListeners.add(listener);
    }

    /**
     * Remove a listener that was notified of any changes in pull state or position.
     *
     * @param listener listener to set or null to clear
     */
    public void removeOnPullScrollListener(OnPullListener listener) {
        if (mOnPullListeners != null) {
            mOnPullListeners.remove(listener);
        }
    }

    /**
     * Remove all secondary listener that were notified of any changes in pull state or position.
     */
    public void clearOnPullScrollListeners() {
        if (mOnPullListeners != null) {
            mOnPullListeners.clear();
        }
    }
}