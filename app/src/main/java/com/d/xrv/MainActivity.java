package com.d.xrv;

import android.app.Activity;
import android.os.Bundle;

import com.d.lib.xrv.LRecyclerView;
import com.d.xrv.adapter.MainAdapter;
import com.d.xrv.presenter.Factory;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        LRecyclerView lrvList = (LRecyclerView) this.findViewById(R.id.lrv_list);
        MainAdapter adapter = new MainAdapter(this, Factory.createList(), R.layout.item_0);
        lrvList.setAdapter(adapter);
    }
}
