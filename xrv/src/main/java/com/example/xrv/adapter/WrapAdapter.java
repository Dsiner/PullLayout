package com.example.xrv.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.example.xrv.ArrowRefreshHeader;
import com.example.xrv.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * recyclerview装饰者模式adapter
 * Created by Shenyulei on 2017/2/9.
 */

public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter adapter;
    private boolean loadingMoreEnabled = true;
    private ArrayList<View> mHeaderViews;
    private ArrowRefreshHeader mRefreshHeader;
    private View mFootView;
    private List<Integer> sHeaderTypes;

    public WrapAdapter(RecyclerView.Adapter adapter,
                       boolean loadingMoreEnabled,
                       ArrayList<View> mHeaderViews,
                       ArrowRefreshHeader mRefreshHeader,
                       View mFootView,
                       List<Integer> sHeaderTypes) {
        this.adapter = adapter;
        this.loadingMoreEnabled = loadingMoreEnabled;
        this.mHeaderViews = mHeaderViews;
        this.mRefreshHeader = mRefreshHeader;
        this.mFootView = mFootView;
        this.sHeaderTypes = sHeaderTypes;
    }

    /**
     * 同步设置引用参数
     */
    public void setArgs(boolean loadingMoreEnabled,
                        ArrayList<View> mHeaderViews,
                        ArrowRefreshHeader mRefreshHeader,
                        View mFootView,
                        List<Integer> sHeaderTypes) {
        this.loadingMoreEnabled = loadingMoreEnabled;
        this.mHeaderViews = mHeaderViews;
        this.mRefreshHeader = mRefreshHeader;
        this.mFootView = mFootView;
        this.sHeaderTypes = sHeaderTypes;
    }

    public RecyclerView.Adapter getOriginalAdapter() {
        return this.adapter;
    }

    public boolean isHeader(int position) {
        return position >= 1 && position < mHeaderViews.size() + 1;
    }

    public boolean isFooter(int position) {
        if (loadingMoreEnabled) {
            return position == getItemCount() - 1;
        } else {
            return false;
        }
    }

    public boolean isRefreshHeader(int position) {
        return position == 0;
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == XRecyclerView.TYPE_REFRESH_HEADER) {
            return new SimpleViewHolder(mRefreshHeader);
        } else if (isHeaderType(viewType)) {
            return new SimpleViewHolder(getHeaderViewByType(viewType));
        } else if (viewType == XRecyclerView.TYPE_FOOTER) {
            return new SimpleViewHolder(mFootView);
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position) || isRefreshHeader(position)) {
            return;
        }
        int adjPosition = position - (getHeadersCount() + 1);
        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                adapter.onBindViewHolder(holder, adjPosition);
            }
        }
    }

    // some times we need to override this
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (isHeader(position) || isRefreshHeader(position)) {
            return;
        }
        int adjPosition = position - (getHeadersCount() + 1);
        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                if (payloads.isEmpty()) {
                    adapter.onBindViewHolder(holder, adjPosition);
                } else {
                    adapter.onBindViewHolder(holder, adjPosition, payloads);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (loadingMoreEnabled) {
            if (adapter != null) {
                return getHeadersCount() + adapter.getItemCount() + 2;
            } else {
                return getHeadersCount() + 2;
            }
        } else {
            if (adapter != null) {
                return getHeadersCount() + adapter.getItemCount() + 1;
            } else {
                return getHeadersCount() + 1;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int adjPosition = position - (getHeadersCount() + 1);
        if (isRefreshHeader(position)) {
            return XRecyclerView.TYPE_REFRESH_HEADER;
        }
        if (isHeader(position)) {
            position = position - 1;
            return sHeaderTypes.get(position);
        }
        if (isFooter(position)) {
            return XRecyclerView.TYPE_FOOTER;
        }
        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                int type = adapter.getItemViewType(adjPosition);
                if (isReservedItemViewType(type)) {
                    throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
                }
                return type;
            }
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (adapter != null && position >= getHeadersCount() + 1) {
            int adjPosition = position - (getHeadersCount() + 1);
            if (adjPosition < adapter.getItemCount()) {
                return adapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    //判断一个type是否为HeaderType
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViews.size() > 0 && sHeaderTypes.contains(itemViewType);
    }

    //根据header的ViewType判断是哪个header
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - XRecyclerView.HEADER_INIT_INDEX);
    }

    //判断是否是XRecyclerView保留的itemViewType
    private boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == XRecyclerView.TYPE_REFRESH_HEADER
                || itemViewType == XRecyclerView.TYPE_FOOTER
                || sHeaderTypes.contains(itemViewType)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
        adapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (isHeader(holder.getLayoutPosition()) || isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
        adapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        adapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        adapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return adapter.onFailedToRecycleView(holder);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        adapter.registerAdapterDataObserver(observer);
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}