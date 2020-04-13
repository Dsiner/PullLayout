package com.d.pulllayout.edge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.lib.pulllayout.edge.IState;
import com.d.lib.pulllayout.edge.ripple.RippleView;
import com.d.pulllayout.MainActivity;
import com.d.pulllayout.R;

public class RippleActivity extends AppCompatActivity implements View.OnClickListener {
    private RippleView ripple_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);
        bindView();
    }

    private void bindView() {
        ripple_view = (RippleView) findViewById(R.id.ripple_view);

        MainActivity.setOnClick(this, this, R.id.btn_done,
                R.id.btn_loading,
                R.id.btn_success,
                R.id.btn_error,
                R.id.btn_nomore);
    }

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
}
