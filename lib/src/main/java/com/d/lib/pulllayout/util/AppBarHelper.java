package com.d.lib.pulllayout.util;

import android.view.View;
import android.view.ViewParent;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

public class AppBarHelper {
    private final View mView;
    private final AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
    public State mAppbarState = State.EXPANDED;

    public AppBarHelper(View view) {
        mView = view;
        mOnOffsetChangedListener = new OnStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                mAppbarState = state;
            }
        };
    }

    public boolean isExpanded() {
        return mAppbarState == State.EXPANDED;
    }

    public void setOnOffsetChangedListener() {
        AppBarLayout appBarLayout = null;
        ViewParent p = mView.getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.removeOnOffsetChangedListener(mOnOffsetChangedListener);
                appBarLayout.addOnOffsetChangedListener(mOnOffsetChangedListener);
            }
        }
    }

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    public static abstract class OnStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    mCurrentState = State.EXPANDED;
                    onStateChanged(appBarLayout, State.EXPANDED);
                }

            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    mCurrentState = State.COLLAPSED;
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }

            } else {
                if (mCurrentState != State.IDLE) {
                    mCurrentState = State.IDLE;
                    onStateChanged(appBarLayout, State.IDLE);
                }
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }
}
