package com.d.pulllayout.rv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.d.lib.pulllayout.Pullable;
import com.d.lib.pulllayout.rv.PullRecyclerView;
import com.d.pulllayout.R;
import com.d.pulllayout.rv.adapter.SimpleAdapter;
import com.d.pulllayout.rv.model.Bean;
import com.d.pulllayout.rv.presenter.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Type
 * Created by D on 2017/4/26.
 */
public class SimpleActivity extends Activity {
    private SimpleAdapter mAdapter;
    private List<Bean> mDatas;
    private int mRefreshTime;
    private int mTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_simple);
        init();
        setData();
    }

    private void init() {
        // Step 1: 获取引用
        final PullRecyclerView rv_list = (PullRecyclerView) this.findViewById(R.id.rv_list);
        // Step 2: 设置LayoutManager, ListView展现形式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        // Step 3: New Adapter
        mAdapter = new SimpleAdapter(this, new ArrayList<Bean>(), R.layout.adapter_item_0);
        // Step 4: Set Adapter
        rv_list.setAdapter(mAdapter);
        // Step 5: setListener
        rv_list.setOnPullListener(new Pullable.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTimes = 0;
                mRefreshTime++;
                Factory.onRefreshTest(mRefreshTime, new Factory.SimpleCallback<List<Bean>>() {
                    @Override
                    public void onSuccess(@NonNull List<Bean> result) {
                        mDatas.clear();
                        mDatas.addAll(result);
                        mAdapter.setDatas(mDatas);
                        mAdapter.notifyDataSetChanged();
                        rv_list.refreshSuccess();
                    }
                });
            }

            @Override
            public void onLoadMore() {
                mTimes++;
                Factory.onLoadMoreTest(mTimes, mDatas.size(), new Factory.SimpleCallback<List<Bean>>() {
                    @Override
                    public void onSuccess(@NonNull List<Bean> result) {
                        if (mTimes < 6) {
                            if (mTimes % 2 == 0) {
                                // Test type loadMoreError
                                mAdapter.notifyDataSetChanged();
                                rv_list.loadMoreError();
                            } else {
                                // Test type loadMoreComplete
                                mDatas.addAll(result);
                                mAdapter.setDatas(mDatas);
                                mAdapter.notifyDataSetChanged();
                                rv_list.loadMoreSuccess();
                            }
                        } else {
                            // Test type noMore
                            mDatas.addAll(result);
                            mAdapter.setDatas(mDatas);
                            mAdapter.notifyDataSetChanged();
                            rv_list.loadMoreNoMore();
                        }
                    }
                });
            }
        });
    }

    private void setData() {
        mDatas = Factory.createDatas(Factory.PAGE_COUNT);
        mAdapter.setDatas(mDatas);
        mAdapter.notifyDataSetChanged();
    }
}
