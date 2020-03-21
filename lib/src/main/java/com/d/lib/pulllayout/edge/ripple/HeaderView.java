package com.d.lib.pulllayout.edge.ripple;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.d.lib.pulllayout.R;
import com.d.lib.pulllayout.edge.EdgeView;

public class HeaderView extends EdgeView {

    private RippleView mRippleView;

    public HeaderView(Context context) {
        super(context);
        init(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lib_pull_edge_ripple;
    }

    @Override
    protected void init(@NonNull final Context context) {
        super.init(context);
        setGravity(Gravity.BOTTOM);
        mContainer.setGravity(Gravity.BOTTOM);
        mRippleView = (RippleView) findViewById(R.id.rv_ripple);
    }

    @Override
    public void setState(int state) {
        if (mState == state) {
            return;
        }
        mRippleView.setState(state);
        switch (state) {
            case STATE_NONE:
                break;

            case STATE_EXPANDED:
                break;

            case STATE_LOADING:
                anim(mMeasuredHeight, null);
                break;

            case STATE_SUCCESS:
                reset();
                break;

            case STATE_ERROR:
                reset();
                break;
        }
        mState = state;
    }
}