package com.d.lib.pulllayout.edge.elastic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.d.lib.pulllayout.edge.EdgeView;

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
            nestedAnim(state);
            return false;
        }
        nestedAnim(state);
        mState = state;
        return true;
    }

    private void nestedAnim(int state) {
        switch (state) {
            case STATE_SUCCESS:
            case STATE_ERROR:
                postNestedAnimDelayed(getStartX(), getStartY(), 0, 0, 1650);
                break;
        }
    }

    @Override
    public void onPulled(float dx, float dy) {
        super.onPulled(dx, dy);
        float factor = Math.min(dy, getExpandedOffset()) / getExpandedOffset();
        bwv_wave.update(factor);
    }
}