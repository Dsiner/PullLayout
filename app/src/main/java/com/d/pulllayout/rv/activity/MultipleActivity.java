package com.d.pulllayout.rv.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.pulllayout.Pullable;
import com.d.lib.pulllayout.Refreshable;
import com.d.lib.pulllayout.rv.PullRecyclerView;
import com.d.lib.pulllayout.rv.adapter.MultiItemTypeSupport;
import com.d.pulllayout.R;
import com.d.pulllayout.rv.adapter.MultipleAdapter;
import com.d.pulllayout.rv.model.Bean;
import com.d.pulllayout.rv.presenter.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * Multiple Type
 * Created by D on 2017/2/26.
 */
public class MultipleActivity extends AppCompatActivity {
    private PullRecyclerView rv_list;
    private MultipleAdapter mAdapter;
    private int mRefreshTime;
    private int mTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple);
        bindView();
        init();
        setData(Factory.createDatas(Factory.PAGE_COUNT));
    }

    private void bindView() {
        rv_list = (PullRecyclerView) this.findViewById(R.id.rv_list);
    }

    private void init() {
        // Step 3-1: Set the {@link LayoutManager, @link RecyclerView.Adapter}
        // that this RecyclerView will use.
        mAdapter = new MultipleAdapter(MultipleActivity.this,
                new ArrayList<Bean>(), new MultiItemTypeSupport<Bean>() {
            @Override
            public int getLayoutId(int viewType) {
                switch (viewType) {
                    case 1:
                        return R.layout.adapter_item_image;

                    case 2:
                        return R.layout.adapter_item_music;

                    case 3:
                        return R.layout.adapter_item_video;

                    case 0:
                    default:
                        return R.layout.adapter_item_doc;
                }
            }

            @Override
            public int getItemViewType(int position, Bean bean) {
                return bean.type;
            }
        });
        rv_list.setAdapter(mAdapter);

        // Step 3: Set ths header (optional)
        View header = LayoutInflater.from(this).inflate(R.layout.item_header, (ViewGroup) findViewById(android.R.id.content), false);
        rv_list.addHeaderView(header);

        // Step 6: Set the listener to be notified when a refresh is triggered via the swipe gesture.
        rv_list.setOnRefreshListener(new Refreshable.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTimes = 0;
                mRefreshTime++;
                Factory.onRefresh(mRefreshTime, new Factory.SimpleCallback<List<Bean>>() {

                    @Override
                    public void onSuccess(@NonNull List<Bean> result) {
                        mAdapter.setDatas(result);
                        mAdapter.notifyDataSetChanged();
                        rv_list.refreshSuccess();
                    }
                });
            }

            @Override
            public void onLoadMore() {
                mTimes++;
                Factory.onLoadMore(mTimes, mAdapter.getDatas().size(),
                        new Factory.SimpleCallback<List<Bean>>() {
                            @Override
                            public void onSuccess(@NonNull List<Bean> result) {
                                List<Bean> datas = new ArrayList<>(mAdapter.getDatas());
                                if (mTimes < 6) {
                                    if (mTimes % 2 == 0) {
                                        // Test type loadMoreError
                                        mAdapter.notifyDataSetChanged();
                                        rv_list.loadMoreError();
                                    } else {
                                        // Test type loadMoreComplete
                                        datas.addAll(result);
                                        mAdapter.setDatas(datas);
                                        mAdapter.notifyDataSetChanged();
                                        rv_list.loadMoreSuccess();
                                    }
                                } else {
                                    // Test type noMore
                                    datas.addAll(result);
                                    mAdapter.setDatas(datas);
                                    mAdapter.notifyDataSetChanged();
                                    rv_list.loadMoreNoMore();
                                }
                            }
                        });
            }
        });
    }

    private void setData(List<Bean> datas) {
        mAdapter.setDatas(datas);
        mAdapter.notifyDataSetChanged();
    }
}
