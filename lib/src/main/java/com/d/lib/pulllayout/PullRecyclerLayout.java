package com.d.lib.pulllayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.d.lib.pulllayout.edge.IEdgeView;
import com.d.lib.pulllayout.edge.IState;
import com.d.lib.pulllayout.edge.arrow.FooterView;
import com.d.lib.pulllayout.edge.arrow.HeaderView;

public class PullRecyclerLayout extends PullLayout {
    @NonNull
    private final IEdgeView mHeaderView;
    @NonNull
    private final IEdgeView mFooterView;
    private final RecyclerView mRecyclerView;

    public PullRecyclerLayout(Context context) {
        this(context, null);
    }

    public PullRecyclerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecyclerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderView = getHeader();
        mFooterView = getFooter();
        mRecyclerView = new RecyclerView(context);
        addView((View) mHeaderView);
        addView(mRecyclerView);
        addView((View) mFooterView);
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
        startAnim(0, 0);
        mHeaderView.setState(IState.STATE_SUCCESS);
        mFooterView.setState(IState.STATE_NONE);
    }

    public void refreshError() {
        startAnim(0, 0);
        mHeaderView.setState(IState.STATE_SUCCESS);
        mFooterView.setState(IState.STATE_NONE);
    }

    public void loadMoreSuccess() {
        startAnim(0, 0);
        mFooterView.setState(IState.STATE_SUCCESS);
    }

    public void loadMoreError() {
        startAnim(0, 0);
        mFooterView.setState(IState.STATE_ERROR);
    }

    public void loadMoreNoMore() {
        startAnim(0, 0);
        mFooterView.setState(IState.STATE_NO_MORE);
    }

    @Override
    protected boolean isLoading() {
        return mHeaderView.getState() == IState.STATE_LOADING
                || mFooterView.getState() == IState.STATE_LOADING;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (count >= 3) {
            View header = (View) mHeaderView;
            header.layout(0, -header.getMeasuredHeight(),
                    header.getMeasuredWidth(), 0);
            mRecyclerView.layout(0, 0,
                    mRecyclerView.getMeasuredWidth(), mRecyclerView.getMeasuredHeight());
            View footer = (View) mFooterView;
            footer.layout(0, mRecyclerView.getMeasuredHeight(),
                    footer.getMeasuredWidth(), mRecyclerView.getMeasuredHeight() + footer.getMeasuredHeight());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mEnable || getChildCount() <= 0) {
            return super.dispatchTouchEvent(ev);
        }

        final boolean[] canNestedScrollHorizontally = canNestedScrollHorizontally();
        final boolean[] canNestedScrollVertically = canNestedScrollVertically();

        final int action = ev.getActionMasked();
        final int actionIndex = ev.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mPullState == Pullable.PULL_STATE_DRAGGING) {
                    if (canPullDown() && getScrollY() < -mHeaderView.getExpandedOffset()) {
                        if (mHeaderView.getState() == IState.STATE_EXPANDED) {
                            if (mOnRefreshListener != null) {
                                mOnRefreshListener.onRefresh();
                            }
                            mHeaderView.setState(IState.STATE_LOADING);
                        }
                        if (mHeaderView.getState() == IState.STATE_LOADING) {
                            onActionUp(getScrollX(), -mHeaderView.getExpandedOffset());
                        }
                    } else if (canPullUp() && getScrollY() > mFooterView.getExpandedOffset()) {
                        if (mFooterView.getState() == IState.STATE_EXPANDED) {
                            if (mOnRefreshListener != null) {
                                mOnRefreshListener.onLoadMore();
                            }
                            mFooterView.setState(IState.STATE_LOADING);
                        }
                        if (mFooterView.getState() == IState.STATE_LOADING) {
                            onActionUp(getScrollX(), mFooterView.getExpandedOffset());
                        }
                    }
                    setPullState(Pullable.PULL_STATE_IDLE);
                }
                super.dispatchTouchEvent(ev);
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected View getNestedChild() {
        return mRecyclerView;
    }

    @Override
    protected void dispatchOnPullStateChanged(int state) {
        mHeaderView.onPullStateChanged(state);
        mFooterView.onPullStateChanged(state);
        super.dispatchOnPullStateChanged(state);
    }

    @Override
    protected void dispatchOnPullScrolled(int hresult, int vresult) {
        if (-vresult >= 0) {
            mHeaderView.onPulled(hresult, Math.max(0, -vresult));
        }
        if (vresult >= 0) {
            mFooterView.onPulled(hresult, Math.max(0, vresult));
        }
        super.dispatchOnPullScrolled(hresult, vresult);
    }
}
