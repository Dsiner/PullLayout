package com.d.pulllayout.rv;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.d.pulllayout.R;
import com.d.pulllayout.rv.adapter.MainAdapter;
import com.d.pulllayout.rv.presenter.Factory;

public class PullRecyclerViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_pull);
        init();
    }

    private void init() {
        RecyclerView rv_list = (RecyclerView) this.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        MainAdapter adapter = new MainAdapter(this, Factory.createList(), R.layout.adapter_item_0);
        rv_list.setAdapter(adapter);
    }
}
