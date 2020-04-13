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

    public MultipleAdapter(Context context, List<Bean> datas, MultiItemTypeSupport<Bean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }

    @Override
    public void convert(int position, CommonHolder holder, Bean item) {
        switch (holder.layoutId) {
            case R.layout.adapter_item_doc:
                holder.setText(R.id.tv_des, "P: " + position + "_" + item.content);
                break;

            case R.layout.adapter_item_image:
                holder.setText(R.id.tv_des, "P: " + position + "_" + item.content);
                break;

            case R.layout.adapter_item_music:
                holder.setText(R.id.tv_des, "P: " + position + "_" + item.content);
                break;

            case R.layout.adapter_item_video:
                holder.setText(R.id.tv_des, "P: " + position + "_" + item.content);
                break;
        }
    }
}
