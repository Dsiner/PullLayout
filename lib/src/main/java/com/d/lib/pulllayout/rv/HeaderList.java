package com.d.lib.pulllayout.rv;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.pulllayout.rv.adapter.WrapAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * HeaderList
 * Created by D on 2020/3/21.
 */
public class HeaderList {
    private final List<Integer> mItemViewTypes = new ArrayList<>();
    private final List<View> mItemViews = new ArrayList<>();

    public synchronized void add(@NonNull View view) {
        mItemViewTypes.add(WrapAdapter.ITEM_VIEW_TYPE_HEADER_INDEX + mItemViews.size());
        mItemViews.add(view);
    }

    public int size() {
        return mItemViews.size();
    }

    public int getItemViewType(int index) {
        return mItemViewTypes.get(index);
    }

    public boolean contains(int itemViewType) {
        return mItemViewTypes.contains(itemViewType);
    }

    @Nullable
    public View getView(int itemViewType) {
        if (!contains(itemViewType)) {
            return null;
        }
        int index = itemViewType - WrapAdapter.ITEM_VIEW_TYPE_HEADER_INDEX;
        if (index < 0 || index >= mItemViews.size()) {
            return null;
        }
        return mItemViews.get(index);
    }

    public synchronized boolean remove(View v) {
        if (!mItemViews.contains(v)) {
            return false;
        }
        int index = mItemViews.indexOf(v);
        mItemViewTypes.remove(index);
        mItemViews.remove(index);
        return true;
    }
}
