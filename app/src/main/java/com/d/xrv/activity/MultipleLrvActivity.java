package com.d.xrv.activity;

import android.app.Activity;
import android.os.Bundle;

import com.d.lib.xrv.LRecyclerView;
import com.d.lib.xrv.adapter.MultiItemTypeSupport;
import com.d.xrv.R;
import com.d.xrv.adapter.MultipleAdapter;
import com.d.xrv.model.Bean;
import com.d.xrv.presenter.Factory;

/**
 * Multiple Type
 * Created by D on 2017/2/26.
 */
public class MultipleLrvActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_lrv);
        init();
    }

    private void init() {
        // Step 1: 获取引用
        LRecyclerView rvList = (LRecyclerView) this.findViewById(R.id.lrv_list);
        // step 2: New Adapter
        MultipleAdapter adapter = new MultipleAdapter(this, Factory.createDatas(15), new MultiItemTypeSupport<Bean>() {
            @Override
            public int getLayoutId(int viewType) {
                // Step 2-2: 根据type返回layout布局
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
                // Step 2-1: 获取type类型
                return bean.type;
            }
        });
        // Step 3: SetAdapter
        rvList.setAdapter(adapter);
    }
}
