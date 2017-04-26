package com.example.xrv.adapter;

/**
 * Created by hehuajia on 15/4/7.
 */
public interface MultiItemTypeSupport<T> {
    public int getLayoutId(int viewType);

    public int getItemViewType(int position, T t);
}
