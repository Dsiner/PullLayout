package com.d.lib.pulllayout.edge.ripple.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.d.lib.pulllayout.R;

/**
 * State
 * Created by D on 2018/3/30.
 */
public class State {
    protected View mView;
    protected Context mContext;

    protected int mWidth;
    protected int mHeight;
    protected int mColor;
    protected int mColorError;
    protected int mColorWhite;

    public State(View view) {
        this.mView = view;
        this.mContext = view.getContext();
        this.mColor = ContextCompat.getColor(mContext, R.color.lib_pull_color_main);
        this.mColorError = ContextCompat.getColor(mContext, R.color.lib_pull_color_error);
        this.mColorWhite = Color.parseColor("#ffffff");
    }

    public void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        this.mWidth = measuredWidth;
        this.mHeight = measuredHeight;
    }

    public void onDraw(Canvas canvas) {

    }

    public void reStart() {

    }

    public void stop() {

    }
}
