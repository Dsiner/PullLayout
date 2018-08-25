package com.d.xrv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.xrv.XRecyclerView;
import com.d.lib.xrv.adapter.MultiItemTypeSupport;
import com.d.lib.xrv.listener.IRecyclerView;
import com.d.xrv.R;
import com.d.xrv.adapter.MultipleAdapter;
import com.d.xrv.model.Bean;
import com.d.xrv.presenter.Factory;

import java.util.List;

/**
 * Multiple Type
 * Created by D on 2017/2/26.
 */
public class MultipleXrvActivity extends Activity {
    private MultipleAdapter adapter;
    private List<Bean> datas;
    private int refreshTime;
    private int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_xrv);
        init();
        setData();
    }

    private void init() {
        // Step 1: 获取引用
        final XRecyclerView xrvList = (XRecyclerView) this.findViewById(R.id.xrv_list);
        // Step 2: 设置LayoutManager, ListView展现形式
        xrvList.showAsList();
        // Step 3: Set Header(可选)
        View header = LayoutInflater.from(this).inflate(R.layout.view_header, (ViewGroup) findViewById(android.R.id.content), false);
        xrvList.addHeaderView(header);
        // Step 4: New Adapter
        adapter = new MultipleAdapter(MultipleXrvActivity.this, datas, new MultiItemTypeSupport<Bean>() {
            @Override
            public int getLayoutId(int viewType) {
                // Step 4-2: 根据Type返回Layout布局
                switch (viewType) {
                    case 0:
                        return R.layout.item_0;
                    case 1:
                        return R.layout.item_1;
                    case 2:
                        return R.layout.item_2;
                    case 3:
                        return R.layout.item_3;
                    default:
                        return R.layout.item_0;
                }
            }

            @Override
            public int getItemViewType(int position, Bean bean) {
                // Step 4-1: 获取Type类型
                return bean.type;
            }
        });
        // Step 5: Set Adapter
        xrvList.setAdapter(adapter);
        // Step 6: Set Listener
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
                                // Test type loadMoreError
                                adapter.notifyDataSetChanged();
                                xrvList.loadMoreError();
                            } else {
                                // Test type loadMoreComplete
                                datas.addAll(result);
                                adapter.setDatas(datas);
                                adapter.notifyDataSetChanged();
                                xrvList.loadMoreComplete();
                            }
                        } else {
                            // Test type noMore
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
