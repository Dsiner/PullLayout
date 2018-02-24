package com.d.xrv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.d.lib.xrv.XRecyclerView;
import com.d.lib.xrv.listener.IRecyclerView;
import com.d.xrv.R;
import com.d.xrv.adapter.SimpleAdapter;
import com.d.xrv.model.Bean;
import com.d.xrv.presenter.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Type
 * Created by D on 2017/4/26.
 */
public class SimpleXrvActivity extends Activity {
    private SimpleAdapter adapter;
    private List<Bean> datas;
    private int refreshTime;
    private int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_xrv);
        init();
        setData();
    }

    private void init() {
        //step1:获取引用
        final XRecyclerView xrvList = (XRecyclerView) this.findViewById(R.id.xrv_list);
        //step2:设置LayoutManager
        xrvList.showAsList();//listview展现形式
        //step3:new Adapter
        adapter = new SimpleAdapter(this, new ArrayList<Bean>(), R.layout.item_0);
        //step4:setAdapter
        xrvList.setAdapter(adapter);
        //step5:setListener
        xrvList.setLoadingListener(new IRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                times = 0;
                refreshTime++;
                Factory.onRefreshTest(refreshTime, new Factory.SimpleCallback<List<Bean>>() {
                    @Override
                    public void onSuccess(@NonNull List<Bean> result) {
                        datas.clear();
                        datas.addAll(result);
                        adapter.setDatas(datas);
                        adapter.notifyDataSetChanged();
                        xrvList.refreshComplete();
                    }
                });
            }

            @Override
            public void onLoadMore() {
                times++;
                Factory.onLoadMoreTest(times, datas.size(), new Factory.SimpleCallback<List<Bean>>() {
                    @Override
                    public void onSuccess(@NonNull List<Bean> result) {
                        if (times < 6) {
                            if (times % 2 == 0) {
                                //test type loadMoreError
                                adapter.notifyDataSetChanged();
                                xrvList.loadMoreError();
                            } else {
                                //test type loadMoreComplete
                                datas.addAll(result);
                                adapter.setDatas(datas);
                                adapter.notifyDataSetChanged();
                                xrvList.loadMoreComplete();
                            }
                        } else {
                            //test type noMore
                            datas.addAll(result);
                            adapter.setDatas(datas);
                            adapter.notifyDataSetChanged();
                            xrvList.setNoMore(true);
                        }
                    }
                });
            }
        });
    }

    private void setData() {
        datas = Factory.createDatas(Factory.PAGE_COUNT);
        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();
    }
}
