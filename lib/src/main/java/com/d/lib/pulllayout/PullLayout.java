package com.d.lib.pulllayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.d.lib.pulllayout.util.AppBarHelper;
import com.d.lib.pulllayout.util.NestedScrollHelper;

import java.lang.ref.WeakReference;
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
    protected int mDuration = 250;
    protected int mOrientation = INVALID_ORIENTATION;
    protected int mPullPointerId = INVALID_POINTER;
    protected int mTouchX, mTouchY;
    protected int mLastTouchX, mLastTouchY;
    protected final NestedScrollHelper.Offset mPullOffsetX = new NestedScrollHelper.Offset();
    protected final NestedScrollHelper.Offset mPullOffsetY = new NestedScrollHelper.Offset();
    protected int mPullState = Pullable.PULL_STATE_IDLE;
    protected AppBarHelper mAppBarHelper;

    protected ValueAnimator mAnimation;
    protected AnimUpdateListener mAnimUpdateListener;
    protected AnimListenerAdapter mAnimListenerAdapter;

    protected boolean mEnable;
    protected int mGravity;
    protected boolean mCanPullDown = true;
    protected boolean mCanPullUp = true;
    protected List<Pullable.OnPullListener> mOnPullListeners;

    static class AnimListenerAdapter extends AnimatorListenerAdapter {
        private final WeakReference<PullLayout> reference;

        AnimListenerAdapter(PullLayout view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (isFinish(reference)) {
                return;
            }
            PullLayout view = reference.get();
            view.mAnimUpdateListener.factor = 1;
            view.setPullState(Pullable.PULL_STATE_IDLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (isFinish(reference)) {
                return;
            }
            PullLayout view = reference.get();
            view.mAnimUpdateListener.factor = 1;
            view.setPullState(Pullable.PULL_STATE_IDLE);
        }
    }

    static class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final WeakReference<PullLayout> reference;
        private int x, y, destX, destY;
        private float factor;

        AnimUpdateListener(PullLayout view) {
            this.reference = new WeakReference<>(view);
        }

        void ofInt(int x, int y, int destX, int destY) {
            this.x = x;
            this.y = y;
            this.destX = destX;
            this.destY = destY;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (isFinish(reference)) {
                return;
            }
            PullLayout view = reference.get();
            factor = (float) animation.getAnimatedValue();
            int scrollX = (int) (x + (destX - x) * factor);
            int scrollY = (int) (y + (destY - y) * factor);
            view.scrollTo(scrollX, scrollY);
            view.invalidate();
            view.setPullState(Pullable.PULL_STATE_SETTLING);
        }
    }

    private static boolean isFinish(WeakReference<PullLayout> reference) {
        PullLayout view = reference.get();
        return view == null || view.getContext() == null
                || view.getContext() instanceof Activity
                && ((Activity) view.getContext()).isFinishing();
    }

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
        mAnimation = ValueAnimator.ofFloat(0f, 1f);
        mAnimation.setDuration(mDuration);
        mAnimation.setInterpolator(new DecelerateInterpolator());
        mAnimUpdateListener = new AnimUpdateListener(this);
        mAnimListenerAdapter = new AnimListenerAdapter(this);
        mAppBarHelper = new AppBarHelper(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (count > 0) {
            View child = getNestedChild();
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
                    setPullState(Pullable.PULL_STATE_IDLE);
                    startNestedAnim(0, 0);
                }
                mOrientation = INVALID_ORIENTATION;
                ev.setAction(MotionEvent.ACTION_CANCEL);
                super.dispatchTouchEvent(ev);
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void resetTouch() {
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
        stopNestedAnim();
        if (getScrollX() == destX && getScrollY() == destY) {
            setPullState(Pullable.PULL_STATE_IDLE);
            return;
        }
        mAnimUpdateListener.ofInt(getScrollX(), getScrollY(), destX, destY);
        mAnimation.addUpdateListener(mAnimUpdateListener);
        mAnimation.addListener(mAnimListenerAdapter);
        mAnimation.start();
    }

    protected boolean stopNestedAnim() {
        boolean running = mAnimation.isRunning();
        mAnimation.removeAllUpdateListeners();
        mAnimation.removeAllListeners();
        mAnimation.cancel();
        return running;
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
    public void addOnPullScrollListener(OnPullListener listener) {
        if (mOnPullListeners == null) {
            mOnPullListeners = new ArrayList<>();
        }
        mOnPullListeners.add(listener);
    }

    @Override
    public void removeOnPullScrollListener(OnPullListener listener) {
        if (mOnPullListeners != null) {
            mOnPullListeners.remove(listener);
        }
    }

    @Override
    public void clearOnPullScrollListeners() {
        if (mOnPullListeners != null) {
            mOnPullListeners.clear();
        }
    }
}
