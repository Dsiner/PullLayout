package com.d.lib.pulllayout.rv.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.pulllayout.loader.RecyclerAdapter;
import com.d.lib.pulllayout.rv.itemtouchhelper.ItemTouchHelperAdapter;
import com.d.lib.pulllayout.rv.itemtouchhelper.OnStartDragListener;

import java.util.ArrayList;
import java.util.List;

/**
 * CommonAdapter for RecyclerView
 * Created by D on 2017/4/25.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonHolder>
        implements RecyclerAdapter<T>, ItemTouchHelperAdapter {
    protected Context mContext;
    @NonNull
    protected List<T> mDatas;
    protected int mLayoutId;
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;
    protected OnStartDragListener mStartDragListener;

    public CommonAdapter(@NonNull Context context, List<T> datas, int layoutId) {
        mContext = context;
        mDatas = datas == null ? new ArrayList<T>() : datas;
        mLayoutId = layoutId;
    }

    public CommonAdapter(@NonNull Context context, List<T> datas, MultiItemTypeSupport<T> multiItemTypeSupport) {
        mContext = context;
        mDatas = datas == null ? new ArrayList<T>() : new ArrayList<>(datas);
        this.mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public void setDatas(List<T> datas) {
        if (mDatas != null && datas != null) {
            mDatas.clear();
            mDatas.addAll(datas);
        }
    }

    @Override
    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getItemViewType(position, position < mDatas.size() ? mDatas.get(position) : null);
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public CommonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = mLayoutId;
        if (mMultiItemTypeSupport != null) {
            // MultiType
            if (mDatas != null && mDatas.size() > 0) {
                layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
            }
        }
        CommonHolder holder = CommonHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonHolder holder, int position) {
        convert(position, holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void onViewHolderCreated(CommonHolder holder, View itemView) {
    }

    /**
     * @param position The position of the item within the adapter's data set.
     * @param holder   Holder
     * @param item     Data
     */
    public abstract void convert(int position, CommonHolder holder, T item);

    /**
     * 3-1: Just for ItemTouch (optional)
     */
    public void setOnStartDragListener(OnStartDragListener startDragListener) {
        this.mStartDragListener = startDragListener;
    }

    /**
     * 3-2: Just for ItemTouch (optional)
     */
    @Override
    public void onItemDismiss(int position) {
    }

    /**
     * 3-3: Just for ItemTouch (optional)
     */
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }
}
