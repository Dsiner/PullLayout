package com.d.xrv.adapter;

import android.content.Context;

import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.xrv.R;
import com.d.xrv.model.Bean;

import java.util.List;

/**
 * Simple Type
 * Created by D on 2017/4/26.
 */
public class SimpleAdapter extends CommonAdapter<Bean> {

    /**
     * @param context:  Context
     * @param datas:    填充数据源
     * @param layoutId: 单一类型布局layout
     */
    public SimpleAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(int position, CommonHolder holder, Bean item) {
        // 通过通用holder方法赋值
        holder.setText(R.id.tv_des, "P:" + position + "_" + item.content);
    }
}
