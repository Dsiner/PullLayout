package com.d.lib.pulllayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.d.lib.pulllayout.edge.IEdgeView;
import com.d.lib.pulllayout.edge.IState;
import com.d.lib.pulllayout.edge.arrow.FooterView;
import com.d.lib.pulllayout.edge.arrow.HeaderView;

/**
 * PullRecyclerLayout
 * Created by D on 2020/3/21.
 */
public class PullRecyclerLayout extends PullLayout implements Refreshable {
    private static final int TYPE_RECYCLERVIEW = 1;
    private static final int TYPE_LISTVIEW = 2;

    @NonNull
    private final IEdgeView mHeaderView;
    @NonNull
    private final IEdgeView mFooterView;
    private final View mRecyclerList;
    private final int mType;
    private OnRefreshListener mOnRefreshListener;

    public PullRecyclerLayout(Context context) {
        this(context, null);
    }

    public PullRecyclerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecyclerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pull_PullRecyclerLayout);
        mType = typedArray.getInt(R.styleable.lib_pull_PullRecyclerLayout_lib_pull_type, 1);
        typedArray.recycle();

        mHeaderView = getHeader();
        mFooterView = getFooter();
        mRecyclerList = mType == TYPE_LISTVIEW ? new ListView(context) : new RecyclerView(context);
        addView((View) mHeaderView);
        addView(mRecyclerList);
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

    @Override
    public void reset() {
        loadMoreSuccess();
        refreshSuccess();
    }

    @Override
    public void refresh() {
        if (!canPullDown() || isLoading()) {
            return;
        }
        mHeaderView.setState(IState.STATE_LOADING);
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    @Override
    public void loadMore() {
        if (!canPullUp() || isLoading()) {
            return;
        }
        mFooterView.setState(IState.STATE_LOADING);
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onLoadMore();
        }
    }

    @Override
    public void refreshSuccess() {
        startAnim(0, 0);
        mHeaderView.setState(IState.STATE_SUCCESS);
        mFooterView.setState(IState.STATE_NONE);
    }

    @Override
    public void refreshError() {
        startAnim(0, 0);
        mHeaderView.setState(IState.STATE_SUCCESS);
        mFooterView.setState(IState.STATE_NONE);
    }

    @Override
    public void loadMoreSuccess() {
        startAnim(0, 0);
        mFooterView.setState(IState.STATE_SUCCESS);
    }

    @Override
    public void loadMoreError() {
        startAnim(0, 0);
        mFooterView.setState(IState.STATE_ERROR);
    }

    @Override
    public void loadMoreNoMore() {
        startAnim(0, 0);
        mFooterView.setState(IState.STATE_NO_MORE);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
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
            mRecyclerList.layout(0, 0,
                    mRecyclerList.getMeasuredWidth(), mRecyclerList.getMeasuredHeight());
            View footer = (View) mFooterView;
            footer.layout(0, mRecyclerList.getMeasuredHeight(),
                    footer.getMeasuredWidth(), mRecyclerList.getMeasuredHeight() + footer.getMeasuredHeight());
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
        return mRecyclerList;
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
