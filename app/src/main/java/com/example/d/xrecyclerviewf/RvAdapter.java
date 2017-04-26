package com.example.d.xrecyclerviewf;

import android.content.Context;

import com.example.d.xrecyclerviewf.model.MultiBean;
import com.example.xrv.adapter.CommonAdapter;
import com.example.xrv.adapter.CommonHolder;
import com.example.xrv.adapter.MultiItemTypeSupport;

import java.util.List;

/**
 * Created by Shenyulei on 2017/2/9.
 */

public class RvAdapter extends CommonAdapter<MultiBean> {

    public RvAdapter(Context context, List<MultiBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    public RvAdapter(Context context, List<MultiBean> datas, MultiItemTypeSupport<MultiBean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }

    @Override
    public void convert(int position, CommonHolder holder, MultiBean item) {
        switch (holder.mLayoutId) {
            case R.layout.item_0:
                holder.setText(R.id.tv_des, "P:" + position + "_" + item.name);
                break;
            case R.layout.item_1:
                holder.setText(R.id.tv_des, "P:" + position + "_" + item.name);
                break;
            case R.layout.item_2:
                holder.setText(R.id.tv_des, "P:" + position + "_" + item.name);
                break;
            case R.layout.item_3:
                holder.setText(R.id.tv_des, "P:" + position + "_" + item.name);
                break;
        }
    }
}
