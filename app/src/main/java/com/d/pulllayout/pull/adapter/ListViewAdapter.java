package com.d.pulllayout.pull.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.d.lib.pulllayout.lv.adapter.CommonAdapter;
import com.d.lib.pulllayout.lv.adapter.CommonHolder;
import com.d.pulllayout.R;

import java.util.List;

/**
 * ListAdapter
 * Created by D on 2018/5/31.
 */
public class ListViewAdapter extends CommonAdapter<String> {

    public ListViewAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final int position, CommonHolder holder, String item) {
        holder.setText(R.id.tv_item, "" + position);
        holder.setOnClickListener(R.id.tv_item, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Click at: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
