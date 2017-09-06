package com.d.xrv;

import android.app.Activity;
import android.os.Bundle;

import com.d.xrv.model.Bean;
import com.d.lib.xrv.LRecyclerView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<Bean> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datas = Factory.createMList();
        init();
    }

    private void init() {
        LRecyclerView lrvList = (LRecyclerView) this.findViewById(R.id.lrv_list);
        MainAdapter adapter = new MainAdapter(this, datas, R.layout.item_0);
        lrvList.setAdapter(adapter);
    }
}
