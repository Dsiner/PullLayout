package com.d.pulllayout.pull.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.d.pulllayout.R;
import com.d.pulllayout.pull.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ListViewActivity
 * Created by D on 2018/5/31.
 */
public class ListViewActivity extends AppCompatActivity {
    private ListView lvList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_listview);
        lvList = findViewById(R.id.lv_list);
        init();
    }

    private void init() {
        lvList.setAdapter(new ListViewAdapter(this, getDatas(), R.layout.adapter_item));
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
