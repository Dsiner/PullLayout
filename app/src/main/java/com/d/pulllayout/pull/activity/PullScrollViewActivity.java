package com.d.pulllayout.pull.activity;

import android.view.View;

import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.ToastUtils;
import com.d.lib.common.util.ViewHelper;
import com.d.pulllayout.R;

/**
 * PullScrollViewActivity
 * Created by D on 2018/5/31.
 */
public class PullScrollViewActivity extends BaseActivity<MvpBasePresenter>
        implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_button:
                ToastUtils.toast(mContext, "Button");
                break;

            case R.id.tv_text:
                ToastUtils.toast(mContext, "Text");
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_pull_scrollview;
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
        ViewHelper.setOnClick(this, this, R.id.btn_button, R.id.tv_text);
    }

    @Override
    protected void init() {
    }
}
