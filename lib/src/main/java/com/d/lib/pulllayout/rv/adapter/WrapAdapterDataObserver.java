package com.d.lib.pulllayout.rv.adapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * WrapAdapterDataObserver for RecyclerView
 * Created by D on 2017/4/25.
 */
public class WrapAdapterDataObserver extends RecyclerView.AdapterDataObserver {
    private WrapAdapter mWrapAdapter;

    public WrapAdapterDataObserver(final WrapAdapter wrapAdapter) {
        this.mWrapAdapter = wrapAdapter;
    }

    @Override
    public void onChanged() {
        if (mWrapAdapter == null) {
            return;
        }
        mWrapAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        if (mWrapAdapter == null) {
            return;
        }
        positionStart = mWrapAdapter.getWrapPosition(positionStart);
        mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        if (mWrapAdapter == null) {
            return;
        }
        positionStart = mWrapAdapter.getWrapPosition(positionStart);
        mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        if (mWrapAdapter == null) {
            return;
        }
        positionStart = mWrapAdapter.getWrapPosition(positionStart);
        mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        if (mWrapAdapter == null) {
            return;
        }
        positionStart = mWrapAdapter.getWrapPosition(positionStart);
        mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        if (mWrapAdapter == null) {
            return;
        }
        fromPosition = mWrapAdapter.getWrapPosition(fromPosition);
        toPosition = mWrapAdapter.getWrapPosition(toPosition);
        mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
    }
}