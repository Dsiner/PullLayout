package com.d.lib.refreshlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;

/**
 * RefreshLayout
 * Created by D on 2018/5/29.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RefreshLayout extends ViewGroup {
    private final static int GRAVITY_TOP = 0x1;
    private final static int GRAVITY_BOTTOM = 0x2;
    private final static int GRAVITY_LEFT = 0x4;
    private final static int GRAVITY_RIGHT = 0x8;

    private int mWidth;
    private int mHeight;

    private int mTouchSlop;
    private int mDuration = 250;
    private float mDampFactor = 0.6f; // 滑动阻尼系数
    private float mDX, mDY; // TouchEvent_ACTION_DOWN坐标(dX,dY)
    private float mLastX, mLastY; // TouchEvent最后一次坐标(lastX,lastY)
    private boolean mIsEventValid = true; // 本次touch事件是否有效
    private boolean mIsMoveValidX, mIsMoveValidY;

    /**
     * [0]: 外部拦截/Scroll
     * [1]: 外部拦截停止, 重新事件分发/NestedScroll
     */
    private boolean[] mCancle = new boolean[2];
    private boolean mIsRunning;
    private ValueAnimator mAnimation;
    private AnimUpdateListener mAnimUpdateListener;
    private AnimListenerAdapter mAnimListenerAdapter;
    private float mFactor; // 进度因子:0-1
    private int mCurX, mCurY, mDst;
    private boolean mEnable;
    private int mGravity;

    static class AnimListenerAdapter extends AnimatorListenerAdapter {
        private final WeakReference<RefreshLayout> reference;

        AnimListenerAdapter(RefreshLayout view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (isFinish()) {
                return;
            }
            RefreshLayout view = reference.get();
            view.mFactor = 1;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (isFinish()) {
                return;
            }
            RefreshLayout view = reference.get();
            view.mFactor = 1;
        }

        private boolean isFinish() {
            RefreshLayout view = reference.get();
            if (view == null || view.getContext() == null
                    || view.getContext() instanceof Activity && ((Activity) view.getContext()).isFinishing()
                    || !view.mIsRunning) {
                return true;
            }
            return false;
        }
    }

    static class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final WeakReference<RefreshLayout> reference;

        AnimUpdateListener(RefreshLayout view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (isFinish()) {
                return;
            }
            RefreshLayout view = reference.get();
            view.mFactor = (float) animation.getAnimatedValue();
            if (view.mDst == -1) {
                float scrollY = view.mCurY - view.mCurY * view.mFactor;
                view.scrollTo(0, (int) scrollY);
            } else if (view.mDst == 1) {
                float scrollX = view.mCurX - view.mCurX * view.mFactor;
                view.scrollTo((int) scrollX, 0);
            }
            view.invalidate();
        }

        private boolean isFinish() {
            RefreshLayout view = reference.get();
            if (view == null || view.getContext() == null
                    || view.getContext() instanceof Activity && ((Activity) view.getContext()).isFinishing()
                    || !view.mIsRunning) {
                return true;
            }
            return false;
        }
    }

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_refresh_RefreshLayout);
        mEnable = typedArray.getBoolean(R.styleable.lib_refresh_RefreshLayout_lib_refresh_refreshlayout_enable, true);
        mGravity = typedArray.getInt(R.styleable.lib_refresh_RefreshLayout_lib_refresh_refreshlayout_gravity, 0x11);
        typedArray.recycle();
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mAnimation = ValueAnimator.ofFloat(0f, 1f);
        mAnimation.setDuration(mDuration);
        mAnimation.setInterpolator(new DecelerateInterpolator());
        mAnimUpdateListener = new AnimUpdateListener(this);
        mAnimListenerAdapter = new AnimListenerAdapter(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (count > 0) {
            View child = getChildAt(0);
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
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
        final float eX = ev.getX();
        final float eY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stop();
                mLastX = mDX = eX;
                mLastY = mDY = eY;
                mIsMoveValidX = false;
                mIsMoveValidY = false;
                mIsEventValid = true;
                mCancle[0] = mCancle[1] = false;
                super.dispatchTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!mIsEventValid) {
                    return false;
                }
                int offsetX = (int) (mLastX - eX);
                int offsetY = (int) (mLastY - eY);
                mLastX = eX;
                mLastY = eY;
                if (!(mIsMoveValidX || mIsMoveValidY)) {
                    if (Math.abs(eX - mDX) > mTouchSlop && Math.abs(eX - mDX) > Math.abs(eY - mDY)) {
                        mIsMoveValidX = true;
                    } else if (Math.abs(eY - mDY) > mTouchSlop && Math.abs(eY - mDY) > Math.abs(eX - mDX)) {
                        mIsMoveValidY = true;
                    }
                }
                if (mIsMoveValidX) {
                    if (getScrollY() != 0) {
                        scrollTo(getScrollX(), 0);
                    }
                    dispatchMove(true, ev, offsetX, offsetY);
                } else if (mIsMoveValidY) {
                    if (getScrollX() != 0) {
                        scrollTo(0, getScrollY());
                    }
                    dispatchMove(false, ev, offsetX, offsetY);
                } else {
                    super.dispatchTouchEvent(ev);
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!mIsEventValid) {
                    return false;
                }
                if (mCancle[1]) {
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                if (mIsMoveValidX) {
                    super.dispatchTouchEvent(ev);
                    dealUp(true);
                    mIsMoveValidX = false;
                    mIsMoveValidY = false;
                    return true;
                } else if (mIsMoveValidY) {
                    super.dispatchTouchEvent(ev);
                    dealUp(false);
                    mIsMoveValidX = false;
                    mIsMoveValidY = false;
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void dispatchMove(boolean horizontal, MotionEvent ev, int offsetX, int offsetY) {
        boolean canScrollTop = isAbsList() || isScroller() ? ViewCompat.canScrollVertically(getChildAt(0), -1)
                : getScrollY() + offsetY >= 0;
        final boolean canScrollBottom = isAbsList() || isScroller() ? ViewCompat.canScrollVertically(getChildAt(0), 1)
                : getScrollY() + offsetY <= 0;
        final boolean canScrollLeft = isAbsList() || isScroller() ? ViewCompat.canScrollHorizontally(getChildAt(0), -1)
                : getScrollX() + offsetX >= 0;
        final boolean canScrollRight = isAbsList() || isScroller() ? ViewCompat.canScrollHorizontally(getChildAt(0), 1)
                : getScrollX() + offsetX <= 0;

        final boolean canScrollN = horizontal ? canScrollLeft : canScrollTop;
        final boolean canScrollP = horizontal ? canScrollRight : canScrollBottom;

        final int offset = horizontal ? offsetX : offsetY;
        final boolean isPositive = offset > 0;
        final int scroll = horizontal ? getScrollX() : getScrollY();

        final int gravityN = horizontal ? GRAVITY_LEFT : GRAVITY_TOP;
        final int gravityP = horizontal ? GRAVITY_RIGHT : GRAVITY_BOTTOM;

        if (isPositive) {
            moveImpl(horizontal, ev, offset, canScrollP, gravityP, scroll < 0);
        } else {
            moveImpl(horizontal, ev, offset, canScrollN, gravityN, scroll > 0);
        }
    }

    private void moveImpl(boolean horizontal, MotionEvent ev, int offset,
                          boolean canScroll, int gravity, boolean isScrollOver) {
        if (isGravityEnable(gravity)) {
            if (isScrollOver) {
                intercept(horizontal, ev, offset);
            } else {
                if (canScroll) {
                    releaseIntercept(ev);
                } else {
                    intercept(horizontal, ev, offset);
                }
            }
        } else {
            if (isScrollOver) {
                intercept(horizontal, ev, offset);
            } else {
                if (canScroll) {
                    releaseIntercept(ev);
                } else {
                    scrollTo(0, 0);
                    intercept(horizontal, ev, getScrollY());
                }
            }

            if (horizontal && gravity == GRAVITY_LEFT && getScrollX() < 0) {
                scrollTo(0, getScrollY());
            } else if (horizontal && gravity == GRAVITY_RIGHT && getScrollX() > 0) {
                scrollTo(0, getScrollY());
            } else if (!horizontal && gravity == GRAVITY_TOP && getScrollY() < 0) {
                scrollTo(getScrollX(), 0);
            } else if (!horizontal && gravity == GRAVITY_BOTTOM && getScrollY() > 0) {
                scrollTo(getScrollX(), 0);
            }
        }
    }

    @Deprecated
    private int getOffset(boolean horizontal, int gravity, int offset) {
        if (horizontal && gravity == GRAVITY_LEFT) {
            if (getScrollX() + offset < 0) {
                return -getScrollX();
            }
        } else if (horizontal && gravity == GRAVITY_RIGHT) {
            if (getScrollX() + offset > 0) {
                return -getScrollX();
            }
        } else if (!horizontal && gravity == GRAVITY_TOP) {
            if (getScrollY() + offset < 0) {
                return -getScrollY();
            }
        } else if (!horizontal && gravity == GRAVITY_BOTTOM) {
            if (getScrollY() + offset > 0) {
                return -getScrollY();
            }
        }
        return offset;
    }

    private void intercept(boolean horizontal, MotionEvent ev, int offset) {
        if (!mCancle[0]) {
            mCancle[0] = true;
            mCancle[1] = false;
            ev.setAction(MotionEvent.ACTION_CANCEL);
            super.dispatchTouchEvent(ev);
        }
        if (horizontal) {
            scrollBy((int) (offset * mDampFactor), 0);
        } else {
            scrollBy(0, (int) (offset * mDampFactor));
        }
    }

    private void releaseIntercept(MotionEvent ev) {
        if (mCancle[0]) {
            mCancle[0] = false;
            mCancle[1] = true;
            ev.setAction(MotionEvent.ACTION_DOWN);
        }
        super.dispatchTouchEvent(ev);
    }

    private void dealUp(boolean horizontal) {
        mCurX = getScrollX();
        mCurY = getScrollY();
        mDst = horizontal ? 1 : -1;
        start();
    }

    private boolean isGravityEnable(int g) {
        switch (g) {
            case GRAVITY_TOP:
                return (mGravity & 0x1) != 0;
            case GRAVITY_BOTTOM:
                return (mGravity & 0x2) != 0;
            case GRAVITY_LEFT:
                return (mGravity & 0x4) != 0;
            case GRAVITY_RIGHT:
                return (mGravity & 0x8) != 0;
            default:
                return false;
        }
    }

    private boolean isAbsList() {
        View view = getChildAt(0);
        return view != null && (view instanceof AbsListView || view instanceof RecyclerView);
    }

    private boolean isScroller() {
        View view = getChildAt(0);
        return view != null && (view instanceof ScrollView);
    }

    public void start() {
        stop();
        mIsRunning = true;
        if (mAnimation != null) {
            mAnimation.addUpdateListener(mAnimUpdateListener);
            mAnimation.addListener(mAnimListenerAdapter);
            mAnimation.start();
        }
    }

    public void stop() {
        if (mAnimation != null) {
            mAnimation.removeAllUpdateListeners();
            mAnimation.removeAllListeners();
            mAnimation.cancel();
        }
        mIsRunning = false;
    }
}
