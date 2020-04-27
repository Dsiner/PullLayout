package com.d.lib.pulllayout.edge.ripple;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.d.lib.pulllayout.R;
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
        return R.layout.lib_pull_edge_ripple;
    }

    @Override
    protected void init(@NonNull final Context context) {
        super.init(context);
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
        mState = state;
        mRippleView.setState(state);
        return true;
    }
}