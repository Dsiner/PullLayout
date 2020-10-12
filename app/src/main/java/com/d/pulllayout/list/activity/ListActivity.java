package com.d.pulllayout.list.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.app.v4.BaseFragmentActivity;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.common.view.TitleLayout;
import com.d.lib.common.view.popup.MenuPopup;
import com.d.lib.common.view.popup.PopupWindowFactory;
import com.d.pulllayout.R;
import com.d.pulllayout.list.fragment.CoordinatorLayoutFragment;
import com.d.pulllayout.list.fragment.ItemTouchFragment;
import com.d.pulllayout.list.fragment.MultipleFragment;
import com.d.pulllayout.list.fragment.SimpleFragment;
import com.d.pulllayout.list.model.ListType;

/**
 * ListActivity
 * Created by D on 2017/4/26.
 */
public class ListActivity extends BaseFragmentActivity<MvpBasePresenter>
        implements View.OnClickListener {

    public static final String ARG_TYPE = "ARG_TYPE";
    public static final String ARG_ARGS = "ARG_ARGS";

    public static final int TYPE_SIMPLE = 0;
    public static final int TYPE_MULTIPLE = 1;
    public static final int TYPE_COORDINATOR_LIST = 2;
    public static final int TYPE_ITEM_TOUCH = 3;

    private TitleLayout tl_title;
    private int mType;
    private int mListType;
    private Fragment mCurFragment;

    public static void openActivity(Context context, int type) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(ARG_TYPE, type);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static int getListType(Fragment fragment) {
        return fragment.getArguments() != null
                ? fragment.getArguments().getInt(ListActivity.ARG_ARGS, ListType.PULLRECYCLERLAYOUT_RECYCLERVIEW)
                : ListType.PULLRECYCLERLAYOUT_RECYCLERVIEW;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;

            case R.id.iv_title_right:
                onMore(mListType);
                break;
        }
    }

    public void onMore(final int listType) {
        MenuPopup menuPopup = PopupWindowFactory.createFactory(this)
                .getMenuPopup(ListType.getTypeBeans(mListType),
                        new MenuPopup.OnMenuListener() {
                            @Override
                            public void onClick(PopupWindow popup, int position, String item) {
                                if (listType == position) {
                                    return;
                                }
                                mListType = position;
                                replace(mType, mListType);
                            }
                        });
        menuPopup.showAsDropDown((View) ViewHelper.findView(this, R.id.iv_title_right));
    }

    @Override
    protected int getLayoutRes() {
        return mType == TYPE_COORDINATOR_LIST
                ? R.layout.fragment_list_coordinatorlayout
                : R.layout.lib_pub_activity_abs_content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mType = getIntent().getIntExtra(ARG_TYPE, TYPE_SIMPLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void bindView() {
        super.bindView();
        tl_title = ViewHelper.findView(this, R.id.tl_title);
        tl_title.setVisibility(R.id.iv_title_right, View.VISIBLE);

        ViewHelper.setOnClick(this, this, R.id.iv_title_left,
                R.id.iv_title_right);
    }

    @Override
    protected void init() {
        ImageView iv_title_right = ViewHelper.findView(tl_title, R.id.iv_title_right);
        iv_title_right.setImageResource(R.drawable.lib_pub_ic_title_more);

        replace(mType, mListType);
    }

    public void replace(int type, int listType) {
        final String title;
        final Fragment fragment;
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_ARGS, listType);
        if (type == TYPE_SIMPLE) {
            title = "Simple";
            fragment = new SimpleFragment();
        } else if (type == TYPE_MULTIPLE) {
            title = "Multiple";
            fragment = new MultipleFragment();
        } else if (type == TYPE_COORDINATOR_LIST) {
            title = "CoordinatorLayout";
            fragment = new CoordinatorLayoutFragment();
        } else if (type == TYPE_ITEM_TOUCH) {
            title = "Item Touch";
            fragment = new ItemTouchFragment();
        } else {
            title = "Invalid";
            fragment = new Fragment();
        }
        fragment.setArguments(bundle);
        tl_title.setText(R.id.tv_title_title, title);
        tl_title.setVisibility(mType == TYPE_ITEM_TOUCH ? View.GONE : View.VISIBLE);
        mCurFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, fragment).commitAllowingStateLoss();
    }
}
