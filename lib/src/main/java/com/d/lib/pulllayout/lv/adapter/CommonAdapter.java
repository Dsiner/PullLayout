package com.d.lib.pulllayout.lv.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.d.lib.pulllayout.loader.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * CommonAdapter for ListView
 * Created by D on 2017/4/25.
 */
public abstract class CommonAdapter<T> extends BaseAdapter
        implements RecyclerAdapter<T> {
    protected Context mContext;
    @NonNull
    protected List<T> mDatas;
    protected int mLayoutId;
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public CommonAdapter(@NonNull Context context, List<T> datas, int layoutId) {
        mContext = context;
        mDatas = datas != null ? new ArrayList<>(datas) : new ArrayList<T>();
        mLayoutId = layoutId;
    }

    public CommonAdapter(@NonNull Context context, List<T> datas, MultiItemTypeSupport<T> multiItemTypeSupport) {
        mContext = context;
        mDatas = datas != null ? new ArrayList<>(datas) : new ArrayList<T>();
        mMultiItemTypeSupport = multiItemTypeSupport;
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
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public int getViewTypeCount() {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getViewTypeCount();
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            if (mDatas != null && mDatas.size() > 0) {
                return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public T getItem(int position) {
        return mDatas != null && mDatas.size() > 0 ? mDatas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommonHolder holder = getViewHolder(position, convertView, parent);
        convert(position, holder, getItem(position));
        return holder.itemView;
    }

    /**
     * @param position Position
     * @param holder   Holder
     * @param item     Data
     */
    public abstract void convert(int position, CommonHolder holder, T item);

    private CommonHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        int layoutId = mLayoutId;
        if (mMultiItemTypeSupport != null) {
            // MultiType
            if (mDatas != null && mDatas.size() > 0) {
                layoutId = mMultiItemTypeSupport.getLayoutId(getItemViewType(position));
            }
        }
        return CommonHolder.create(mContext, convertView, parent, layoutId);
    }
}
