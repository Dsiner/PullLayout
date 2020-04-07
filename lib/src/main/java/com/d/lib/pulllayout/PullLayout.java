package com.d.lib.pulllayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;

/**
 * PullLayout
 * Created by D on 2018/5/29.
 */
public class PullLayout extends ViewGroup {
    private static final int INVALID_POINTER = -1;

    private final static int GRAVITY_TOP = 0x1;
    private final static int GRAVITY_BOTTOM = 0x2;
    private final static int GRAVITY_LEFT = 0x4;
    private final static int GRAVITY_RIGHT = 0x8;

    private int mWidth;
    private int mHeight;

    private int mTouchSlop;
    private int mDuration = 250;
    private float mDampFactor = 0.6f;
    private int mPullPointerId = INVALID_POINTER;
    private int mTouchX, mTouchY;
    private int mLastTouchX, mLastTouchY;
    private final int[] mPullOffset = new int[2];
    private int mPullState = Pullable.PULL_STATE_IDLE;
    private boolean mCanPullHorizontally;
    private boolean mCanPullVertically;

    private boolean mIsRunning;
    private ValueAnimator mAnimation;
    private AnimUpdateListener mAnimUpdateListener;
    private AnimListenerAdapter mAnimListenerAdapter;
    private float mFactor;
    private int mPullX, mPullY;
    private boolean mEnable;
    private int mGravity;

    static class AnimListenerAdapter extends AnimatorListenerAdapter {
        private final WeakReference<PullLayout> reference;

        AnimListenerAdapter(PullLayout view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (isFinish()) {
                return;
            }
            PullLayout view = reference.get();
            view.mFactor = 1;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (isFinish()) {
                return;
            }
            PullLayout view = reference.get();
            view.mFactor = 1;
        }

        private boolean isFinish() {
            PullLayout view = reference.get();
            if (view == null || view.getContext() == null
                    || view.getContext() instanceof Activity && ((Activity) view.getContext()).isFinishing()
                    || !view.mIsRunning) {
                return true;
            }
            return false;
        }
    }

    static class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final WeakReference<PullLayout> reference;

        AnimUpdateListener(PullLayout view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (isFinish()) {
                return;
            }
            PullLayout view = reference.get();
            view.mFactor = (float) animation.getAnimatedValue();
            float scrollX = view.mPullX - view.mPullX * view.mFactor;
            float scrollY = view.mPullY - view.mPullY * view.mFactor;
            view.scrollTo((int) scrollX, (int) scrollY);
            view.invalidate();
        }

        private boolean isFinish() {
            PullLayout view = reference.get();
            if (view == null || view.getContext() == null
                    || view.getContext() instanceof Activity && ((Activity) view.getContext()).isFinishing()
                    || !view.mIsRunning) {
                return true;
            }
            return false;
        }
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
        mEnable = typedArray.getBoolean(R.styleable.lib_pull_PullLayout_lib_pull_pulllayout_enable, true);
        mGravity = typedArray.getInt(R.styleable.lib_pull_PullLayout_lib_pull_pulllayout_gravity, 0x11);
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

        final boolean[] canNestedScrollHorizontally = canNestedScrollHorizontally();
        final boolean[] canNestedScrollVertically = canNestedScrollVertically();

        final int action = ev.getActionMasked();
        final int actionIndex = ev.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                stop();
                mPullPointerId = ev.getPointerId(0);
                mLastTouchX = mTouchX = (int) (ev.getX() + 0.5f);
                mLastTouchY = mTouchY = (int) (ev.getY() + 0.5f);
                mPullOffset[0] = 0;
                mPullOffset[1] = 0;
                mCanPullHorizontally = false;
                mCanPullVertically = false;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if (mPullState == Pullable.PULL_STATE_DRAGGING) {
                    mPullPointerId = ev.getPointerId(actionIndex);
                    mLastTouchX = mTouchX = (int) (ev.getX(actionIndex) + 0.5f);
                    mLastTouchY = mTouchY = (int) (ev.getY(actionIndex) + 0.5f);
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                final int index = ev.findPointerIndex(mPullPointerId);
                if (index < 0) {
                    Log.e("E", "Error processing scroll; pointer index for id "
                            + mPullPointerId + " not found. Did any MotionEvents get skipped?");
                    return super.dispatchTouchEvent(ev);
                }
                final int x = (int) (ev.getX(index) + 0.5f);
                final int y = (int) (ev.getY(index) + 0.5f);

                if (mPullState != Pullable.PULL_STATE_DRAGGING) {
                    final int dx = x - mTouchX;
                    final int dy = y - mTouchY;
                    boolean startScroll = false;
                    if (Math.abs(dx) > mTouchSlop && Math.abs(dx) > Math.abs(dy)) {
                        if (!canNestedScrollHorizontally[0] && mLastTouchX - x < 0
                                || !canNestedScrollHorizontally[1] && mLastTouchX - x > 0) {
                            mLastTouchX = mTouchX = x;
                            mLastTouchY = mTouchY = y;
                            mCanPullHorizontally = true;
                            mCanPullVertically = false;
                            startScroll = true;
                        }
                    } else if (Math.abs(dy) > mTouchSlop && Math.abs(dy) > Math.abs(dx)) {
                        if (!canNestedScrollVertically[0] && mLastTouchY - y < 0
                                || !canNestedScrollVertically[1] && mLastTouchY - y > 0) {
                            mLastTouchX = mTouchX = x;
                            mLastTouchY = mTouchY = y;
                            mCanPullHorizontally = false;
                            mCanPullVertically = true;
                            startScroll = true;
                        }
                    }
                    if (startScroll) {
                        setPullState(Pullable.PULL_STATE_DRAGGING);
                    }
                }
                final int dx = mLastTouchX - x;
                final int dy = mLastTouchY - y;
                Log.d("dsiner", "dispatchTouchEvent dy: " + dy + " "
                        + (mPullState == Pullable.PULL_STATE_DRAGGING));
                if (mPullState == Pullable.PULL_STATE_DRAGGING) {
                    if (mCanPullHorizontally) {
                        int offsetOld = mPullOffset[0];
                        int offset = offsetOld + dx;
                        offset = offsetOld > 0 ? Math.max(0, offset)
                                : offsetOld < 0 ? Math.min(0, offset) : offset;
                        mPullOffset[0] = offset;
                        float delta = offset * mDampFactor;
                        delta = delta > 0 ? delta + 0.5f
                                : delta < 0 ? delta - 0.5f : delta;
                        scrollTo((int) delta, 0);
                        if (offset == 0 && offset != offsetOld) {
                            setPullState(Pullable.PULL_STATE_IDLE);
                        }
                    } else if (mCanPullVertically) {
                        int offsetOld = mPullOffset[1];
                        int offset = offsetOld + dy;
                        offset = offsetOld > 0 ? Math.max(0, offset)
                                : offsetOld < 0 ? Math.min(0, offset) : offset;
                        mPullOffset[1] = offset;
                        float delta = offset * mDampFactor;
                        delta = delta > 0 ? delta + 0.5f
                                : delta < 0 ? delta - 0.5f : delta;
                        scrollTo(0, (int) delta);
                        if (offset == 0 && offset != offsetOld) {
                            setPullState(Pullable.PULL_STATE_IDLE);
                        }
                    }
                }
                mLastTouchX = x;
                mLastTouchY = y;
                if (mPullState == Pullable.PULL_STATE_DRAGGING) {
                    return true;
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (mPullState == Pullable.PULL_STATE_DRAGGING) {
                    onPointerUp(ev);
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mPullState == Pullable.PULL_STATE_DRAGGING) {
                    setPullState(Pullable.PULL_STATE_IDLE);
                }
                if (mCanPullHorizontally || mCanPullVertically) {
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                dispatchUp();
                mPullOffset[0] = 0;
                mPullOffset[1] = 0;
                mCanPullHorizontally = false;
                mCanPullVertically = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void onPointerUp(MotionEvent e) {
        final int actionIndex = e.getActionIndex();
        if (e.getPointerId(actionIndex) == mPullPointerId) {
            // Pick a new pointer to pick up the slack.
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mPullPointerId = e.getPointerId(newIndex);
            mLastTouchX = mTouchX = (int) (e.getX(newIndex) + 0.5f);
            mLastTouchY = mTouchY = (int) (e.getY(newIndex) + 0.5f);
        }
    }

    private void dispatchUp() {
        mPullX = getScrollX();
        mPullY = getScrollY();
        start();
    }

    private void setPullState(int state) {
        mPullState = state;
    }

    private boolean[] canNestedScrollVertically() {
        final boolean canScrollTop = (isAbsList() || isScroller()) && ViewCompat.canScrollVertically(getChildAt(0), -1);
        final boolean canScrollBottom = (isAbsList() || isScroller()) && ViewCompat.canScrollVertically(getChildAt(0), 1);
        return new boolean[]{canScrollTop, canScrollBottom};
    }

    private boolean[] canNestedScrollHorizontally() {
        final boolean canScrollLeft = (isAbsList() || isScroller()) && ViewCompat.canScrollHorizontally(getChildAt(0), -1);
        final boolean canScrollRight = (isAbsList() || isScroller()) && ViewCompat.canScrollHorizontally(getChildAt(0), 1);
        return new boolean[]{canScrollLeft, canScrollRight};
    }

    private boolean isGravityEnable(final int g) {
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
        return view instanceof AbsListView || view instanceof RecyclerView;
    }

    private boolean isScroller() {
        View view = getChildAt(0);
        return view instanceof ScrollView;
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
