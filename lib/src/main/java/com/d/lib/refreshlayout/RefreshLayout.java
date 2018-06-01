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

    private int width;
    private int height;

    private int touchSlop;
    private int duration = 250;
    private float dampFactor = 0.6f;//滑动阻尼系数
    private float dX, dY;//TouchEvent_ACTION_DOWN坐标(dX,dY)
    private float lastX, lastY;//TouchEvent最后一次坐标(lastX,lastY)
    private boolean isEventValid = true;//本次touch事件是否有效
    private boolean isMoveValidX, isMoveValidY;
    private boolean[] cancle = new boolean[2];
    private boolean isRunning;
    private ValueAnimator animation;
    private AnimUpdateListener animUpdateListener;
    private AnimListenerAdapter animListenerAdapter;
    private float factor;//进度因子:0-1
    private int curX, curY, dst;
    private boolean enable;
    private int gravity;

    static class AnimListenerAdapter extends AnimatorListenerAdapter {
        private final WeakReference<RefreshLayout> reference;

        AnimListenerAdapter(RefreshLayout view) {
            this.reference = new WeakReference<RefreshLayout>(view);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (isFinish()) {
                return;
            }
            RefreshLayout view = reference.get();
            view.factor = 1;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (isFinish()) {
                return;
            }
            RefreshLayout view = reference.get();
            view.factor = 1;
        }

        private boolean isFinish() {
            RefreshLayout view = reference.get();
            if (view == null || view.getContext() == null
                    || view.getContext() instanceof Activity && ((Activity) view.getContext()).isFinishing()
                    || !view.isRunning) {
                return true;
            }
            return false;
        }
    }

    static class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final WeakReference<RefreshLayout> reference;

        AnimUpdateListener(RefreshLayout view) {
            this.reference = new WeakReference<RefreshLayout>(view);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (isFinish()) {
                return;
            }
            RefreshLayout view = reference.get();
            view.factor = (float) animation.getAnimatedValue();//更新进度因子
            if (view.dst == -1) {
                float scrollY = view.curY - view.curY * view.factor;
                view.scrollTo(0, (int) scrollY);
            } else if (view.dst == 1) {
                float scrollX = view.curX - view.curX * view.factor;
                view.scrollTo((int) scrollX, 0);
            }
            view.postInvalidate();
        }

        private boolean isFinish() {
            RefreshLayout view = reference.get();
            if (view == null || view.getContext() == null
                    || view.getContext() instanceof Activity && ((Activity) view.getContext()).isFinishing()
                    || !view.isRunning) {
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
        enable = typedArray.getBoolean(R.styleable.lib_refresh_RefreshLayout_lib_refresh_refreshlayout_enable, true);
        gravity = typedArray.getInt(R.styleable.lib_refresh_RefreshLayout_lib_refresh_refreshlayout_gravity, 0x11);
        typedArray.recycle();
    }

    private void init(Context context) {
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(duration);
        animation.setInterpolator(new DecelerateInterpolator());
        animUpdateListener = new AnimUpdateListener(this);
        animListenerAdapter = new AnimListenerAdapter(this);
        animation.addUpdateListener(animUpdateListener);
        animation.addListener(animListenerAdapter);
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
        if (!enable) {
            return super.dispatchTouchEvent(ev);
        }
        final float eX = ev.getX();
        final float eY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stop();
                lastX = dX = eX;
                lastY = dY = eY;
                isMoveValidX = false;
                isMoveValidY = false;
                isEventValid = true;
                cancle[0] = cancle[1] = false;
                super.dispatchTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!isEventValid) {
                    return false;
                }
                int offsetX = (int) (lastX - eX);
                int offsetY = (int) (lastY - eY);
                boolean scrollVertically = offsetY > 0;
                boolean scrollHorizontally = offsetX > 0;
                boolean canScrollTop = isAbsList() ? ViewCompat.canScrollVertically(getChildAt(0), -1)
                        : getScrollY() + offsetY >= 0;
                boolean canScrollBottom = isAbsList() ? ViewCompat.canScrollVertically(getChildAt(0), 1)
                        : getScrollY() + offsetY <= 0;
                boolean canScrollLeft = isAbsList() ? ViewCompat.canScrollHorizontally(getChildAt(0), -1)
                        : getScrollX() + offsetX >= 0;
                boolean canScrollRight = isAbsList() ? ViewCompat.canScrollHorizontally(getChildAt(0), 1)
                        : getScrollX() + offsetX <= 0;
                lastX = eX;
                lastY = eY;
                if (!(isMoveValidX || isMoveValidY)) {
                    if (Math.abs(eX - dX) > touchSlop && Math.abs(eX - dX) > Math.abs(eY - dY)) {
                        isMoveValidX = true;
                    } else if (Math.abs(eY - dY) > touchSlop && Math.abs(eY - dY) > Math.abs(eX - dX)) {
                        isMoveValidY = true;
                    }
                }
                if (isMoveValidX) {
                    if (getScrollY() != 0) {
                        scrollTo(0, 0);
                    }
                    if (!isGravityEnable(GRAVITY_LEFT) && (!canScrollLeft || getScrollX() < 0)) {
                        super.dispatchTouchEvent(ev);
                        scrollBy(0, 0);
                    } else if (!isGravityEnable(GRAVITY_RIGHT) && (!canScrollRight) || getScrollX() > 0) {
                        super.dispatchTouchEvent(ev);
                        scrollBy(0, 0);
                    } else {
                        if (!scrollHorizontally && (!canScrollLeft || getScrollX() > 0)
                                || scrollHorizontally && (!canScrollRight || getScrollX() < 0)) {
                            if (!cancle[0]) {
                                cancle[0] = true;
                                ev.setAction(MotionEvent.ACTION_CANCEL);
                                super.dispatchTouchEvent(ev);
                            }
                            scrollBy((int) (offsetX * dampFactor), 0);
                        } else {
                            if (cancle[0]) {
                                cancle[0] = false;
                                dX = eX;
                                dY = eY;
                                ev.setAction(MotionEvent.ACTION_DOWN);
                            }
                            super.dispatchTouchEvent(ev);
                        }
                    }
                } else if (isMoveValidY) {
                    if (getScrollX() != 0) {
                        scrollTo(0, 0);
                    }
                    if (!isGravityEnable(GRAVITY_TOP) && (!canScrollTop || getScrollY() < 0)) {
                        super.dispatchTouchEvent(ev);
                        scrollBy(0, 0);
                    } else if (!isGravityEnable(GRAVITY_BOTTOM) && (!canScrollBottom || getScrollY() > 0)) {
                        super.dispatchTouchEvent(ev);
                        scrollBy(0, 0);
                    } else {
                        if (!scrollVertically && (!canScrollTop || getScrollY() > 0)
                                || scrollVertically && (!canScrollBottom || getScrollY() < 0)) {
                            if (!cancle[0]) {
                                cancle[0] = true;
                                cancle[1] = false;
                                ev.setAction(MotionEvent.ACTION_CANCEL);
                                super.dispatchTouchEvent(ev);
                            }
                            scrollBy(0, (int) (offsetY * dampFactor));
                        } else {
                            if (cancle[0]) {
                                cancle[0] = false;
                                cancle[1] = true;
                                ev.setAction(MotionEvent.ACTION_DOWN);
                            }
                            super.dispatchTouchEvent(ev);
                        }
                    }
                } else {
                    super.dispatchTouchEvent(ev);
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isEventValid) {
                    return false;
                }
                if (cancle[1]) {
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                super.dispatchTouchEvent(ev);
                if (isMoveValidX) {
                    dealUp(true);
                    isMoveValidX = false;
                    return true;
                } else if (isMoveValidY) {
                    dealUp(false);
                    isMoveValidY = false;
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void dealUp(boolean horizontal) {
        curX = getScrollX();
        curY = getScrollY();
        dst = horizontal ? 1 : -1;
        start();
    }

    private boolean isGravityEnable(int g) {
        switch (g) {
            case GRAVITY_TOP:
                return (gravity & 0x1) != 0;
            case GRAVITY_BOTTOM:
                return (gravity & 0x2) != 0;
            case GRAVITY_LEFT:
                return (gravity & 0x4) != 0;
            case GRAVITY_RIGHT:
                return (gravity & 0x8) != 0;
            default:
                return false;
        }
    }

    private boolean isAbsList() {
        View view = getChildAt(0);
        return view instanceof AbsListView || view instanceof RecyclerView;
    }

    public void start() {
        stop();
        isRunning = true;
        if (animation != null) {
            animation.start();
        }
    }

    public void stop() {
        isRunning = false;
        if (animation != null) {
            animation.end();
        }
    }
}
