package com.d.pulllayout.pull.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.ViewHelper;
import com.d.pulllayout.R;
import com.d.pulllayout.list.adapter.rv.SimpleAdapter;
import com.d.pulllayout.list.model.Bean;

/**
 * PullRecyclerViewActivity
 * Created by D on 2018/5/31.
 */
public class PullRecyclerViewActivity extends BaseActivity<MvpBasePresenter> {
    private RecyclerView rv_list;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_pull_recyclerview;
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
        rv_list = ViewHelper.findView(this, R.id.rv_list);
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        rv_list.setAdapter(new SimpleAdapter(mContext, Bean.create(20), R.layout.adapter_item_text));
    }
}
