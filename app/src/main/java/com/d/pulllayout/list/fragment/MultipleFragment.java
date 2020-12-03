package com.d.pulllayout.list.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.component.loader.v4.AbsFragment;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.view.DSLayout;
import com.d.lib.pulllayout.loader.CommonLoader;
import com.d.lib.pulllayout.loader.RecyclerAdapter;
import com.d.lib.pulllayout.rv.adapter.MultiItemTypeSupport;
import com.d.lib.pulllayout.util.RefreshableCompat;
import com.d.pulllayout.R;
import com.d.pulllayout.list.activity.ListActivity;
import com.d.pulllayout.list.adapter.rv.MultipleAdapter;
import com.d.pulllayout.list.model.Bean;
import com.d.pulllayout.list.model.ListType;
import com.d.pulllayout.list.presenter.LoadPresenter;

import java.util.ArrayList;

/**
 * Multiple Type
 * Created by D on 2017/4/26.
 */
public class MultipleFragment extends AbsFragment<Bean, LoadPresenter> {
    private int mListType;

    @Override
    protected int getLayoutRes() {
        return ListType.S_RES_IDS[mListType];
    }

    @Override
    public LoadPresenter getPresenter() {
        return new LoadPresenter(getActivity().getApplicationContext());
    }

    @Override
    protected MvpView getMvpView() {
        return this;
    }

    @Override
    protected RecyclerAdapter<Bean> getAdapter() {
        return mListType == ListType.PULLRECYCLERLAYOUT_LISTVIEW ?
                new com.d.pulllayout.list.adapter.lv.
                        MultipleAdapter(mContext, new ArrayList<Bean>(),
                        new com.d.lib.pulllayout.lv.adapter.
                                MultiItemTypeSupport<Bean>() {
                            @Override
                            public int getViewTypeCount() {
                                return 5;
                            }

                            @Override
                            public int getItemViewType(int position, Bean bean) {
                                return bean.type;
                            }

                            @Override
                            public int getLayoutId(int viewType) {
                                switch (viewType) {
                                    case 1:
                                        return R.layout.adapter_item_image;

                                    case 2:
                                        return R.layout.adapter_item_music;

                                    case 3:
                                        return R.layout.adapter_item_video;

                                    case 0:
                                    default:
                                        return R.layout.adapter_item_doc;
                                }
                            }
                        })
                : new MultipleAdapter(mContext, new ArrayList<Bean>(),
                new MultiItemTypeSupport<Bean>() {
                    @Override
                    public int getItemViewType(int position, Bean bean) {
                        return bean.type;
                    }

                    @Override
                    public int getLayoutId(int viewType) {
                        switch (viewType) {
                            case 1:
                                return R.layout.adapter_item_image;

                            case 2:
                                return R.layout.adapter_item_music;

                            case 3:
                                return R.layout.adapter_item_video;

                            case 0:
                            default:
                                return R.layout.adapter_item_doc;
                        }
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mListType = ListActivity.getExtrasListType(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initList() {
        // Step 3-1: Add header or footer (optional)
        RefreshableCompat.addHeaderView(mPullList, R.layout.adapter_item_header);
        RefreshableCompat.addFooterView(mPullList, R.layout.adapter_item_footer);

        // Step 3-2: Set the {@link LayoutManager, @link RecyclerView.Adapter}
        // that this RecyclerView will use.
        mAdapter = getAdapter();
        RefreshableCompat.setAdapter(mPullList, mAdapter);

        // Step 3-3: Set the listener to be notified when a refresh is triggered via the swipe gesture.
        mCommonLoader = new CommonLoader<>(mPullList, mAdapter);
        mCommonLoader.setOnLoaderListener(new CommonLoader.OnLoaderListener() {
            @Override
            public void onRefresh() {
                onLoad(mCommonLoader.page);
            }

            @Override
            public void onLoadMore() {
                onLoad(mCommonLoader.page);
            }

            @Override
            public void loadSuccess() {
                mDslDs.setState(DSLayout.GONE);
                mPullList.setVisibility(View.VISIBLE);
            }

            @Override
            public void noContent() {
                mDslDs.setState(DSLayout.STATE_EMPTY);
            }

            @Override
            public void loadError(boolean isEmpty) {
                mDslDs.setState(isEmpty ? DSLayout.STATE_NET_ERROR : DSLayout.GONE);
            }
        });
    }

    @Override
    protected void onLoad(int page) {
        mPresenter.get(page);
    }
}
