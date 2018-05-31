package com.d.lib.refreshlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import java.lang.ref.WeakReference;

/**
 * RefreshLayout
 * Created by D on 2018/5/29.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RefreshLayout extends ViewGroup {

    private int width;
    private int height;

    private int touchSlop;
    private int slideSlop;
    private int duration = 210;
    private float dX, dY;//TouchEvent_ACTION_DOWN坐标(dX,dY)
    private float lastY;//TouchEvent最后一次坐标(lastX,lastY)
    private boolean isEventValid = true;//本次touch事件是否有效
    private boolean isMoveValid;
    private boolean isRunning;
    private ValueAnimator animation;
    private AnimUpdateListener animUpdateListener;
    private AnimListenerAdapter animListenerAdapter;
    private float factor;//进度因子:0-1
    private int curY, dstY;

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
            float scrollY = view.curY + (view.dstY - view.curY) * view.factor;
            view.scrollTo(0, (int) scrollY);
            view.postInvalidate();//刷新
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
        init(context);
    }

    private void init(Context context) {
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        slideSlop = UIUtil.dip2px(context, 45);
        animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
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
        final float eX = ev.getX();
        final float eY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stop();
                dX = eX;
                lastY = dY = eY;
                isMoveValid = false;
                isEventValid = true;
                super.dispatchTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!isEventValid) {
                    return false;
                }
                int offset = (int) (lastY - eY);
                lastY = eY;
                if (!isMoveValid && Math.abs(eY - dY) > touchSlop && Math.abs(eY - dY) > Math.abs(eX - dX)) {
                    isMoveValid = true;
                }
                if (isMoveValid) {
                    scrollBy(0, (int) (offset * 0.4f));
                } else {
                    super.dispatchTouchEvent(ev);
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isEventValid) {
                    return false;
                }
                if (isMoveValid) {
                    dealUp(getScrollY());
                    isMoveValid = false;
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void dealUp(int scrollY) {
        curY = getScrollY();
        dstY = 0;
        start();
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
