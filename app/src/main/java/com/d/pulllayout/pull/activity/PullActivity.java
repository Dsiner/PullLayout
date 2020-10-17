package com.d.pulllayout.pull.activity;

import android.content.Intent;
import android.view.View;

import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.ViewHelper;
import com.d.pulllayout.R;

public class PullActivity extends BaseActivity<MvpBasePresenter>
        implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view:
                startActivity(new Intent(PullActivity.this, PullViewActivity.class));
                break;

            case R.id.btn_listview:
                startActivity(new Intent(PullActivity.this, PullListViewActivity.class));
                break;

            case R.id.btn_recyclerview:
                startActivity(new Intent(PullActivity.this, PullRecyclerViewActivity.class));
                break;

            case R.id.btn_scrollview:
                startActivity(new Intent(PullActivity.this, PullScrollViewActivity.class));
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_pull;
    }

    @Override
    public MvpBasePresenter getPresenter() {
        return new MvpBasePresenter(getApplicationContext());
    }

    @Override
    protected MvpView getMvpView() {
        return this;
    }

    @Override
    protected void bindView() {
        ViewHelper.setOnClickListener(this, this,
                R.id.btn_view,
                R.id.btn_listview,
                R.id.btn_recyclerview,
                R.id.btn_scrollview);
    }

    @Override
    protected void init() {
    }
}
