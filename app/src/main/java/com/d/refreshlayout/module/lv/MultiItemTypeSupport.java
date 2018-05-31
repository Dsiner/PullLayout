package com.d.refreshlayout.module.lv;

/**
 * CommonHolder for ListView
 * Created by D on 2017/4/25.
 */
public interface MultiItemTypeSupport<T> {
    public int getLayoutId(int position, T t);

    public int getViewTypeCount();

    public int getItemViewType(int position, T t);
}
