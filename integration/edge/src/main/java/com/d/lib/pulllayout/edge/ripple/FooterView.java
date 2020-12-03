package com.d.lib.pulllayout.edge.ripple;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.d.lib.pulllayout.edge.EdgeView;
import com.d.lib.pulllayout.edge.R;

public class FooterView extends EdgeView implements View.OnClickListener {

    private RippleView mRippleView;

    public FooterView(Context context) {
        super(context);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lib_pull_edge_ripple_footer;
    }

    @Override
    protected void init(@NonNull final Context context) {
        super.init(context);
        mRippleView = (RippleView) findViewById(R.id.rv_ripple);
    }

    @Override
    public boolean setState(int state) {
        if (mState == state) {
            return false;
        }
        mState = state;
        mRippleView.setState(state);
        switch (state) {
            case STATE_NONE:
            case STATE_EXPANDED:
            case STATE_LOADING:
            case STATE_SUCCESS:
            case STATE_NO_MORE:
                setOnClickListener(null);
                break;

            case STATE_ERROR:
                setOnClickListener(this);
                break;
        }
        return true;
    }
}
