package com.d.refreshlayout.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.refreshlayout.R;

import java.util.List;

/**
 * RecyclerAdapter
 * Created by D on 2018/5/31.
 */
public class RecyclerAdapter extends CommonAdapter<String> {

    public RecyclerAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final int position, CommonHolder holder, String item) {
        holder.setText(R.id.tv_item, "" + position);
        holder.setViewOnClickListener(R.id.tv_item, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Click at: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
