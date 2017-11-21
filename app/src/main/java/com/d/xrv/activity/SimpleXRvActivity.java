package com.d.xrv.activity;

import android.app.Activity;
import android.os.Bundle;

import com.d.lib.xrv.XRecyclerView;
import com.d.lib.xrv.listener.IRecyclerView;
import com.d.xrv.Factory;
import com.d.xrv.R;
import com.d.xrv.adapter.SimpleAdapter;
import com.d.xrv.model.Bean;

import java.util.ArrayList;

/**
 * Simple Type
 * Created by D on 2017/4/26.
 */
public class SimpleXrvActivity extends Activity {
    private ArrayList<Bean> datas;
    private int refreshTime;
    private int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_xrv);
        datas = Factory.createDatas();
        init();
    }

    private void init() {
        //step1:获取引用
        final XRecyclerView recyclerView = (XRecyclerView) this.findViewById(R.id.xrv_list);
        //step2:设置LayoutManager
        recyclerView.showAsList();//listview展现形式
        //step3:new Adapter
        final SimpleAdapter adapter = new SimpleAdapter(this, datas, R.layout.item_0);
        //step4:setAdapter
        recyclerView.setAdapter(adapter);
        //step5:setListener
        recyclerView.setLoadingListener(new IRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                Factory.onRefreshTest(refreshTime, times,
                        datas, adapter, recyclerView);
            }

            @Override
            public void onLoadMore() {
                Factory.onLoadMoreTest(refreshTime, times,
                        datas, adapter, recyclerView);
                times++;
            }
        });
    }
}
