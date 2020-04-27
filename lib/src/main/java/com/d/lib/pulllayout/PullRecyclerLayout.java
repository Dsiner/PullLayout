package com.d.lib.pulllayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.d.lib.pulllayout.edge.IEdgeView;
import com.d.lib.pulllayout.edge.IState;
import com.d.lib.pulllayout.edge.arrow.FooterView;
import com.d.lib.pulllayout.edge.arrow.HeaderView;
import com.d.lib.pulllayout.util.ScrollChangeHelper;

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
    private View mRecyclerList;
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
        mType = typedArray.getInt(R.styleable.lib_pull_PullRecyclerLayout_lib_pull_type, TYPE_RECYCLERVIEW);
        typedArray.recycle();

        if (mType == TYPE_LISTVIEW) {
            mRecyclerList = new ListView(context);
        } else {
            RecyclerView view = new RecyclerView(context);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            view.setLayoutManager(layoutManager);
            mRecyclerList = view;
        }

        mHeaderView = getHeader();
        mFooterView = getFooter();

        addView((View) mHeaderView);
        setNestedChild(mRecyclerList);
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
        if (isLoading()) {
            return;
        }
        mHeaderView.setState(IState.STATE_LOADING);
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    @Override
    public void loadMore() {
        if (isLoading() || mFooterView.getState() == IState.STATE_NO_MORE) {
            return;
        }
        mFooterView.setState(IState.STATE_LOADING);
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onLoadMore();
        }
    }

    @Override
    public void refreshSuccess() {
        onScrollTo(0, 0);
        mHeaderView.setState(IState.STATE_SUCCESS);
        mFooterView.setState(IState.STATE_NONE);
    }

    @Override
    public void refreshError() {
        onScrollTo(0, 0);
        mHeaderView.setState(IState.STATE_SUCCESS);
        mFooterView.setState(IState.STATE_NONE);
    }

    @Override
    public void loadMoreSuccess() {
        onScrollTo(0, 0);
        mFooterView.setState(IState.STATE_SUCCESS);
    }

    @Override
    public void loadMoreError() {
        onScrollTo(0, 0);
        mFooterView.setState(IState.STATE_ERROR);
    }

    @Override
    public void loadMoreNoMore() {
        onScrollTo(0, 0);
        mFooterView.setState(IState.STATE_NO_MORE);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    public boolean autoLoadMore() {
        return true;
    }

    protected boolean isLoading() {
        return mHeaderView.getState() == IState.STATE_LOADING
                || mFooterView.getState() == IState.STATE_LOADING;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("PullRecyclerLayout", "onLayout changed: " + changed
                + " l: " + l + " t: " + t + " r: " + r + " b: " + b);
        final int childCount = getChildCount();
        if (childCount >= 3) {
            View header = (View) mHeaderView;
            header.layout(0, -header.getMeasuredHeight(),
                    header.getMeasuredWidth(), 0);
            mRecyclerList = getChildAt(1);
            mRecyclerList.layout(0, 0,
                    mRecyclerList.getMeasuredWidth(), mRecyclerList.getMeasuredHeight());
            View footer = (View) mFooterView;
            int footerTop = mRecyclerList.getMeasuredHeight();
            footerTop = footerTop > 0 ? footerTop : getMeasuredHeight();
            footer.layout(0, footerTop,
                    footer.getMeasuredWidth(), footerTop + footer.getMeasuredHeight());
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
                if (mOrientation == INVALID_ORIENTATION) {
                    return super.dispatchTouchEvent(ev);
                }
                if (mPullState == Pullable.PULL_STATE_DRAGGING) {
                    if (canPullDown() && getScrollY() < -mHeaderView.getExpandedOffset()) {
                        refresh();
                        onScrollTo(getScrollX(), mHeaderView.getState() == IState.STATE_LOADING
                                ? -mHeaderView.getExpandedOffset() : 0);
                    } else if (canPullUp() && getScrollY() > mFooterView.getExpandedOffset()) {
                        loadMore();
                        onScrollTo(getScrollX(), mFooterView.getState() == IState.STATE_LOADING
                                ? mFooterView.getExpandedOffset() : 0);
                    } else {
                        onScrollTo(0, 0);
                    }
                    setPullState(Pullable.PULL_STATE_IDLE);
                }
                mOrientation = INVALID_ORIENTATION;
                ev.setAction(MotionEvent.ACTION_CANCEL);
                super.dispatchTouchEvent(ev);
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected boolean[] canNestedScrollHorizontally() {
        return new boolean[]{true, true};
    }

    @Override
    public View getNestedChild() {
        return mRecyclerList;
    }

    public void setNestedChild(View view) {
        if (!(view instanceof RecyclerView)
                && !(view instanceof ListView)) {
            throw new IllegalArgumentException("View type must be RecyclerView or ListView.");
        }

        removeView(mRecyclerList);
        addView(view, 1);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(lp);
        mRecyclerList = view;
        if (mRecyclerList instanceof RecyclerView) {
            ScrollChangeHelper.addOnScrollListener((RecyclerView) mRecyclerList, new ScrollChangeHelper.OnScrollListener() {
                @Override
                public void onBottom() {
                    if (!autoLoadMore() || mFooterView.getState() == IState.STATE_ERROR) {
                        return;
                    }
                    loadMore();
                }
            });
        } else if (mRecyclerList instanceof ListView) {
            ScrollChangeHelper.setOnScrollListener((ListView) mRecyclerList, new ScrollChangeHelper.OnScrollListener() {
                @Override
                public void onBottom() {
                    if (!autoLoadMore() || mFooterView.getState() == IState.STATE_ERROR) {
                        return;
                    }
                    loadMore();
                }
            });
        }
        requestLayout();
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
