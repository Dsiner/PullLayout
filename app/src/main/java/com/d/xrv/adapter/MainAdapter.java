package com.d.xrv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.xrv.R;
import com.d.xrv.activity.ItemTouchActivity;
import com.d.xrv.activity.MultipleLrvActivity;
import com.d.xrv.activity.MultipleXrvActivity;
import com.d.xrv.activity.SimpleLrvActivity;
import com.d.xrv.activity.SimpleXrvActivity;
import com.d.xrv.model.Bean;

import java.util.List;

/**
 * MainAdapter
 * Created by D on 2017/4/26.
 */
public class MainAdapter extends CommonAdapter<Bean> {

    public MainAdapter(Context context, List<Bean> datas, int layoutId) {
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
                        clz = SimpleLrvActivity.class;
                        break;
                    case 1:
                        clz = MultipleLrvActivity.class;
                        break;
                    case 2:
                        clz = SimpleXrvActivity.class;
                        break;
                    case 3:
                        clz = MultipleXrvActivity.class;
                        break;
                    case 4:
                        clz = ItemTouchActivity.class;
                        break;
                }
                mContext.startActivity(new Intent(mContext, clz));
            }
        });
    }
}
