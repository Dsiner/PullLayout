package com.d.refreshlayout.adapter;

import android.content.Context;

import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;

import java.util.List;

/**
 * RecyclerAdapter
 * Created by D on 2018/5/31.
 */
public class RecyclerAdapter extends CommonAdapter<String> {

    public RecyclerAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(int position, CommonHolder holder, String item) {

    }
}
