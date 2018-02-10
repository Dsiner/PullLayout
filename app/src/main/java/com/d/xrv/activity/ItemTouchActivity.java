package com.d.xrv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.d.lib.xrv.itemtouchhelper.OnStartDragListener;
import com.d.lib.xrv.itemtouchhelper.SimpleItemTouchHelperCallback;
import com.d.xrv.Factory;
import com.d.xrv.R;
import com.d.xrv.adapter.ItemTouchAdapter;
import com.d.xrv.adapter.SpaceItemDecoration;
import com.d.xrv.model.Bean;

import java.util.ArrayList;

/**
 * Simple Type
 * Created by D on 2017/4/26.
 */
public class ItemTouchActivity extends Activity implements OnStartDragListener {
    private ArrayList<Bean> datas;
    private RecyclerView recyclerView;
    private ItemTouchAdapter adapter;
    private SpaceItemDecoration itemDecoration;
    private ItemTouchHelper itemTouchHelper;
    private boolean isLinear = true;//true：线性，false：网格

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemtouch);
        datas = Factory.createDatas();
        init();
        findViewById(R.id.btn_toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLinear = !isLinear;
                setLayoutManager(isLinear);
                adapter.toggle(isLinear);
            }
        });
    }

    private void init() {
        //step9-1:获取引用-!!!!!!!!!!不要使用XRecyclerView
        recyclerView = (RecyclerView) this.findViewById(R.id.rv_list);
        recyclerView.setHasFixedSize(true);
        //step9-2:为RecyclerView指定布局管理对象
        setLayoutManager(isLinear);
        //step9-3:setAdapter
        adapter = new ItemTouchAdapter(this, datas, R.layout.item_touch);
        adapter.toggle(isLinear);
        adapter.setOnStartDragListener(this);
        recyclerView.setAdapter(adapter);
        //step9-4:关联ItemTouchHelper
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        //!!!!!!!!!!不要使用XRecyclerView-重要的事情说三遍
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setLayoutManager(boolean isLinear) {
        if (isLinear) {
            //线性布局
            recyclerView.removeItemDecoration(itemDecoration);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            //网格布局
            if (itemDecoration == null) {
                itemDecoration = new SpaceItemDecoration(10);
            }
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        //step9-6:回调、开始拖拽
        itemTouchHelper.startDrag(viewHolder);
    }
}
