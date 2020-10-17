package com.d.pulllayout.pull.activity;

import android.widget.ListView;

import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.ViewHelper;
import com.d.pulllayout.R;
import com.d.pulllayout.list.adapter.lv.SimpleAdapter;
import com.d.pulllayout.list.model.Bean;

/**
 * PullListViewActivity
 * Created by D on 2018/5/31.
 */
public class PullListViewActivity extends BaseActivity<MvpBasePresenter> {
    private ListView lv_list;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_pull_listview;
    }

    @Override
    public MvpBasePresenter getPresenter() {
        return null;
    }

    @Override
    protected MvpView getMvpView() {
        return null;
    }

    @Override
    protected void bindView() {
        super.bindView();
        lv_list = ViewHelper.findViewById(this, R.id.lv_list);
    }

    @Override
    protected void init() {
        lv_list.setAdapter(new SimpleAdapter(this, Bean.create(20), R.layout.adapter_item_text));
    }
}
