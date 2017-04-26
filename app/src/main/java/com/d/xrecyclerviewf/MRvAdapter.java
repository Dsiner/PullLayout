package com.d.xrecyclerviewf;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.d.xrecyclerviewf.activity.MultiLRvActivity;
import com.d.xrecyclerviewf.activity.MultiXRvActivity;
import com.d.xrecyclerviewf.activity.SimLRvActivity;
import com.d.xrecyclerviewf.activity.SimXRvActivity;
import com.d.xrecyclerviewf.model.Bean;
import com.d.xrv.adapter.CommonAdapter;
import com.d.xrv.adapter.CommonHolder;

import java.util.List;

/**
 * simple type
 * Created by D on 2017/4/26.
 */

class MRvAdapter extends CommonAdapter<Bean> {
    MRvAdapter(Context context, List<Bean> datas, int layoutId) {
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
                        clz = SimLRvActivity.class;
                        break;
                    case 1:
                        clz = MultiLRvActivity.class;
                        break;
                    case 2:
                        clz = SimXRvActivity.class;
                        break;
                    case 3:
                        clz = MultiXRvActivity.class;
                        break;
                }
                mContext.startActivity(new Intent(mContext, clz));
            }
        });
    }
}
