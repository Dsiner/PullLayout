package com.d.lib.pulllayout.edge.arrow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.d.lib.pulllayout.edge.IExtendEdgeView;

public class ExtendHeaderView extends HeaderView implements IExtendEdgeView {
    private IExtendEdgeView.NestedExtendChildHelper mNestedExtendChildHelper;

    public ExtendHeaderView(Context context) {
        super(context);
    }

    public ExtendHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                mNestedExtendChildHelper.onExtendTo(0, getExpandedOffset());
                break;

            case STATE_SUCCESS:
            case STATE_ERROR:
                mNestedExtendChildHelper.onReset();
                break;
        }
        return true;
    }

    @Override
    public void onDispatchPulled(float dx, float dy) {
        mNestedExtendChildHelper.dispatchPulled(dx, dy);
    }

    @Override
    public void onExtendTo(int destX, int destY) {
        mNestedExtendChildHelper.onExtendTo(destX, destY);
    }
}