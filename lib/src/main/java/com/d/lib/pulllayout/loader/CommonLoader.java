package com.d.lib.pulllayout.loader;

import com.d.lib.pulllayout.Refreshable;

import java.util.ArrayList;
import java.util.List;

/**
 * CommonLoader
 * Created by D on 2017/8/23.
 */
public class CommonLoader<T> {
    public final static int PAGE_COUNT = 20; // Number of data per page

    public int page = 1;

    private Refreshable mRefreshable;
    private RecyclerAdapter<T> mAdapter;
    private List<T> mDatas;
    private int mPageCount = PAGE_COUNT;
    private OnLoaderListener mListener;

    public CommonLoader(Refreshable refreshable, RecyclerAdapter<T> adapter) {
        this.mDatas = new ArrayList<>();
        this.mRefreshable = refreshable;
        this.mAdapter = adapter;
        this.mRefreshable.setOnRefreshListener(new Refreshable.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (mListener != null) {
                    mListener.onRefresh();
                }
            }

            @Override
            public void onLoadMore() {
                page++;
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        });
    }

    public void setPageCount(int count) {
        this.mPageCount = count;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void loadSuccess(List<T> data) {
        if (data == null) {
            return;
        }
        int sizeLoad = data.size();
        initList(data);
        if (page == 1) {
            mRefreshable.refreshSuccess();
        } else {
            if (sizeLoad < mPageCount) {
                mRefreshable.loadMoreNoMore();
            } else {
                mRefreshable.loadMoreSuccess();
            }
        }

        if (mListener != null) {
            if (page == 1 && sizeLoad <= 0) {
                mListener.noContent();
            } else {
                mListener.loadSuccess();
            }
        }
    }

    public void loadError() {
        if (page == 1) {
            mRefreshable.refreshError();
        } else {
            page--;
            mRefreshable.loadMoreError();
        }
        if (mListener != null) {
            mListener.loadError(mDatas.size() <= 0);
        }
    }

    private void initList(List<T> caches) {
        if (page == 1) {
            mDatas.clear();
            mDatas.addAll(caches);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        } else {
            mDatas.addAll(caches);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addTop(T data) {
        if (mDatas != null && data != null) {
            mDatas.add(0, data);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addTop(List<T> datas) {
        if (mDatas != null && datas != null) {
            mDatas.addAll(0, datas);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addData(T data) {
        if (mDatas != null && data != null) {
            mDatas.add(data);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addData(List<T> datas) {
        if (mDatas != null && datas != null) {
            mDatas.addAll(datas);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addData(int position, T data) {
        if (mDatas != null && data != null
                && position >= 0 && position <= mDatas.size()) {
            mDatas.add(position, data);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addData(int position, List<T> datas) {
        if (mDatas != null && datas != null
                && position >= 0 && position <= mDatas.size()) {
            mDatas.addAll(position, datas);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface OnLoaderListener {
        void onRefresh();

        void onLoadMore();

        void noContent();

        void loadSuccess();

        void loadError(boolean isEmpty);
    }

    public void setOnLoaderListener(OnLoaderListener listener) {
        this.mListener = listener;
    }
}
