package com.d.pulllayout.rv.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.d.lib.pulllayout.rv.PullRecyclerView;
import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.itemtouchhelper.OnStartDragListener;
import com.d.lib.pulllayout.rv.itemtouchhelper.SimpleItemTouchHelperCallback;
import com.d.pulllayout.MainActivity;
import com.d.pulllayout.R;
import com.d.pulllayout.rv.adapter.ItemTouchGridAdapter;
import com.d.pulllayout.rv.adapter.ItemTouchLinearAdapter;
import com.d.pulllayout.rv.presenter.Factory;

/**
 * ItemTouch
 * Created by D on 2017/4/26.
 */
public class ItemTouchActivity extends AppCompatActivity implements View.OnClickListener {

    private PullRecyclerView rv_list;
    private CommonAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_toggle:
                onLayoutManagerChange();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemtouch);
        bindView();
        init();
    }

    private void bindView() {
        rv_list = (PullRecyclerView) this.findViewById(R.id.rv_list);

        MainActivity.setOnClick(this, this, R.id.btn_toggle);
    }

    private void init() {
        rv_list.setCanPullDown(false);
        rv_list.setCanPullUp(false);
        rv_list.setHasFixedSize(true);

        onLayoutManagerChange();
    }

    private void onLayoutManagerChange() {
        final RecyclerView.LayoutManager layoutManager = rv_list.getLayoutManager();
        // Step 3-1: Set the {@link LayoutManager, @link RecyclerView.Adapter}
        // that this RecyclerView will use.
        if (!(layoutManager instanceof GridLayoutManager)) {
            rv_list.setLayoutManager(new GridLayoutManager(ItemTouchActivity.this, 4));
            mAdapter = new ItemTouchGridAdapter(this,
                    mAdapter != null ? mAdapter.getDatas() : Factory.createDatas(15),
                    R.layout.adapter_item_touch_grid);
        } else {
            rv_list.setLayoutManager(new LinearLayoutManager(ItemTouchActivity.this));
            mAdapter = new ItemTouchLinearAdapter(this,
                    mAdapter != null ? mAdapter.getDatas() : Factory.createDatas(15),
                    R.layout.adapter_item_touch_linear);
        }
        rv_list.setAdapter(mAdapter);

        // Release (optional)
        if (mItemTouchHelper != null) {
            mItemTouchHelper.attachToRecyclerView(new RecyclerView(ItemTouchActivity.this));
        }

        // Step 3-2: Attaches the ItemTouchHelper to the provided RecyclerView.
        final ItemTouchHelper.Callback itemTouchHelperCallback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        mAdapter.setOnStartDragListener(new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });
        mItemTouchHelper.attachToRecyclerView(rv_list);
    }
}
