package com.d.lib.pulllayout.util;

import android.view.View;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ScrollView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.d.lib.pulllayout.Pullable;

public class NestedScrollHelper {
    public static final int GRAVITY_TOP = 0x0001;
    public static final int GRAVITY_BOTTOM = 0x0010;
    public static final int GRAVITY_LEFT = 0x0100;
    public static final int GRAVITY_RIGHT = 0x1000;

    public static boolean isEnable(final int gravity, final int g) {
        switch (gravity) {
            case GRAVITY_TOP:
                return (g & GRAVITY_TOP) != 0;
            case GRAVITY_BOTTOM:
                return (g & GRAVITY_BOTTOM) != 0;
            case GRAVITY_LEFT:
                return (g & GRAVITY_LEFT) != 0;
            case GRAVITY_RIGHT:
                return (g & GRAVITY_RIGHT) != 0;
            default:
                return false;
        }
    }

    public static boolean[] canNestedScrollVertically(View view) {
        final boolean canScrollTop = (isRecyclerList(view) || isScrollView(view))
                && ViewCompat.canScrollVertically(view, -1);
        final boolean canScrollBottom = (isRecyclerList(view) || isScrollView(view))
                && ViewCompat.canScrollVertically(view, 1);
        return new boolean[]{canScrollTop, canScrollBottom};
    }

    public static boolean[] canNestedScrollHorizontally(View view) {
        final boolean canScrollLeft = (isRecyclerList(view) || isScrollView(view))
                && ViewCompat.canScrollHorizontally(view, -1);
        final boolean canScrollRight = (isRecyclerList(view) || isScrollView(view))
                && ViewCompat.canScrollHorizontally(view, 1);
        return new boolean[]{canScrollLeft, canScrollRight};
    }

    public static boolean isRecyclerList(View view) {
        return view instanceof AbsListView || view instanceof RecyclerView;
    }

    public static boolean isScrollView(View view) {
        return view instanceof ScrollView;
    }

    public static boolean isOnTop(View view) {
        boolean attached = view.getParent() != null;
        int offset = view.getBottom();
        return attached && offset >= 0;
    }

    public static boolean isOnBottom(View parent, View view) {
        boolean attached = view.getParent() != null;
        int offset = view.getTop() - parent.getHeight();
        return attached && offset <= 0;
    }

    public static void scrollToPosition(RecyclerView view, int position) {
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.scrollToPosition(position);
        }
    }

    public static void scrollToPositionWithOffset(RecyclerView view, int position, int offset) {
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) layoutManager).scrollToPositionWithOffset(position, offset);
        } else if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).scrollToPositionWithOffset(position, offset);
        } else if (layoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, offset);
        }
    }

    public static void stopNestedScroll(View view) {
        if (!(view instanceof RecyclerView)) {
            return;
        }
        ViewParent p = view.getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p instanceof CoordinatorLayout) {
            ((RecyclerView) view).stopScroll();
        }
    }

    public static class Offset {
        public static final int INVALID_ORIENTATION = 0;
        public static final int POSITIVE = 1;
        public static final int NEGATIVE = -1;

        private float mPullFactor = Pullable.PULL_FACTOR;
        private int mOrientation = INVALID_ORIENTATION;
        private int mOffset;

        public void init(int scroll) {
            mOffset = (int) (scroll / mPullFactor);
            mOrientation = (scroll < 0) ? NEGATIVE
                    : ((scroll == 0) ? INVALID_ORIENTATION : POSITIVE);
        }

        public void setPullFactor(float factor) {
            this.mPullFactor = factor;
        }

        public void orientation(int orientation) {
            mOrientation = orientation;
        }

        public int onPulled(float dx) {
            mOffset += dx;
            mOffset = mOrientation == POSITIVE ? Math.max(0, mOffset)
                    : mOrientation == NEGATIVE ? Math.min(0, mOffset) : mOffset;
            return (int) (mOffset * mPullFactor);
        }
    }
}
