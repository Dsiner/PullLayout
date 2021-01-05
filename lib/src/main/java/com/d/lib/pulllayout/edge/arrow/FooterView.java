package com.d.lib.pulllayout.edge.arrow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.d.lib.pulllayout.R;
import com.d.lib.pulllayout.edge.EdgeView;

public class FooterView extends EdgeView {

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
            nestedAnim(state);
            return false;
        }
        switch (state) {
            case STATE_NONE:
                ldv_loading.setVisibility(View.VISIBLE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_none));
                break;

            case STATE_EXPANDED:
                ldv_loading.setVisibility(View.VISIBLE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_expanded));
                break;

            case STATE_LOADING:
                ldv_loading.setVisibility(View.VISIBLE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_loading));
                break;

            case STATE_SUCCESS:
                ldv_loading.setVisibility(View.GONE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_success));
                break;

            case STATE_ERROR:
                ldv_loading.setVisibility(View.GONE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_error));
                break;

            case STATE_NO_MORE:
                ldv_loading.setVisibility(View.GONE);
                tv_load_more.setText(getResources().getString(R.string.lib_pull_list_load_more_nomore));
                break;
        }
        nestedAnim(state);
        setOnClickListener(state == STATE_ERROR ? mOnFooterClickListener : null);
        mState = state;
        return true;
    }

    private void nestedAnim(int state) {
        switch (state) {
            case STATE_SUCCESS:
            case STATE_NO_MORE:
                startNestedAnim(getStartX(), getStartY(), 0, 0);
                break;

            case STATE_ERROR:
                postNestedAnimDelayed(getStartX(), getStartY(), 0, 0, 450);
                break;
        }
    }
}