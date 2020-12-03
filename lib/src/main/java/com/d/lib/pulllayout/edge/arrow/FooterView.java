package com.d.lib.pulllayout.edge.arrow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.d.lib.pulllayout.R;
import com.d.lib.pulllayout.edge.EdgeView;
import com.d.lib.pulllayout.edge.IEdgeView;

public class FooterView extends EdgeView implements View.OnClickListener {

    private LoadingView ldv_loading;
    private TextView tv_load_more;

    public FooterView(Context context) {
        super(context);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lib_pull_edge_footer;
    }

    @Override
    protected void init(@NonNull final Context context) {
        super.init(context);
        ldv_loading = (LoadingView) mContainer.findViewById(R.id.ldv_loading);
        tv_load_more = (TextView) mContainer.findViewById(R.id.tv_load_more);
    }

    @Override
    public boolean setState(int state) {
        if (mState == state) {
            return false;
        }
        mState = state;
        switch (state) {
            case STATE_NONE:
                setOnClickListener(null);
                ldv_loading.setVisibility(View.VISIBLE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_none));
                break;

            case STATE_EXPANDED:
                setOnClickListener(null);
                ldv_loading.setVisibility(View.VISIBLE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_expanded));
                break;

            case STATE_LOADING:
                setOnClickListener(null);
                ldv_loading.setVisibility(View.VISIBLE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_loading));
                break;

            case STATE_SUCCESS:
                setOnClickListener(null);
                ldv_loading.setVisibility(View.GONE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_success));
                break;

            case STATE_ERROR:
                setOnClickListener(this);
                ldv_loading.setVisibility(View.GONE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_error));
                break;

            case STATE_NO_MORE:
                setOnClickListener(null);
                ldv_loading.setVisibility(View.GONE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_nomore));
                break;
        }
        return true;
    }
}