package com.d.pulllayout.list.adapter.lv;

import android.content.Context;
import android.view.View;

import com.d.lib.common.util.ToastUtils;
import com.d.lib.pulllayout.lv.adapter.CommonAdapter;
import com.d.lib.pulllayout.lv.adapter.CommonHolder;
import com.d.lib.pulllayout.lv.adapter.MultiItemTypeSupport;
import com.d.pulllayout.R;
import com.d.pulllayout.list.model.Bean;

import java.util.List;

/**
 * Multiple Type
 * Created by D on 2017/4/26.
 */
public class MultipleAdapter extends CommonAdapter<Bean> {

    public MultipleAdapter(Context context, List<Bean> datas, MultiItemTypeSupport<Bean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }

    @Override
    public void convert(final int position, CommonHolder holder, Bean item) {
        switch (holder.layoutId) {
            case R.layout.adapter_item_doc:
                holder.setText(R.id.tv_des, item.content);
                break;

            case R.layout.adapter_item_image:
                holder.setText(R.id.tv_des, item.content);
                break;

            case R.layout.adapter_item_music:
                holder.setText(R.id.tv_des, item.content);
                break;

            case R.layout.adapter_item_video:
                holder.setText(R.id.tv_des, item.content);
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast(mContext, "Click at: " + position);
            }
        });
    }
}
