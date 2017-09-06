package com.d.xrv.activity;

import android.app.Activity;
import android.os.Bundle;

import com.d.xrv.Factory;
import com.d.xrv.R;
import com.d.xrv.adapter.SimpleAdapter;
import com.d.xrv.model.Bean;
import com.d.lib.xrv.LRecyclerView;

import java.util.ArrayList;

/**
 * Simple Type
 * Created by D on 2017/4/26.
 */
public class SimpleLRvActivity extends Activity {
    private ArrayList<Bean> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_lrv);
        datas = Factory.createDatas();
        init();
    }

    private void init() {
        //step1:获取引用
        LRecyclerView recyclerView = (LRecyclerView) this.findViewById(R.id.lrv_list);
        //step2:new Adapter
        SimpleAdapter adapter = new SimpleAdapter(this, datas, R.layout.item_0);
        //step3:setAdapter
        recyclerView.setAdapter(adapter);
    }
}
