package com.d.pulllayout.list.adapter.rv;

import android.content.Context;
import android.view.View;

import com.d.lib.common.util.ToastUtils;
import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;
import com.d.pulllayout.R;
import com.d.pulllayout.list.model.Bean;

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
    public void convert(final int position, CommonHolder holder, Bean item) {
        holder.setText(R.id.tv_item, item.content);
        holder.setOnClickListener(R.id.tv_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast(mContext, "Click at: " + position);
            }
        });
    }
}
