package com.d.lib.pulllayout.edge;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.d.lib.pulllayout.Pullable;
import com.d.lib.pulllayout.util.NestedAnimHelper;
import com.d.lib.pulllayout.util.Utils;

public abstract class EdgeView extends LinearLayout implements IEdgeView {

    protected LinearLayout mContainer;
    protected int mMeasuredHeight;
    protected int mState = STATE_NONE;
    protected NestedAnimHelper mNestedAnimHelper;
    protected NestedExtendChildHelper mNestedExtendChildHelper;
    protected IEdgeView.OnNestedAnimListener mOnNestedAnimListener;
    protected OnClickListener mOnFooterClickListener;

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
        mNestedAnimHelper = new NestedAnimHelper(this);
        mNestedExtendChildHelper = new NestedExtendChildHelper(this);
    }

    protected abstract int getLayoutId();

    protected int getStartX() {
        return mOnNestedAnimListener != null ? mOnNestedAnimListener.getStartX() : 0;
    }

    protected int getStartY() {
        return mOnNestedAnimListener != null ? mOnNestedAnimListener.getStartY() : 0;
    }

    @Override
    public int getState() {
        return mState;
    }

    @Override
    public int getVisibleHeight() {
        return Utils.getVisibleHeight(mContainer);
    }

    @Override
    public void setVisibleHeight(int height) {
        Utils.setVisibleHeight(mContainer, Math.max(0, height));
    }

    @Override
    public int getExpandedOffset() {
        return mMeasuredHeight;
    }

    @Override
    public void setPullFactor(float factor) {
        this.mNestedExtendChildHelper.setPullFactor(factor);
    }

    @Override
    public void dispatchPulled(int hresult, int vresult) {
        mNestedExtendChildHelper.dispatchPulled(hresult, vresult);
    }

    @Override
    public void setDuration(int duration) {
        mNestedAnimHelper.setDuration(duration);
    }

    @Override
    public void setInterpolator(TimeInterpolator value) {
        mNestedAnimHelper.setInterpolator(value);
    }

    @Override
    public void startNestedAnim(int startX, int startY, int destX, int destY) {
        mNestedAnimHelper.startNestedAnim(startX, startY, destX, destY);
    }

    public void postNestedAnimDelayed(int startX, int startY, int destX, int destY, long delayMillis) {
        mNestedAnimHelper.postNestedAnimDelayed(startX, startY, destX, destY, delayMillis);
    }

    @Override
    public boolean stopNestedAnim() {
        return mNestedAnimHelper.stopNestedAnim();
    }

    @Override
    public void onPulled(float dx, float dy) {
        Log.d("EdgeView", "onPulled: " + dy);
        if (mState == STATE_LOADING || mState == STATE_NO_MORE) {
            return;
        }
        if (Math.abs(dy) > getExpandedOffset()) {
            setState(STATE_EXPANDED);
        } else {
            setState(STATE_NONE);
        }
    }

    @Override
    public void setOnNestedAnimListener(IEdgeView.OnNestedAnimListener l) {
        mOnNestedAnimListener = l;
        mNestedAnimHelper.setOnNestedAnimListener(l);
    }

    @Override
    public void setOnFooterClickListener(OnClickListener l) {
        this.mOnFooterClickListener = l;
    }

    public static class NestedExtendChildHelper implements INestedExtend {
        private final EdgeView mEdgeView;
        private float mPullFactor = Pullable.PULL_FACTOR;

        public NestedExtendChildHelper(EdgeView view) {
            mEdgeView = view;
        }

        @Override
        public void setPullFactor(float factor) {
            mPullFactor = factor;
        }

        @Override
        public void dispatchPulled(int hresult, int vresult) {
            Log.d("EdgeView", "dispatchPulled: " + vresult);
            vresult = (int) (vresult * mPullFactor);
            if (mEdgeView.getVisibleHeight() > 0 || vresult > 0) {
                int height = Math.max(0, (int) vresult + mEdgeView.getVisibleHeight());
                mEdgeView.setVisibleHeight(height);
                Log.d("EdgeView", "getVisibleHeight: " + mEdgeView.getVisibleHeight());
            }
        }
    }
}