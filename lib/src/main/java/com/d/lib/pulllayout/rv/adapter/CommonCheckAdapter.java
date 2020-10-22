package com.d.lib.pulllayout.rv.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * CommonCheckAdapter
 * Created by D on 2018/1/25.
 */
public abstract class CommonCheckAdapter<T> extends CommonAdapter<T> {
    public final static int MODE_NORMAL = 0;
    public final static int MODE_SELECT = 1;

    protected int mMode = MODE_NORMAL;
    protected final Set<Integer> mSelectedPositions;

    public CommonCheckAdapter(@NonNull Context context, List<T> datas, int layoutId) {
        super(context, datas, layoutId);
        mSelectedPositions = Collections.synchronizedSet(new TreeSet<Integer>());
    }

    public CommonCheckAdapter(@NonNull Context context, List<T> datas, MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
        mSelectedPositions = Collections.synchronizedSet(new TreeSet<Integer>());
    }

    public void setMode(int mode) {
        this.mMode = mode;
        this.notifyDataSetChanged();
    }

    public boolean isSelected(int position) {
        return mSelectedPositions.contains(position);
    }

    public void setSelected(int position) {
        mSelectedPositions.contains(position);
    }

    public void toggleSelection(int position) {
        if (position < 0) {
            return;
        }
        boolean contains = mSelectedPositions.contains(position);
        if (contains) {
            removeSelection(position);
        } else {
            addSelection(position);
        }
    }

    public final boolean addSelection(int position) {
        return mSelectedPositions.add(position);
    }

    public final boolean removeSelection(int position) {
        return mSelectedPositions.remove(position);
    }

    public int getSelectedItemCount() {
        return mSelectedPositions.size();
    }

    public List<Integer> getSelectedPositions() {
        return new ArrayList<>(mSelectedPositions);
    }

    public Set<Integer> getSelectedPositionsAsSet() {
        return mSelectedPositions;
    }

    public void selectAll() {
        for (int i = 0; i < getItemCount(); i++) {
            mSelectedPositions.add(i);
        }
    }

    public void unSelectAll() {
        for (int i = 0; i < getItemCount(); i++) {
            mSelectedPositions.remove(i);
        }
    }

    public boolean isSelectAll() {
        List<T> list = getDatas();
        for (int i = 0; i < list.size(); i++) {
            if (!isSelected(i)) {
                return false;
            }
        }
        return true;
    }

    @NonNull
    public List<T> getChecked() {
        List<T> list = new ArrayList<>();
        List<Integer> positions = getSelectedPositions();
        for (int i = 0; i < positions.size(); i++) {
            list.add(mDatas.get(positions.get(i)));
        }
        return list;
    }

    public void clearSelection() {
        synchronized (mSelectedPositions) {
            mSelectedPositions.clear();
        }
    }
}
