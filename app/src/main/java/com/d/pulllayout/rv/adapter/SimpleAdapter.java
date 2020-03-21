package com.d.pulllayout.rv.adapter;

import android.content.Context;

import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;
import com.d.pulllayout.R;
import com.d.pulllayout.rv.model.Bean;

import java.util.List;

/**
 * Simple Type
 * Created by D on 2017/4/26.
 */
public class SimpleAdapter extends CommonAdapter<Bean> {

    public SimpleAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(int position, CommonHolder holder, Bean item) {
        holder.setText(R.id.tv_des, "P:" + position + "_" + item.content);
    }
}
