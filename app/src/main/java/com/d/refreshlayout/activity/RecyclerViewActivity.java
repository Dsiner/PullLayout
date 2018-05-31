package com.d.refreshlayout.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.d.lib.xrv.LRecyclerView;
import com.d.refreshlayout.R;
import com.d.refreshlayout.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerViewActivity
 * Created by D on 2018/5/31.
 */
public class RecyclerViewActivity extends AppCompatActivity {
    private LRecyclerView lrvList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        lrvList = (LRecyclerView) findViewById(R.id.lrv_list);
        init();
    }

    private void init() {
        lrvList.setAdapter(new RecyclerAdapter(this, getDatas(), R.layout.adapter_item));
    }

    @NonNull
    private List<String> getDatas() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("" + i);
        }
        return datas;
    }
}
