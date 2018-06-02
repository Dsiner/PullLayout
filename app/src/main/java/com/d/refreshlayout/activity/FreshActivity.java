package com.d.refreshlayout.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.lib.refreshlayout.refresh.FreshView;
import com.d.lib.refreshlayout.refresh.IFresh;
import com.d.refreshlayout.R;

public class FreshActivity extends AppCompatActivity implements View.OnClickListener {
    private FreshView freshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh);
        freshView = (FreshView) findViewById(R.id.fv_fresh);
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
                freshView.setState(IFresh.STATE_DONE);
                break;
            case R.id.btn_loading:
                freshView.setState(IFresh.STATE_LOADING);
                break;
            case R.id.btn_success:
                freshView.setState(IFresh.STATE_SUCCESS);
                break;
            case R.id.btn_error:
                freshView.setState(IFresh.STATE_ERROR);
                break;
            case R.id.btn_nomore:
                freshView.setState(IFresh.STATE_NOMORE);
                break;
        }
    }
}
