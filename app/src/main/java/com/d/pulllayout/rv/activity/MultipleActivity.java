package com.d.pulllayout.rv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.pulllayout.Pullable;
import com.d.lib.pulllayout.rv.PullRecyclerView;
import com.d.lib.pulllayout.rv.adapter.MultiItemTypeSupport;
import com.d.pulllayout.R;
import com.d.pulllayout.rv.adapter.MultipleAdapter;
import com.d.pulllayout.rv.model.Bean;
import com.d.pulllayout.rv.presenter.Factory;

import java.util.List;

/**
 * Multiple Type
 * Created by D on 2017/2/26.
 */
public class MultipleActivity extends Activity {
    private MultipleAdapter mAdapter;
    private List<Bean> mDatas;
    private int mRefreshTime;
    private int mTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_multiple);
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
        // Step 3: Set Header(可选)
        View header = LayoutInflater.from(this).inflate(R.layout.item_header, (ViewGroup) findViewById(android.R.id.content), false);
        rv_list.addHeaderView(header);
        // Step 4: New Adapter
        mAdapter = new MultipleAdapter(MultipleActivity.this, mDatas, new MultiItemTypeSupport<Bean>() {
            @Override
            public int getLayoutId(int viewType) {
                // Step 4-2: 根据Type返回Layout布局
                switch (viewType) {
                    case 0:
                        return R.layout.adapter_item_0;
                    case 1:
                        return R.layout.adapter_item_1;
                    case 2:
                        return R.layout.adapter_item_2;
                    case 3:
                        return R.layout.adapter_item_3;
                    default:
                        return R.layout.adapter_item_0;
                }
            }

            @Override
            public int getItemViewType(int position, Bean bean) {
                // Step 4-1: 获取Type类型
                return bean.type;
            }
        });
        // Step 5: Set Adapter
        rv_list.setAdapter(mAdapter);
        // Step 6: Set Listener
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
