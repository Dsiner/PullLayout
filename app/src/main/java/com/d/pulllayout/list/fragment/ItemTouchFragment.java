package com.d.pulllayout.list.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.v4.BaseFragment;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.common.view.TitleLayout;
import com.d.lib.common.view.popup.MenuPopup;
import com.d.lib.common.view.popup.PopupWindowFactory;
import com.d.lib.pulllayout.rv.PullRecyclerView;
import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.itemtouchhelper.OnStartDragListener;
import com.d.lib.pulllayout.rv.itemtouchhelper.SimpleItemTouchHelperCallback;
import com.d.pulllayout.R;
import com.d.pulllayout.list.adapter.rv.ItemTouchGridAdapter;
import com.d.pulllayout.list.adapter.rv.ItemTouchLinearAdapter;
import com.d.pulllayout.list.model.Bean;

import java.util.Arrays;

/**
 * ItemTouch
 * Created by D on 2017/4/26.
 */
public class ItemTouchFragment extends BaseFragment<MvpBasePresenter>
        implements View.OnClickListener {

    private TitleLayout tl_title;
    private PullRecyclerView mPullList;
    private CommonAdapter<Bean> mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private int mLayoutManagerType = 1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                getActivity().finish();
                break;

            case R.id.iv_title_right:
                MenuPopup menuPopup = PopupWindowFactory.createFactory(mActivity)
                        .getMenuPopup(Arrays.asList(
                                new MenuPopup.Bean("LinearLayoutManager",
                                        mLayoutManagerType == 0 ?
                                                R.color.lib_pub_color_main
                                                : R.color.lib_pub_color_white, false),
                                new MenuPopup.Bean("GridLayoutManager",
                                        mLayoutManagerType == 1 ?
                                                R.color.lib_pub_color_main
                                                : R.color.lib_pub_color_white, false)),
                                new MenuPopup.OnMenuListener() {
                                    @Override
                                    public void onClick(PopupWindow popup, int position, String item) {
                                        if (mLayoutManagerType == position) {
                                            return;
                                        }
                                        mLayoutManagerType = position;
                                        onLayoutManagerChange();
                                    }
                                });
                menuPopup.showAsDropDown((View) ViewHelper.findViewById(mRootView, R.id.iv_title_right));
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_list_item_touch;
    }

    @Override
    public MvpBasePresenter getPresenter() {
        return new MvpBasePresenter(getActivity().getApplicationContext());
    }

    @Override
    protected MvpView getMvpView() {
        return this;
    }

    @Override
    protected void bindView(View rootView) {
        tl_title = ViewHelper.findViewById(rootView, R.id.tl_title);
        tl_title.setVisibility(R.id.iv_title_right, View.VISIBLE);
        mPullList = ViewHelper.findViewById(rootView, R.id.rv_list);

        ViewHelper.setOnClickListener(rootView, this, R.id.iv_title_left,
                R.id.iv_title_right);
    }

    @Override
    protected void init() {
        tl_title.setText(R.id.tv_title_title, "Item Touch");
        ImageView iv_title_right = ViewHelper.findViewById(tl_title, R.id.iv_title_right);
        iv_title_right.setImageResource(R.drawable.lib_pub_ic_title_more);

        mPullList.setCanPullDown(false);
        mPullList.setCanPullUp(false);
        mPullList.setHasFixedSize(true);

        onLayoutManagerChange();
    }

    private void onLayoutManagerChange() {
        final RecyclerView.LayoutManager layoutManager = mPullList.getLayoutManager();
        // Step 3-1: Set the {@link LayoutManager, @link RecyclerView.Adapter}
        // that this RecyclerView will use.
        if (!(layoutManager instanceof GridLayoutManager)) {
            mPullList.setLayoutManager(new GridLayoutManager(mContext, 4));
            mAdapter = new ItemTouchGridAdapter(mContext,
                    mAdapter != null ? mAdapter.getDatas() : Bean.create(15),
                    R.layout.adapter_item_touch_grid);
        } else {
            mPullList.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new ItemTouchLinearAdapter(mContext,
                    mAdapter != null ? mAdapter.getDatas() : Bean.create(15),
                    R.layout.adapter_item_touch_linear);
        }
        mPullList.setAdapter(mAdapter);

        // Release (optional)
        if (mItemTouchHelper != null) {
            mItemTouchHelper.attachToRecyclerView(new RecyclerView(mContext));
        }

        // Step 3-2: Attaches the ItemTouchHelper to the provided RecyclerView.
        final ItemTouchHelper.Callback itemTouchHelperCallback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        mAdapter.setOnStartDragListener(new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });
        mItemTouchHelper.attachToRecyclerView(mPullList);
    }
}
