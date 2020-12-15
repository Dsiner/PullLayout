package com.d.pulllayout.list.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.PopupWindow;

import com.d.lib.common.component.loader.v4.AbsPageFragmentActivity;
import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.common.view.popup.MenuPopup;
import com.d.lib.common.view.popup.PopupWindowFactory;
import com.d.pulllayout.R;
import com.d.pulllayout.list.fragment.CoordinatorLayoutFragment;
import com.d.pulllayout.list.model.ListType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CoordinatorTabActivity
 * Created by D on 2018/5/31.
 */
public class CoordinatorTabActivity extends AbsPageFragmentActivity<MvpBasePresenter> {

    private int mListType = ListType.PULLRECYCLERLAYOUT_PULLRECYCLERVIEW;

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
                .getMenuPopup(ListType.getMenus(mListType),
                        new MenuPopup.OnMenuListener() {
                            @Override
                            public void onClick(PopupWindow popup, int position, String item) {
                                if (listType == position) {
                                    return;
                                }
                                mListType = position;
                                clearAllFragments(mFragmentList);
                                init();
                            }
                        });
        menuPopup.showAsDropDown((View) ViewHelper.findViewById(this, R.id.iv_title_right));
    }

    public void clearAllFragments(List<Fragment> fragments) {
        if (fragments != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : fragments) {
                ft.remove(f);
            }
            ft.commitAllowingStateLoss();
            fm.executePendingTransactions();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_coordinatorlayout;
    }

    @Override
    protected List<String> getTitles() {
        return Arrays.asList("Peach", "Lemon", "Watermelon", "Pear", "Avocado",
                "Banana", "Grape", "Apricot", "Orange", "Kumquat");
    }

    @Override
    protected List<Fragment> getFragments() {
        int size = getTitles().size();
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            final Bundle bundle = new Bundle();
            bundle.putInt(ListActivity.EXTRA_LIST_TYPE, mListType);
            Fragment fragment = new CoordinatorLayoutFragment();
            fragment.setArguments(bundle);
            list.add(fragment);
        }
        return list;
    }

    @Override
    protected void bindView() {
        super.bindView();

        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left,
                R.id.iv_title_right);
    }
}
