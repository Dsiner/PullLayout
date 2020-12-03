package com.d.lib.pulllayout;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.d.lib.pulllayout.util.AppBarHelper;
import com.d.lib.pulllayout.util.NestedAnimHelper;
import com.d.lib.pulllayout.util.NestedScrollHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * PullLayout
 * Created by D on 2018/5/29.
 */
public class PullLayout extends ViewGroup implements Pullable {
    protected static final int INVALID_POINTER = -1;
    protected static final int INVALID_ORIENTATION = -1;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    protected int mTouchSlop;
    protected int mOrientation = INVALID_ORIENTATION;
    protected int mPullPointerId = INVALID_POINTER;
    protected int mTouchX, mTouchY;
    protected int mLastTouchX, mLastTouchY;
    protected final NestedScrollHelper.Offset mPullOffsetX = new NestedScrollHelper.Offset();
    protected final NestedScrollHelper.Offset mPullOffsetY = new NestedScrollHelper.Offset();
    protected int mPullState = Pullable.PULL_STATE_IDLE;
    protected NestedAnimHelper mNestedAnimHelper;
    protected AppBarHelper mAppBarHelper;

    protected boolean mEnable;
    protected int mGravity;
    protected boolean mCanPullDown = true;
    protected boolean mCanPullUp = true;
    protected List<Pullable.OnPullListener> mOnPullListeners;

    public PullLayout(Context context) {
        this(context, null);
    }

    public PullLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pull_PullLayout);
        mEnable = typedArray.getBoolean(R.styleable.lib_pull_PullLayout_lib_pull_enable, true);
        mGravity = typedArray.getInt(R.styleable.lib_pull_PullLayout_lib_pull_gravity, 0x1111);
        typedArray.recycle();
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mNestedAnimHelper = new NestedAnimHelper(this) {
            @Override
            public void onState(int state) {
                super.onState(state);
            }
        };
        mNestedAnimHelper.setOnPullListener(new OnPullListener() {
            @Override
            public void onPullStateChanged(Pullable pullable, int newState) {
                setPullState(newState);
            }

            @Override
            public void onPulled(Pullable pullable, int x, int y) {
                scrollTo(x, y);
                invalidate();
            }
        });
        mAppBarHelper = new AppBarHelper(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
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
            case MotionEvent.ACTION_DOWN:
                stopNestedAnim();
                mPullPointerId = ev.getPointerId(0);
                mLastTouchX = mTouchX = (int) (ev.getX() + 0.5f);
                mLastTouchY = mTouchY = (int) (ev.getY() + 0.5f);
                if (getScrollX() != 0) {
                    scrollTo(getScrollX(), 0);
                    mOrientation = HORIZONTAL;
                    setPullState(Pullable.PULL_STATE_DRAGGING);
                } else if (getScrollY() != 0) {
                    scrollTo(0, getScrollY());
                    mOrientation = VERTICAL;
                    setPullState(Pullable.PULL_STATE_DRAGGING);
                } else {
                    mOrientation = INVALID_ORIENTATION;
                    setPullState(Pullable.PULL_STATE_IDLE);
                }
                mPullOffsetX.init(getScrollX());
                mPullOffsetY.init(getScrollY());
                super.dispatchTouchEvent(ev);
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                mPullPointerId = ev.getPointerId(actionIndex);
                mLastTouchX = mTouchX = (int) (ev.getX(actionIndex) + 0.5f);
                mLastTouchY = mTouchY = (int) (ev.getY(actionIndex) + 0.5f);
                super.dispatchTouchEvent(ev);
                return true;

            case MotionEvent.ACTION_MOVE:
                final int index = ev.findPointerIndex(mPullPointerId);
                if (index < 0) {
                    Log.e("PullLayout", "Error processing scroll; pointer index for id "
                            + mPullPointerId + " not found. Did any MotionEvents get skipped?");
                    super.dispatchTouchEvent(ev);
                    return true;
                }
                final int x = (int) (ev.getX(index) + 0.5f);
                final int y = (int) (ev.getY(index) + 0.5f);

                final int dx = mLastTouchX - x;
                final int dy = mLastTouchY - y;

                if (mPullState != Pullable.PULL_STATE_DRAGGING) {
                    boolean startScroll = false;
                    if (mOrientation != VERTICAL
                            && Math.abs(x - mTouchX) > mTouchSlop
                            && Math.abs(x - mTouchX) > Math.abs(y - mTouchY)) {
                        if (canStartScrollHorizontally(canNestedScrollHorizontally, dx)) {
                            mOrientation = HORIZONTAL;
                            mPullOffsetX.orientation(dx > 0 ? NestedScrollHelper.Offset.POSITIVE
                                    : NestedScrollHelper.Offset.NEGATIVE);
                            mPullOffsetY.orientation(NestedScrollHelper.Offset.INVALID_ORIENTATION);
                            startScroll = true;
                        }
                    } else if (mOrientation != HORIZONTAL
                            && Math.abs(y - mTouchY) > mTouchSlop
                            && Math.abs(y - mTouchY) > Math.abs(x - mTouchX)) {
                        if (canStartScrollVertically(canNestedScrollVertically, dy)) {
                            mOrientation = VERTICAL;
                            mPullOffsetX.orientation(NestedScrollHelper.Offset.INVALID_ORIENTATION);
                            mPullOffsetY.orientation(dy > 0 ? NestedScrollHelper.Offset.POSITIVE
                                    : NestedScrollHelper.Offset.NEGATIVE);
                            startScroll = true;
                        }
                    }
                    if (startScroll) {
                        // resetTouch();
                        mLastTouchX = mTouchX = x;
                        mLastTouchY = mTouchY = y;
                        setPullState(Pullable.PULL_STATE_DRAGGING);
                        super.dispatchTouchEvent(ev);
                        return true;
                    }
                }

                if (mPullState == Pullable.PULL_STATE_DRAGGING) {
                    boolean stopScroll = false;
                    if (mOrientation == HORIZONTAL) {
                        scrollTo(mPullOffsetX.onPulled(dx), 0);
                        if (getScrollX() == 0) {
                            stopScroll = true;
                        }
                    } else if (mOrientation == VERTICAL) {
                        scrollTo(0, mPullOffsetY.onPulled(dy));
                        if (getScrollY() == 0) {
                            stopScroll = true;
                        }
                    }
                    if (stopScroll) {
                        mLastTouchX = mTouchX = x;
                        mLastTouchY = mTouchY = y;
                        setPullState(Pullable.PULL_STATE_IDLE);
                        super.dispatchTouchEvent(ev);
                        return true;
                    }
                }
                mLastTouchX = x;
                mLastTouchY = y;
                if (mPullState != Pullable.PULL_STATE_DRAGGING) {
                    super.dispatchTouchEvent(ev);
                }
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                onPointerUp(ev);
                super.dispatchTouchEvent(ev);
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mOrientation == INVALID_ORIENTATION) {
                    return super.dispatchTouchEvent(ev);
                }
                if (mPullState == Pullable.PULL_STATE_DRAGGING) {
                    startNestedAnim(0, 0);
                }
                mOrientation = INVALID_ORIENTATION;
                ev.setAction(MotionEvent.ACTION_CANCEL);
                super.dispatchTouchEvent(ev);
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void cancelEvent() {
        final long now = SystemClock.uptimeMillis();
        final MotionEvent cancelEvent = MotionEvent.obtain(now, now,
                MotionEvent.ACTION_CANCEL, 0.0f, 0.0f, 0);
        super.dispatchTouchEvent(cancelEvent);
        if (cancelEvent != null) {
            cancelEvent.recycle();
        }
    }

    protected void onPointerUp(MotionEvent e) {
        final int actionIndex = e.getActionIndex();
        if (e.getPointerId(actionIndex) == mPullPointerId) {
            // Pick a new pointer to pick up the slack.
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mPullPointerId = e.getPointerId(newIndex);
            mLastTouchX = mTouchX = (int) (e.getX(newIndex) + 0.5f);
            mLastTouchY = mTouchY = (int) (e.getY(newIndex) + 0.5f);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        dispatchOnPullScrolled(l, t);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Solve the conflict with CollapsingToolbarLayout
        mAppBarHelper.setOnOffsetChangedListener();
    }

    @Override
    public int getPullState() {
        return mPullState;
    }

    @Override
    public void setPullState(int state) {
        if (state == mPullState) {
            return;
        }
        mPullState = state;
        dispatchOnPullStateChanged(state);
    }

    @Override
    public boolean canPullDown() {
        return mCanPullDown;
    }

    @Override
    public void setCanPullDown(boolean enable) {
        this.mCanPullDown = enable;
        this.mGravity = enable ? this.mGravity | NestedScrollHelper.GRAVITY_TOP
                : this.mGravity & (~NestedScrollHelper.GRAVITY_TOP);
    }

    @Override
    public boolean canPullUp() {
        return mCanPullUp;
    }

    @Override
    public void setCanPullUp(boolean enable) {
        this.mCanPullUp = enable;
        this.mGravity = enable ? this.mGravity | NestedScrollHelper.GRAVITY_BOTTOM
                : this.mGravity & (~NestedScrollHelper.GRAVITY_BOTTOM);
    }

    @Override
    public void setPullFactor(float factor) {
        this.mPullOffsetX.setPullFactor(factor);
        this.mPullOffsetY.setPullFactor(factor);
    }

    @Override
    public void setDuration(int duration) {
        this.mNestedAnimHelper.setDuration(duration);
    }

    @Override
    public void setInterpolator(TimeInterpolator value) {
        this.mNestedAnimHelper.setInterpolator(value);
    }

    private boolean canStartScrollVertically(boolean[] canNestedScroll, int deltas) {
        return !canNestedScroll[0] && deltas < 0 && mAppBarHelper.isExpanded()
                || !canNestedScroll[1] && deltas > 0;
    }

    private boolean canStartScrollHorizontally(boolean[] canNestedScroll, int deltas) {
        return !canNestedScroll[0] && deltas < 0 || !canNestedScroll[1] && deltas > 0;
    }

    protected boolean[] canNestedScrollVertically() {
        boolean[] enables = NestedScrollHelper.canNestedScrollVertically(getNestedChild());
        return new boolean[]{!NestedScrollHelper.isEnable(
                NestedScrollHelper.GRAVITY_TOP, mGravity) || enables[0],
                !NestedScrollHelper.isEnable(
                        NestedScrollHelper.GRAVITY_BOTTOM, mGravity) || enables[1]};
    }

    protected boolean[] canNestedScrollHorizontally() {
        boolean[] enables = NestedScrollHelper.canNestedScrollHorizontally(getNestedChild());
        return new boolean[]{!NestedScrollHelper.isEnable(
                NestedScrollHelper.GRAVITY_LEFT, mGravity) || enables[0],
                !NestedScrollHelper.isEnable(
                        NestedScrollHelper.GRAVITY_RIGHT, mGravity) || enables[1]};
    }

    protected View getNestedChild() {
        return getChildAt(0);
    }

    protected void startNestedAnim(int destX, int destY) {
        mNestedAnimHelper.postNestedAnim(getScrollX(), getScrollY(), destX, destY);
    }

    protected boolean stopNestedAnim() {
        return mNestedAnimHelper.stopNestedAnim();
    }

    protected void dispatchOnPullStateChanged(int state) {
        // Listeners go last. All other internal state is consistent by this point.
        if (mOnPullListeners != null) {
            for (int i = mOnPullListeners.size() - 1; i >= 0; i--) {
                mOnPullListeners.get(i).onPullStateChanged(this, state);
            }
        }
    }

    protected void dispatchOnPullScrolled(int hresult, int vresult) {
        // Pass the real deltas to onScrolled
        if (mOnPullListeners != null) {
            for (int i = mOnPullListeners.size() - 1; i >= 0; i--) {
                mOnPullListeners.get(i).onPulled(this, hresult, vresult);
            }
        }
    }

    @Override
    public void addOnPullListener(OnPullListener listener) {
        if (mOnPullListeners == null) {
            mOnPullListeners = new ArrayList<>();
        }
        mOnPullListeners.add(listener);
    }

    @Override
    public void removeOnPullListener(OnPullListener listener) {
        if (mOnPullListeners != null) {
            mOnPullListeners.remove(listener);
        }
    }

    @Override
    public void clearOnPullListeners() {
        if (mOnPullListeners != null) {
            mOnPullListeners.clear();
        }
    }
}
