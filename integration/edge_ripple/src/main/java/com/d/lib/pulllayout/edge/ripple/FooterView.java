package com.d.lib.pulllayout.edge.ripple;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.d.lib.pulllayout.edge.EdgeView;

public class FooterView extends EdgeView {

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
            nestedAnim(state);
            return false;
        }
        mRippleView.setState(state);
        nestedAnim(state);
        setOnClickListener(state == STATE_ERROR ? mOnFooterClickListener : null);
        mState = state;
        return true;
    }

    private void nestedAnim(int state) {
        switch (state) {
            case STATE_SUCCESS:
            case STATE_NO_MORE:
                startNestedAnim(getStartX(), getStartY(), 0, 0);
                break;

            case STATE_ERROR:
                postNestedAnimDelayed(getStartX(), getStartY(), 0, 0, 1700);
                break;
        }
    }
}
