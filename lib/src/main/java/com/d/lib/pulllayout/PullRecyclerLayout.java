package com.d.lib.pulllayout;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.d.lib.pulllayout.edge.IEdgeView;
import com.d.lib.pulllayout.edge.IState;
import com.d.lib.pulllayout.edge.arrow.FooterView;
import com.d.lib.pulllayout.edge.arrow.HeaderView;
import com.d.lib.pulllayout.loader.RecyclerAdapter;
import com.d.lib.pulllayout.rv.PullRecyclerView;
import com.d.lib.pulllayout.util.NestedScrollHelper;
import com.d.lib.pulllayout.util.RecyclerScrollHelper;

/**
 * PullRecyclerLayout
 * Created by D on 2020/3/21.
 */
public class PullRecyclerLayout extends PullLayout implements Refreshable {
    private static final int TYPE_LISTVIEW = 1;
    private static final int TYPE_RECYCLERVIEW = 2;
    private static final int TYPE_PULLRECYCLERVIEW = 3;

    @NonNull
    private IEdgeView mHeaderView;
    @NonNull
    private IEdgeView mFooterView;
    private final int mType;
    private View mRecyclerList;
    private boolean mAutoLoadMore = true;
    private RecyclerScrollHelper mRecyclerScrollHelper;
    private RecyclerScrollHelper.OnBottomScrollListener mOnBottomScrollListener;
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
        mType = typedArray.getInt(R.styleable.lib_pull_PullRecyclerLayout_lib_pull_type, TYPE_PULLRECYCLERVIEW);
        typedArray.recycle();

        if (TYPE_LISTVIEW == mType) {
            mRecyclerList = new ListView(context);

        } else if (TYPE_RECYCLERVIEW == mType) {
            mRecyclerList = new RecyclerView(context);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ((RecyclerView) mRecyclerList).setLayoutManager(layoutManager);

        } else if (TYPE_PULLRECYCLERVIEW == mType) {
            mRecyclerList = new PullRecyclerView(context);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ((PullRecyclerView) mRecyclerList).setCanPullDown(false);
            ((PullRecyclerView) mRecyclerList).setCanPullUp(false);
            ((PullRecyclerView) mRecyclerList).setLayoutManager(layoutManager);
        }

        mHeaderView = new HeaderView(getContext());
        mFooterView = new FooterView(getContext());

        resetAllViews();
    }

    private void resetAllViews() {
        removeAllViews();
        setHeader(mHeaderView);
        setNestedChild(mRecyclerList);
        setFooter(mFooterView);
    }

    @Override
    public IEdgeView getHeader() {
        return mHeaderView;
    }

    @Override
    public void setHeader(IEdgeView view) {
        removeView((View) mHeaderView);
        addView((View) view, 0);
        view.setOnNestedAnimListener(getOnNestedAnimListener(view));
        mHeaderView = view;
    }

    @Override
    public IEdgeView getFooter() {
        return mFooterView;
    }

    @Override
    public void setFooter(IEdgeView view) {
        removeView((View) mFooterView);
        addView((View) view);
        view.setOnNestedAnimListener(getOnNestedAnimListener(view));
        view.setOnFooterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canPullUp()) {
                    return;
                }
                loadMore();
            }
        });
        mFooterView = view;
    }

    @Override
    public boolean autoLoadMore() {
        return mAutoLoadMore;
    }

    @Override
    public void setAutoLoadMore(boolean enable) {
        mAutoLoadMore = enable;
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
        stopNestedAnim();
        mHeaderView.setState(IState.STATE_SUCCESS);
        mFooterView.setState(IState.STATE_NONE);
    }

    @Override
    public void refreshError() {
        stopNestedAnim();
        mHeaderView.setState(IState.STATE_SUCCESS);
        mFooterView.setState(IState.STATE_NONE);
    }

    @Override
    public void loadMoreSuccess() {
        stopNestedAnim();
        mFooterView.setState(IState.STATE_SUCCESS);
    }

    @Override
    public void loadMoreError() {
        stopNestedAnim();
        mFooterView.setState(IState.STATE_ERROR);
    }

    @Override
    public void loadMoreNoMore() {
        stopNestedAnim();
        mFooterView.setState(IState.STATE_NO_MORE);
    }

    protected boolean isLoading() {
        return mHeaderView.getState() == IState.STATE_LOADING
                || mFooterView.getState() == IState.STATE_LOADING;
    }

    public void setAdapter(RecyclerAdapter adapter) {
        View nestedChild = getNestedChild();
        if (nestedChild instanceof RecyclerView) {
            ((RecyclerView) nestedChild).setAdapter((RecyclerView.Adapter) adapter);
        } else if (nestedChild instanceof ListView) {
            ((ListView) nestedChild).setAdapter((ListAdapter) adapter);
        }
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
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

        final int action = ev.getActionMasked();
        final int actionIndex = ev.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mOrientation == INVALID_ORIENTATION) {
                    return super.dispatchTouchEvent(ev);
                }
                if (mPullState == Pullable.PULL_STATE_DRAGGING) {
                    if (canPullDown() && getScrollY() < -mHeaderView.getExpandedOffset()
                            && mAppBarHelper.isExpanded()) {
                        refresh();
                        mHeaderView.startNestedAnim(getScrollX(),
                                getScrollY(),
                                getScrollX(),
                                mHeaderView.getState() == IState.STATE_LOADING
                                        ? -mHeaderView.getExpandedOffset() : 0);
                    } else if (canPullUp() && getScrollY() > mFooterView.getExpandedOffset()) {
                        loadMore();
                        mFooterView.startNestedAnim(getScrollX(),
                                getScrollY(),
                                getScrollX(),
                                mFooterView.getState() == IState.STATE_LOADING
                                        ? mFooterView.getExpandedOffset() : 0);
                    } else {
                        startNestedAnim(getScrollX(), getScrollY(), 0, 0);
                    }
                }
                mOrientation = INVALID_ORIENTATION;
                ev.setAction(MotionEvent.ACTION_CANCEL);
                super.dispatchTouchEvent(ev);
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setDuration(int duration) {
        mHeaderView.setDuration(duration);
        mFooterView.setDuration(duration);
        super.setDuration(duration);
    }

    @Override
    public void setInterpolator(TimeInterpolator value) {
        mHeaderView.setInterpolator(value);
        mFooterView.setInterpolator(value);
        super.setInterpolator(value);
    }

    @Override
    public void startNestedAnim(int startX, int startY, int destX, int destY) {
        stopNestedAnim();
        super.startNestedAnim(startX, startY, destX, destY);
    }

    @Override
    public boolean stopNestedAnim() {
        mHeaderView.stopNestedAnim();
        mFooterView.stopNestedAnim();
        return super.stopNestedAnim();
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
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(lp);

        mRecyclerList = view;
        mRecyclerList.setHorizontalScrollBarEnabled(false);
        mRecyclerList.setVerticalScrollBarEnabled(false);
        mRecyclerList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        if (mRecyclerList instanceof ListView) {
            ((ListView) mRecyclerList).setDivider(null);
            ((ListView) mRecyclerList).setSelector(new ColorDrawable());
            ((ListView) mRecyclerList).setCacheColorHint(Color.TRANSPARENT);
        }
        if (mOnBottomScrollListener == null) {
            mOnBottomScrollListener = new RecyclerScrollHelper.OnBottomScrollListener() {
                @Override
                public void onBottom() {
                    if (!canPullUp() || !autoLoadMore()
                            || !NestedScrollHelper.canNestedScrollVertically(mRecyclerList)[0]
                            || mFooterView.getState() == IState.STATE_ERROR) {
                        return;
                    }
                    loadMore();
                }
            };
        }
        if (mRecyclerScrollHelper != null) {
            mRecyclerScrollHelper.removeOnScrollListener(mOnBottomScrollListener);
        }
        mRecyclerScrollHelper = new RecyclerScrollHelper(mRecyclerList);
        mRecyclerScrollHelper.addOnScrollListener(mOnBottomScrollListener);
        requestLayout();
    }

    @Override
    public void dispatchPulled(int hresult, int vresult) {
        if (-vresult >= 0) {
            mHeaderView.onPulled(hresult, Math.max(0, -vresult));
        }
        if (vresult >= 0) {
            mFooterView.onPulled(hresult, Math.max(0, vresult));
        }
        super.dispatchPulled(hresult, vresult);
    }
}
