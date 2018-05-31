package com.d.refreshlayout.adapter;

import android.content.Context;

import com.d.refreshlayout.module.lv.CommonAdapter;
import com.d.refreshlayout.module.lv.CommonHolder;

import java.util.List;

/**
 * ListAdapter
 * Created by D on 2018/5/31.
 */
public class ListViewAdapter extends CommonAdapter<String> {

    public ListViewAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(int position, CommonHolder holder, String item) {

    }
}
