package com.d.pulllayout.rv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;
import com.d.pulllayout.R;
import com.d.pulllayout.rv.activity.ItemTouchActivity;
import com.d.pulllayout.rv.activity.MultipleActivity;
import com.d.pulllayout.rv.activity.SimpleActivity;
import com.d.pulllayout.rv.model.Bean;

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
        holder.setVisibility(R.id.flyt_block, View.GONE);
        holder.setText(R.id.tv_des, item.mark);
        holder.itemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (item.type) {
                    case Bean.TYPE_SIMPLE:
                        mContext.startActivity(new Intent(mContext, SimpleActivity.class));
                        break;
                    case Bean.TYPE_MULTIPLE:
                        mContext.startActivity(new Intent(mContext, MultipleActivity.class));
                        break;
                    case Bean.TYPE_ITEM_TOUCH:
                        mContext.startActivity(new Intent(mContext, ItemTouchActivity.class));
                        break;
                }
            }
        });
    }
}
