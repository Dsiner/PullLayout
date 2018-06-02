package com.d.lib.refreshlayout.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.refreshlayout.refresh.state.Error;
import com.d.lib.refreshlayout.refresh.state.Loading;
import com.d.lib.refreshlayout.refresh.state.Nomore;
import com.d.lib.refreshlayout.refresh.state.None;
import com.d.lib.refreshlayout.refresh.state.State;
import com.d.lib.refreshlayout.refresh.state.Success;

/**
 * HeaderView
 * Created by D on 2018/3/30.
 */
public class FreshView extends View implements IFresh {
    private int width;
    private int height;

    private int state;
    private State attacher;
    private None none;
    private Loading loading;
    private Success success;
    private Error error;
    private Nomore nomore;

    public FreshView(Context context) {
        this(context, null);
    }

    public FreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        none = new None(this);
        loading = new Loading(this);
        success = new Success(this);
        error = new Error(this);
        nomore = new Nomore(this);
        attacher = none;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        attacher.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        none.setMeasuredDimension(width, height);
        loading.setMeasuredDimension(width, height);
        success.setMeasuredDimension(width, height);
        error.setMeasuredDimension(width, height);
        nomore.setMeasuredDimension(width, height);
        setMeasuredDimension(width, height);
    }

    @Override
    public void setState(int state) {
        this.state = state;
        attacher.stop();
        switch (state) {
            case STATE_DONE:
                attacher = none;
                break;
            case STATE_LOADING:
                attacher = loading;
                break;
            case STATE_SUCCESS:
                attacher = success;
                break;
            case STATE_ERROR:
                attacher = error;
                break;
            case STATE_NOMORE:
                attacher = nomore;
                break;
            default:
                attacher = none;
                break;
        }
        attacher.reStart();
        invalidate();
    }
}
