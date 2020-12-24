package com.d.pulllayout.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * NoScrollViewPager
 * Created by D on 2020/3/11.
 */
public class NoScrollViewPager extends ViewPager {
    private boolean mEnableScroll = false;

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isEnableScroll() {
        return mEnableScroll;
    }

    public void setEnableScroll(boolean enableScrollHorizontally) {
        this.mEnableScroll = enableScrollHorizontally;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mEnableScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mEnableScroll && super.onTouchEvent(ev);
    }
}
