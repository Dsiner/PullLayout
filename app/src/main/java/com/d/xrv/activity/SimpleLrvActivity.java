package com.d.xrv.activity;

import android.app.Activity;
import android.os.Bundle;

import com.d.lib.xrv.LRecyclerView;
import com.d.xrv.R;
import com.d.xrv.adapter.SimpleAdapter;
import com.d.xrv.presenter.Factory;

/**
 * Simple Type
 * Created by D on 2017/4/26.
 */
public class SimpleLrvActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_lrv);
        init();
    }

    private void init() {
        // Step 1: 获取引用
        LRecyclerView lrvList = (LRecyclerView) this.findViewById(R.id.lrv_list);
        // Step 2: New Adapter
        SimpleAdapter adapter = new SimpleAdapter(this, Factory.createDatas(15), R.layout.item_0);
        // Step 3: Set Adapter
        lrvList.setAdapter(adapter);
    }
}
