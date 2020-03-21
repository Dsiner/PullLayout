package com.d.pulllayout.pull.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.lib.pulllayout.edge.IState;
import com.d.lib.pulllayout.edge.ripple.RippleView;
import com.d.pulllayout.R;

public class FreshActivity extends AppCompatActivity implements View.OnClickListener {
    private RippleView rippleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh);
        rippleView = (RippleView) findViewById(R.id.fv_fresh);
        findViewById(R.id.btn_done).setOnClickListener(this);
        findViewById(R.id.btn_loading).setOnClickListener(this);
        findViewById(R.id.btn_success).setOnClickListener(this);
        findViewById(R.id.btn_error).setOnClickListener(this);
        findViewById(R.id.btn_nomore).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                rippleView.setState(IState.STATE_NONE);
                break;
            case R.id.btn_loading:
                rippleView.setState(IState.STATE_LOADING);
                break;
            case R.id.btn_success:
                rippleView.setState(IState.STATE_SUCCESS);
                break;
            case R.id.btn_error:
                rippleView.setState(IState.STATE_ERROR);
                break;
            case R.id.btn_nomore:
                rippleView.setState(IState.STATE_NO_MORE);
                break;
        }
    }
}
