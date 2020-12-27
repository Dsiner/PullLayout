package com.d.lib.pulllayout.edge.ripple;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.d.lib.pulllayout.edge.EdgeView;

public class HeaderView extends EdgeView {

    private RippleView mRippleView;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lib_pull_edge_ripple_header;
    }

    @Override
    protected void init(@NonNull final Context context) {
        super.init(context);
        mContainer.setGravity(Gravity.BOTTOM);
        bindView();
    }

    private void bindView() {
        mRippleView = (RippleView) findViewById(R.id.rv_ripple);
    }

    @Override
    public boolean setState(int state) {
        if (mState == state) {
            return false;
        }
        mRippleView.setState(state);
        switch (state) {
            case STATE_LOADING:
                startNestedAnim(getStartX(), getStartY(), 0, getExpandedOffset());
                break;

            case STATE_SUCCESS:
            case STATE_ERROR:
                postNestedAnimDelayed(getStartX(), getStartY(), 0, 0, 1650);
                break;
        }
        mState = state;
        return true;
    }
}