package com.d.lib.pulllayout.edge.elastic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.d.lib.pulllayout.edge.EdgeView;
import com.d.lib.pulllayout.edge.IState;

public class HeaderView extends EdgeView {
    private BezierWaveView bwv_wave;

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
        return R.layout.lib_pull_edge_elastic_header;
    }

    @Override
    protected void init(@NonNull final Context context) {
        super.init(context);
        mContainer.setGravity(Gravity.BOTTOM);
        bindView();
    }

    private void bindView() {
        bwv_wave = (BezierWaveView) findViewById(R.id.bwv_wave);
    }

    @Override
    public boolean setState(int state) {
        if (mState == state) {
            return false;
        }
        switch (state) {
            case STATE_LOADING:
                startNestedAnim(0, getExpandedOffset());
                break;

            case STATE_SUCCESS:
            case STATE_ERROR:
                postNestedAnimDelayed(0, 0, 1650, IState.STATE_NONE);
                break;
        }
        mState = state;
        return true;
    }

    @Override
    public void onPulled(float dx, float dy) {
        super.onPulled(dx, dy);
        float factor = Math.min(dy, getExpandedOffset()) / getExpandedOffset();
        bwv_wave.update(factor);
    }
}