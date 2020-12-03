package com.d.lib.pulllayout.edge.ripple;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.d.lib.pulllayout.Pullable;
import com.d.lib.pulllayout.edge.IExtendEdgeView;

public class ExtendFooterView extends FooterView implements IExtendEdgeView {
    private IExtendEdgeView.NestedExtendChildHelper mNestedExtendChildHelper;

    public ExtendFooterView(Context context) {
        super(context);
    }

    public ExtendFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExtendFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(@NonNull final Context context) {
        super.init(context);
        mNestedExtendChildHelper = new IExtendEdgeView.NestedExtendChildHelper(this);
        mNestedExtendChildHelper.setVisibleHeight(0);
    }

    @Override
    public boolean setState(int state) {
        final boolean updated = super.setState(state);
        if (!updated) {
            return false;
        }
        switch (state) {
            case STATE_LOADING:
            case STATE_NO_MORE:
                mNestedExtendChildHelper.postNestedAnim(0, getExpandedOffset());
                break;

            case STATE_SUCCESS:
                mNestedExtendChildHelper.resetDelayed(250);
                break;

            case STATE_ERROR:
                mNestedExtendChildHelper.resetDelayed(1700);
                break;
        }
        return true;
    }

    @Override
    public void setPullFactor(float factor) {
        this.mNestedExtendChildHelper.setPullFactor(factor);
    }

    @Override
    public void setDuration(int duration) {
        this.mNestedExtendChildHelper.setDuration(duration);
    }

    @Override
    public void setInterpolator(TimeInterpolator value) {
        this.mNestedExtendChildHelper.setInterpolator(value);
    }

    @Override
    public void postNestedAnim(int destX, int destY) {
        mNestedExtendChildHelper.postNestedAnim(destX, destY);
    }

    @Override
    public void dispatchPulled(float dx, float dy) {
        mNestedExtendChildHelper.dispatchPulled(dx, dy);
    }

    @Override
    public void setOnPullListener(Pullable.OnPullListener l) {
        mNestedExtendChildHelper.setOnPullListener(l);
    }
}
