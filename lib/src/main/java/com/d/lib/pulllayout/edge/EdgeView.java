package com.d.lib.pulllayout.edge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.d.lib.pulllayout.Pullable;

public abstract class EdgeView extends LinearLayout
        implements IEdgeView {

    protected LinearLayout mContainer;
    protected int mMeasuredHeight;
    protected int mState = STATE_NONE;
    private boolean mResizable;

    public EdgeView(Context context) {
        super(context);
        init(context);
    }

    public EdgeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EdgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(@NonNull final Context context) {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
        mContainer = (LinearLayout) LayoutInflater.from(getContext())
                .inflate(getLayoutId(), this, false);
        addView(mContainer);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    @Override
    public int getState() {
        return mState;
    }

    @Override
    public void dispatchPulled(float dx, float dy) {
        Log.d("EdgeView", "dispatchPulled: " + dy);
        dy = dy / IEdgeView.DRAG_FACTOR;
        if (getVisibleHeight() > 0 || dy > 0) {
            int height = Math.max(0, (int) dy + getVisibleHeight());
            if (mState == STATE_NO_MORE) {
                height = getExpandedOffset();
            }
            setVisibleHeight(height);
            Log.d("EdgeView", "getVisibleHeight: " + getVisibleHeight());
        }
    }

    @Override
    public void onPulled(float dx, float dy) {
        Log.d("EdgeView", "onPulled: " + dy);
        if (mState == STATE_LOADING) {
            return;
        }
        if (Math.abs(dy) > getExpandedOffset()) {
            setState(STATE_EXPANDED);
        } else {
            setState(STATE_NONE);
        }
    }

    @Override
    public void onPullStateChanged(int newState) {
        Log.d("EdgeView", "onPullStateChanged: " + newState);
        if (newState != Pullable.PULL_STATE_IDLE) {
            return;
        }
        if (mState == STATE_EXPANDED) {
            setState(STATE_LOADING);
        }
        if (mState == STATE_LOADING) {
            anim(getExpandedOffset(), null);
        } else {
            anim(0, null);
        }
    }

    @Override
    public int getExpandedOffset() {
        return mMeasuredHeight;
    }

    public boolean resizable() {
        return mResizable;
    }

    public void setResizable(boolean enable) {
        mResizable = enable;
        if (enable) {
            LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
            lp.height = 0;
            mContainer.setLayoutParams(lp);
        }
    }

    protected void reset() {
        if (!resizable()) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                anim(0, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setState(STATE_NONE);
                    }
                });
            }
        }, 300);
    }

    protected void anim(final int dest, final AnimatorListenerAdapter listener) {
        if (!resizable()) {
            return;
        }
        ValueAnimator anim = ValueAnimator.ofInt(getVisibleHeight(), dest);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        if (listener != null) {
            anim.addListener(listener);
        }
        anim.start();
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    protected abstract int getLayoutId();
}