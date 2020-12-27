package com.d.lib.pulllayout.rv;

import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemViewList
 * Created by D on 2020/3/21.
 */
public class ItemViewList {
    private final int mIndex, mCount;
    private final List<View> mItemViews = new ArrayList<>();

    public ItemViewList(int index, int count) {
        mIndex = index;
        mCount = count;
    }

    public int size() {
        return mItemViews.size();
    }

    public int getItemViewType(int index) {
        return mIndex + index;
    }

    @Nullable
    public View getView(int viewType) {
        if (!contains(viewType)) {
            return null;
        }
        int index = viewType - mIndex;
        return mItemViews.get(index);
    }

    public boolean contains(int viewType) {
        int index = viewType - mIndex;
        return index >= 0 && index < mItemViews.size();
    }

    public void add(View view) {
        if (view == null) {
            return;
        }
        if (mItemViews.size() >= mCount) {
            throw new IllegalStateException("The number of views must be less than " + mCount);
        }
        mItemViews.add(view);
    }

    public boolean remove(View view) {
        return mItemViews.remove(view);
    }
}
