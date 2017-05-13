package com.d.xrecyclerviewf;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.d.xrecyclerviewf.activity.MultipleLRvActivity;
import com.d.xrecyclerviewf.activity.MultipleXRvActivity;
import com.d.xrecyclerviewf.activity.SimpleLRvActivity;
import com.d.xrecyclerviewf.activity.SimpleXRvActivity;
import com.d.xrecyclerviewf.model.Bean;
import com.d.xrv.adapter.CommonAdapter;
import com.d.xrv.adapter.CommonHolder;

import java.util.List;

/**
 * Simple Type
 * Created by D on 2017/4/26.
 */

class MainAdapter extends CommonAdapter<Bean> {
    MainAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(int position, CommonHolder holder, final Bean item) {
        holder.setViewVisibility(R.id.flyt_block, View.GONE);
        holder.setText(R.id.tv_des, item.mark);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class clz;
                switch (item.index) {
                    case 0:
                    default:
                        clz = SimpleLRvActivity.class;
                        break;
                    case 1:
                        clz = MultipleLRvActivity.class;
                        break;
                    case 2:
                        clz = SimpleXRvActivity.class;
                        break;
                    case 3:
                        clz = MultipleXRvActivity.class;
                        break;
                }
                mContext.startActivity(new Intent(mContext, clz));
            }
        });
    }
}
