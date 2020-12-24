package com.d.lib.pulllayout.loader;

import java.util.List;

/**
 * RecyclerAdapter
 * Created by D on 2020/4/25.
 */
public interface RecyclerAdapter<T> {

    List<T> getDatas();

    void setDatas(List<T> datas);

    void notifyDataSetChanged();
}
