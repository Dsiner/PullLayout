package com.d.lib.pulllayout.edge.ripple;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.pulllayout.edge.IState;
import com.d.lib.pulllayout.edge.ripple.state.Error;
import com.d.lib.pulllayout.edge.ripple.state.Loading;
import com.d.lib.pulllayout.edge.ripple.state.Nomore;
import com.d.lib.pulllayout.edge.ripple.state.None;
import com.d.lib.pulllayout.edge.ripple.state.State;
import com.d.lib.pulllayout.edge.ripple.state.Success;

/**
 * RippleView
 * Created by D on 2018/3/30.
 */
public class RippleView extends View implements IState {
    private int mWidth;
    private int mHeight;

    private int mState;
    private State mAttacher;
    private None mNone;
    private Loading mLoading;
    private Success mSuccess;
    private Error mError;
    private Nomore mNomore;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mNone = new None(this);
        mLoading = new Loading(this);
        mSuccess = new Success(this);
        mError = new Error(this);
        mNomore = new Nomore(this);
        mAttacher = mNone;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mAttacher.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mNone.setMeasuredDimension(mWidth, mHeight);
        mLoading.setMeasuredDimension(mWidth, mHeight);
        mSuccess.setMeasuredDimension(mWidth, mHeight);
        mError.setMeasuredDimension(mWidth, mHeight);
        mNomore.setMeasuredDimension(mWidth, mHeight);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean setState(int state) {
        if (mState == state) {
            return false;
        }
        mState = state;
        mAttacher.stop();
        switch (state) {
            case STATE_NONE:
                mAttacher = mNone;
                break;
            case STATE_LOADING:
                mAttacher = mLoading;
                break;
            case STATE_SUCCESS:
                mAttacher = mSuccess;
                break;
            case STATE_ERROR:
                mAttacher = mError;
                break;
            case STATE_NO_MORE:
                mAttacher = mNomore;
                break;
            default:
                mAttacher = mNone;
                break;
        }
        mAttacher.reStart();
        invalidate();
        return true;
    }

    @Override
    public int getState() {
        return mState;
    }
}
