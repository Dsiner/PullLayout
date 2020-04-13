package com.d.pulllayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.pulllayout.edge.RippleActivity;
import com.d.pulllayout.pull.activity.ListViewActivity;
import com.d.pulllayout.pull.activity.PullRecyclerLayoutActivity;
import com.d.pulllayout.pull.activity.RecyclerViewActivity;
import com.d.pulllayout.pull.activity.ScrollViewActivity;
import com.d.pulllayout.pull.activity.ViewActivity;
import com.d.pulllayout.rv.activity.ItemTouchActivity;
import com.d.pulllayout.rv.activity.MultipleActivity;
import com.d.pulllayout.rv.activity.SimpleActivity;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ripple:
                startActivity(new Intent(MainActivity.this, RippleActivity.class));
                break;

            case R.id.btn_view:
                startActivity(new Intent(MainActivity.this, ViewActivity.class));
                break;

            case R.id.btn_listview:
                startActivity(new Intent(MainActivity.this, ListViewActivity.class));
                break;

            case R.id.btn_recyclerview:
                startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
                break;

            case R.id.btn_scrollview:
                startActivity(new Intent(MainActivity.this, ScrollViewActivity.class));
                break;

            case R.id.btn_simple:
                startActivity(new Intent(MainActivity.this, SimpleActivity.class));
                break;

            case R.id.btn_multiple:
                startActivity(new Intent(MainActivity.this, MultipleActivity.class));
                break;

            case R.id.btn_item_touch:
                startActivity(new Intent(MainActivity.this, ItemTouchActivity.class));
                break;

            case R.id.btn_pullrecyclerlayout:
                startActivity(new Intent(MainActivity.this, PullRecyclerLayoutActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
    }

    private void bindView() {
        setOnClick(this, this, R.id.btn_ripple,
                R.id.btn_view,
                R.id.btn_listview,
                R.id.btn_recyclerview,
                R.id.btn_scrollview,
                R.id.btn_simple,
                R.id.btn_multiple,
                R.id.btn_item_touch,
                R.id.btn_pullrecyclerlayout);
    }

    public static void setOnClick(Activity activity,
                                  View.OnClickListener onClickListener,
                                  @IdRes int... ids) {
        for (int id : ids) {
            activity.findViewById(id).setOnClickListener(onClickListener);
        }
    }
}
