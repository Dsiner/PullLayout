package com.d.lib.pulllayout.edge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class EdgeView extends LinearLayout
        implements IEdgeView {

    protected LinearLayout mContainer;
    protected int mMeasuredHeight;
    protected int mState = STATE_NONE;

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
    public int getExpandedOffset() {
        return mMeasuredHeight;
    }

    protected abstract int getLayoutId();
}