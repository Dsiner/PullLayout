package com.d.lib.pulllayout.edge.arrow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.d.lib.pulllayout.R;
import com.d.lib.pulllayout.edge.EdgeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HeaderView extends EdgeView {

    private static final int ROTATE_ANIM_DURATION = 180;
    private static final String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    private ImageView img_head_arrow;
    private LoadingView ldv_loading;
    private TextView tv_head_tip;
    private TextView tv_head_last_update_time;
    private RotateAnimation mRotateUpAnim, mRotateDownAnim;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lib_pull_edge_header;
    }

    @Override
    protected void init(@NonNull final Context context) {
        super.init(context);
        bindView();
        initAnim();
        updateTime();
    }

    private void bindView() {
        img_head_arrow = (ImageView) mContainer.findViewById(R.id.img_head_arrow);
        tv_head_tip = (TextView) mContainer.findViewById(R.id.tv_head_tip);
        ldv_loading = (LoadingView) mContainer.findViewById(R.id.ldv_loading);
        tv_head_last_update_time = (TextView) mContainer.findViewById(R.id.tv_head_last_update_time);
    }

    private void initAnim() {
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    public boolean setState(int state) {
        if (mState == state) {
            nestedAnim(state);
            return false;
        }
        switch (state) {
            case STATE_NONE:
                img_head_arrow.clearAnimation();
                img_head_arrow.setVisibility(View.VISIBLE);
                if (mState == STATE_EXPANDED) {
                    img_head_arrow.startAnimation(mRotateDownAnim);
                }
                ldv_loading.setVisibility(View.INVISIBLE);
                tv_head_tip.setText(getResources().getString(R.string.lib_pull_list_refresh_none));
                break;

            case STATE_EXPANDED:
                img_head_arrow.clearAnimation();
                img_head_arrow.setVisibility(View.VISIBLE);
                img_head_arrow.startAnimation(mRotateUpAnim);
                ldv_loading.setVisibility(View.INVISIBLE);
                tv_head_tip.setText(getResources().getString(R.string.lib_pull_list_refresh_expanded));
                break;

            case STATE_LOADING:
                img_head_arrow.clearAnimation();
                img_head_arrow.setVisibility(View.INVISIBLE);
                ldv_loading.setVisibility(View.VISIBLE);
                tv_head_tip.setText(getResources().getString(R.string.lib_pull_list_refresh_loading));
                break;

            case STATE_SUCCESS:
                img_head_arrow.clearAnimation();
                img_head_arrow.setVisibility(View.INVISIBLE);
                ldv_loading.setVisibility(View.INVISIBLE);
                tv_head_tip.setText(getResources().getString(R.string.lib_pull_list_refresh_success));
                updateTime();
                break;

            case STATE_ERROR:
                img_head_arrow.clearAnimation();
                img_head_arrow.setVisibility(View.INVISIBLE);
                ldv_loading.setVisibility(View.INVISIBLE);
                tv_head_tip.setText(getResources().getString(R.string.lib_pull_list_refresh_error));
                updateTime();
                break;
        }
        nestedAnim(state);
        mState = state;
        return true;
    }

    private void nestedAnim(int state) {
        switch (state) {
            case STATE_SUCCESS:
            case STATE_ERROR:
                postNestedAnimDelayed(getStartX(), getStartY(), 0, 0, 450);
                break;
        }
    }

    private void updateTime() {
        if (tv_head_last_update_time != null) {
            tv_head_last_update_time.setText(getResources().getString(R.string.lib_pull_list_header_last_time)
                    + new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
        }
    }
}