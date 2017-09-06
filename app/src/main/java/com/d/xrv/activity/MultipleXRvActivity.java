package com.d.xrv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d.xrv.Factory;
import com.d.xrv.R;
import com.d.xrv.adapter.MultipleAdapter;
import com.d.xrv.model.Bean;
import com.d.lib.xrv.XRecyclerView;
import com.d.lib.xrv.adapter.MultiItemTypeSupport;
import com.d.lib.xrv.listener.IRecyclerView;

import java.util.ArrayList;

/**
 * Multiple Type
 * Created by D on 2017/2/26.
 */
public class MultipleXRvActivity extends Activity {
    private ArrayList<Bean> datas;
    private int refreshTime;
    private int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_xrv);
        datas = Factory.createDatas();
        init();
    }

    private void init() {
        //step1:获取引用
        final XRecyclerView recyclerView = (XRecyclerView) this.findViewById(R.id.xrv_list);
        //step2:设置LayoutManager
        recyclerView.showAsList();//listview展现形式
        //step3:setHeader(可选)
        View header = LayoutInflater.from(this).inflate(R.layout.view_header, (ViewGroup) findViewById(android.R.id.content), false);
        recyclerView.addHeaderView(header);
        //step4:new Adapter
        final MultipleAdapter adapter = new MultipleAdapter(MultipleXRvActivity.this, datas, new MultiItemTypeSupport<Bean>() {
            @Override
            public int getLayoutId(int viewType) {
                //step4-2:根据type返回layout布局
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
                //step4-1:获取type类型
                return bean.type;
            }
        });
        //step5:setAdapter
        recyclerView.setAdapter(adapter);
        //step6:setListener
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
