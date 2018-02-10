package com.d.xrv.activity;

import android.app.Activity;
import android.os.Bundle;

import com.d.lib.xrv.LRecyclerView;
import com.d.lib.xrv.adapter.MultiItemTypeSupport;
import com.d.xrv.Factory;
import com.d.xrv.R;
import com.d.xrv.adapter.MultipleAdapter;
import com.d.xrv.model.Bean;

import java.util.ArrayList;

/**
 * Multiple Type
 * Created by D on 2017/2/26.
 */
public class MultipleLrvActivity extends Activity {
    private ArrayList<Bean> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_lrv);
        datas = Factory.createDatas();
        init();
    }

    private void init() {
        //step1:获取引用
        LRecyclerView recyclerView = (LRecyclerView) this.findViewById(R.id.lrv_list);
        //step2:new Adapter
        MultipleAdapter adapter = new MultipleAdapter(this, datas, new MultiItemTypeSupport<Bean>() {
            @Override
            public int getLayoutId(int viewType) {
                //step2-2:根据type返回layout布局
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
                //step2-1:获取type类型
                return bean.type;
            }
        });
        //step3:setAdapter
        recyclerView.setAdapter(adapter);
    }
}
