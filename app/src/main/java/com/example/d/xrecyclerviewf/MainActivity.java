package com.example.d.xrecyclerviewf;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.d.xrecyclerviewf.model.MultiBean;
import com.example.xrv.ProgressStyle;
import com.example.xrv.XRecyclerView;
import com.example.xrv.adapter.MultiItemTypeSupport;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private XRecyclerView mRecyclerView;
    private ArrayList<MultiBean> datas;
    private int refreshTime;
    private int times;
    private RvAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();
    }

    private void init() {
        mRecyclerView = (XRecyclerView) this.findViewById(R.id.xrv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        View header = LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup) findViewById(android.R.id.content), false);
        mRecyclerView.addHeaderView(header);

        createData();
        mAdapter = new RvAdapter(MainActivity.this, datas, new MultiItemTypeSupport<MultiBean>() {
            @Override
            public int getLayoutId(int viewType) {
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
            public int getItemViewType(int position, MultiBean multiBean) {
                return multiBean.type;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void createData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            int type = i % 4;
            MultiBean bean = new MultiBean(type, i, "item_" + i, "mark_" + i);
            datas.add(bean);
        }
    }

    private void setListener() {
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        refreshTime++;
                        times = 0;
                        datas.clear();
                        for (int i = 0; i < 15; i++) {
                            int type = i % 4;
                            MultiBean bean = new MultiBean(type, i, "item_" + i + "after " + refreshTime + " times of refresh", "mark_" + i);
                            datas.add(bean);
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                if (times < 2) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for (int i = 0; i < 15; i++) {
                                int type = i % 4;
                                MultiBean bean = new MultiBean(type, i, "item_0" + (1 + datas.size()), "mark_" + i);
                                datas.add(bean);
                            }
                            mRecyclerView.loadMoreComplete();
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for (int i = 0; i < 9; i++) {
                                int type = i % 4;
                                MultiBean bean = new MultiBean(type, i, "item_0" + (1 + datas.size()), "mark_" + i);
                                datas.add(bean);
                            }
                            mRecyclerView.setNoMore(true);
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }
                times++;
            }
        });
    }
}
