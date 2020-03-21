package com.d.pulllayout.rv.adapter;

import android.content.Context;

import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;
import com.d.lib.pulllayout.rv.adapter.MultiItemTypeSupport;
import com.d.pulllayout.R;
import com.d.pulllayout.rv.model.Bean;

import java.util.List;

/**
 * Multiple Type
 * Created by D on 2017/4/26.
 */
public class MultipleAdapter extends CommonAdapter<Bean> {

    /**
     * @param context:              Context
     * @param datas:                填充数据源
     * @param multiItemTypeSupport: 多布局类型支持
     */
    public MultipleAdapter(Context context, List<Bean> datas, MultiItemTypeSupport<Bean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }

    @Override
    public void convert(int position, CommonHolder holder, Bean item) {
        // 先判断mLayoutId布局类型，后通过通用holder方法赋值
        switch (holder.mLayoutId) {
            case R.layout.adapter_item_0:
                holder.setText(R.id.tv_des, "P:" + position + "_" + item.content);
                break;
            case R.layout.adapter_item_1:
                holder.setText(R.id.tv_des, "P:" + position + "_" + item.content);
                break;
            case R.layout.adapter_item_2:
                holder.setText(R.id.tv_des, "P:" + position + "_" + item.content);
                break;
            case R.layout.adapter_item_3:
                holder.setText(R.id.tv_des, "P:" + position + "_" + item.content);
                break;
        }
    }
}
