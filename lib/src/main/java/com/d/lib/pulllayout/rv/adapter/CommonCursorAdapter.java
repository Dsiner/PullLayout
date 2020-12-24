package com.d.lib.pulllayout.rv.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.pulllayout.util.Utils;

/**
 * CommonCursorAdapter for RecyclerView
 * Created by D on 2018/1/25.
 */
public abstract class CommonCursorAdapter extends RecyclerView.Adapter<CommonHolder> {
    protected Context mContext;
    protected int mLayoutId;
    protected MultiItemTypeSupport<Cursor> mMultiItemTypeSupport;
    protected Cursor mCursor;

    public CommonCursorAdapter(@NonNull Context context, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
    }

    public CommonCursorAdapter(@NonNull Context context, MultiItemTypeSupport<Cursor> multiItemTypeSupport) {
        mContext = context;
        mMultiItemTypeSupport = multiItemTypeSupport;
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void setCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return;
        }
        final Cursor oldCursor = mCursor;
        mCursor = cursor;
        notifyDataSetChanged();
        Utils.closeQuietly(oldCursor);
    }

    protected boolean isDataValid(Cursor cursor) {
        return cursor != null && !cursor.isClosed();
    }

    @Override
    public int getItemCount() {
        if (isDataValid(mCursor)) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("Could not move cursor to position " + position
                        + " when trying to get item view type.");
            }
            return mMultiItemTypeSupport.getItemViewType(position, mCursor);
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public CommonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = mLayoutId;
        if (mMultiItemTypeSupport != null) {
            // MultiType
            layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        }
        CommonHolder holder = CommonHolder.create(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonHolder holder, int position) {
        if (!isDataValid(mCursor)) {
            throw new IllegalStateException("Cannot bind view holder when cursor is in invalid state.");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Could not move cursor to position " + position
                    + " when trying to bind view holder");
        }
        convert(position, holder, mCursor);
    }

    public void onViewHolderCreated(CommonHolder holder, View itemView) {
    }

    /**
     * @param position The position of the item within the adapter's data set.
     * @param holder   Holder
     * @param cursor   Cursor
     */
    public abstract void convert(int position, CommonHolder holder, Cursor cursor);
}
