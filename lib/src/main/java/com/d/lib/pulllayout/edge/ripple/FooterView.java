package com.d.lib.pulllayout.edge.ripple;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.d.lib.pulllayout.R;
import com.d.lib.pulllayout.edge.EdgeView;
import com.d.lib.pulllayout.edge.IEdgeView;

public class FooterView extends EdgeView implements View.OnClickListener {

    private RippleView mRippleView;
    private IEdgeView.OnClickListener mOnClickListener;

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
        return R.layout.lib_pull_edge_ripple;
    }

    @Override
    protected void init(@NonNull final Context context) {
        super.init(context);
        setGravity(Gravity.TOP);
        mContainer.setGravity(Gravity.TOP);
        mRippleView = (RippleView) findViewById(R.id.rv_ripple);
        mRippleView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
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

    public void setOnFooterClickListener(IEdgeView.OnClickListener listener) {
        this.mOnClickListener = listener;
    }
}
