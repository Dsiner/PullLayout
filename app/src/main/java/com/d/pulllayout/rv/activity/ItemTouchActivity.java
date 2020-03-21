package com.d.pulllayout.rv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.d.lib.pulllayout.rv.PullRecyclerView;
import com.d.lib.pulllayout.rv.itemtouchhelper.OnStartDragListener;
import com.d.lib.pulllayout.rv.itemtouchhelper.SimpleItemTouchHelperCallback;
import com.d.pulllayout.R;
import com.d.pulllayout.rv.adapter.ItemTouchAdapter;
import com.d.pulllayout.rv.adapter.SpaceItemDecoration;
import com.d.pulllayout.rv.presenter.Factory;

/**
 * ItemTouch
 * Created by D on 2017/4/26.
 */
public class ItemTouchActivity extends Activity implements OnStartDragListener {
    private PullRecyclerView rv_list;
    private ItemTouchAdapter mAdapter;
    private SpaceItemDecoration mItemDecoration;
    private ItemTouchHelper mItemTouchHelper;
    private boolean mIsLinear = true; // true：线性，false：网格

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_itemtouch);
        init();
        findViewById(R.id.btn_toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsLinear = !mIsLinear;
                setLayoutManager(mIsLinear);
                mAdapter.toggle(mIsLinear);
            }
        });
    }

    private void init() {
        // Step 9-1: 获取引用
        rv_list = (PullRecyclerView) this.findViewById(R.id.rv_list);
        rv_list.setCanPullDown(false);
        rv_list.setCanPullUp(false);
        rv_list.setHasFixedSize(true);
        // Step 9-2: 为RecyclerView指定布局管理对象
        setLayoutManager(mIsLinear);
        // Step 9-3: Set adapter
        mAdapter = new ItemTouchAdapter(this, Factory.createDatas(15), R.layout.adapter_item_touch);
        mAdapter.toggle(mIsLinear);
        mAdapter.setOnStartDragListener(this);
        rv_list.setAdapter(mAdapter);
        // Step 9-4: 关联ItemTouchHelper
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rv_list);
    }

    private void setLayoutManager(boolean isLinear) {
        if (isLinear) {
            // 线性布局
            rv_list.removeItemDecoration(mItemDecoration);
            rv_list.setLayoutManager(new LinearLayoutManager(this));
        } else {
            // 网格布局
            if (mItemDecoration == null) {
                mItemDecoration = new SpaceItemDecoration(10);
            }
            rv_list.addItemDecoration(mItemDecoration);
            rv_list.setLayoutManager(new GridLayoutManager(this, 4));
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        // Step 9-6: 回调, 开始拖拽
        mItemTouchHelper.startDrag(viewHolder);
    }
}
