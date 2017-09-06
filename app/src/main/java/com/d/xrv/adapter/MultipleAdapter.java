package com.d.xrv.adapter;

import android.content.Context;

import com.d.xrv.R;
import com.d.xrv.model.Bean;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.lib.xrv.adapter.MultiItemTypeSupport;

import java.util.List;

/**
 * Multiple Type
 * Created by D on 2017/4/26.
 */
public class MultipleAdapter extends CommonAdapter<Bean> {

    /**
     * @param context:context
     * @param datas:填充数据源
     * @param multiItemTypeSupport:多布局类型支持
     */
    public MultipleAdapter(Context context, List<Bean> datas, MultiItemTypeSupport<Bean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }

    @Override
    public void convert(int position, CommonHolder holder, Bean item) {
        //先判断mLayoutId布局类型，后通过通用holder方法赋值
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
