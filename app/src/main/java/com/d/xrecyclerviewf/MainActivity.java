package com.d.xrecyclerviewf;

import android.app.Activity;
import android.os.Bundle;

import com.d.xrecyclerviewf.model.Bean;
import com.d.xrv.LRecyclerView;

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
        MRvAdapter mRvAdapter = new MRvAdapter(this, datas, R.layout.item_0);
        lrvList.setAdapter(mRvAdapter);
    }
}
