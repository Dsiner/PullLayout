package com.d.pulllayout.edge;

import android.view.View;

import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.pulllayout.edge.IState;
import com.d.lib.pulllayout.edge.ripple.RippleView;
import com.d.pulllayout.R;

public class RippleActivity extends BaseActivity<MvpBasePresenter>
        implements View.OnClickListener {

    private RippleView ripple_view;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                ripple_view.setState(IState.STATE_NONE);
                break;

            case R.id.btn_loading:
                ripple_view.setState(IState.STATE_LOADING);
                break;

            case R.id.btn_success:
                ripple_view.setState(IState.STATE_SUCCESS);
                break;

            case R.id.btn_error:
                ripple_view.setState(IState.STATE_ERROR);
                break;

            case R.id.btn_nomore:
                ripple_view.setState(IState.STATE_NO_MORE);
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_edge_ripple;
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
        ripple_view = findViewById(R.id.ripple_view);

        ViewHelper.setOnClickListener(this, this, R.id.btn_done,
                R.id.btn_loading,
                R.id.btn_success,
                R.id.btn_error,
                R.id.btn_nomore);
    }

    @Override
    protected void init() {
        ripple_view.setState(IState.STATE_LOADING);
    }
}
