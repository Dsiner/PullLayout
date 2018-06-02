package com.d.lib.refreshlayout.refresh.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.d.lib.refreshlayout.R;

/**
 * State
 * Created by D on 2018/3/30.
 */
public class State {
    protected View view;
    protected Context context;

    protected int width;
    protected int height;
    protected int color;
    protected int colorError;
    protected int colorWhite;

    public State(View view) {
        this.view = view;
        this.context = view.getContext();
        this.color = ContextCompat.getColor(context, R.color.lib_refresh_color_main);
        this.colorError = ContextCompat.getColor(context, R.color.lib_refresh_color_error);
        this.colorWhite = Color.parseColor("#ffffff");
    }

    public void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        this.width = measuredWidth;
        this.height = measuredHeight;
    }

    public void onDraw(Canvas canvas) {

    }

    public void reStart() {

    }

    public void stop() {

    }
}
