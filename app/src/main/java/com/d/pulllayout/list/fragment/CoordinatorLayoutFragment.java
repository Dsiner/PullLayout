package com.d.pulllayout.list.fragment;

import com.d.lib.pulllayout.util.RefreshableCompat;

public class CoordinatorLayoutFragment extends SimpleFragment {

    @Override
    protected void initList() {
        super.initList();
        RefreshableCompat.setNestedScrollingEnabled(mPullList, true);
    }
}
