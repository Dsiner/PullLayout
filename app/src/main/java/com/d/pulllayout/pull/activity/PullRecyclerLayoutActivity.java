package com.d.pulllayout.pull.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.d.lib.pulllayout.PullRecyclerLayout;
import com.d.pulllayout.R;
import com.d.pulllayout.pull.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerLayoutActivity
 * Created by D on 2018/5/31.
 */
public class PullRecyclerLayoutActivity extends AppCompatActivity {
    private PullRecyclerLayout prl_list;
    private RecyclerView rv_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_recyclerlayout);
        prl_list =  findViewById(R.id.prl_list);
        rv_list = (RecyclerView) prl_list.getChildAt(1);
        init();
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        rv_list.setAdapter(new RecyclerAdapter(this, getDatas(), R.layout.adapter_item));
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
