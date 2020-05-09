package com.d.pulllayout;

import android.content.Intent;
import android.view.View;

import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.ViewHelper;
import com.d.pulllayout.edge.RippleActivity;
import com.d.pulllayout.list.activity.CoordinatorTabActivity;
import com.d.pulllayout.list.activity.ListActivity;
import com.d.pulllayout.pull.activity.PullActivity;

public class MainActivity extends BaseActivity<MvpBasePresenter>
        implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ripple:
                startActivity(new Intent(MainActivity.this, RippleActivity.class));
                break;

            case R.id.btn_pull:
                startActivity(new Intent(MainActivity.this, PullActivity.class));
                break;

            case R.id.btn_simple:
                ListActivity.openActivity(mActivity, ListActivity.TYPE_SIMPLE);
                break;

            case R.id.btn_multiple:
                ListActivity.openActivity(mActivity, ListActivity.TYPE_MULTIPLE);
                break;

            case R.id.btn_item_touch:
                ListActivity.openActivity(mActivity, ListActivity.TYPE_ITEM_TOUCH);
                break;

            case R.id.btn_coordinator_list:
                ListActivity.openActivity(mActivity, ListActivity.TYPE_COORDINATOR_LIST);
//                startActivity(new Intent(MainActivity.this, CoordinatorListActivity.class));
                break;

            case R.id.btn_coordinator_tab:
                startActivity(new Intent(MainActivity.this, CoordinatorTabActivity.class));
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
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
        ViewHelper.setOnClick(this, this, R.id.btn_ripple,
                R.id.btn_pull,
                R.id.btn_simple,
                R.id.btn_multiple,
                R.id.btn_item_touch,
                R.id.btn_coordinator_list,
                R.id.btn_coordinator_tab);
    }

    @Override
    protected void init() {
    }
}
