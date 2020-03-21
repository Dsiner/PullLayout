package com.d.lib.pulllayout.rv.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.pulllayout.edge.IEdgeView;
import com.d.lib.pulllayout.rv.HeaderList;

import java.util.List;

/**
 * Adapter(Decorator pattern) for RecyclerView
 * Created by D on 2017/2/9.
 */
public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * The following ItemViewType are reserved values (ReservedItemViewType) .
     * If the user's adapter is duplicated with them, an exception will be forced to be thrown.
     */
    public final static int ITEM_VIEW_TYPE_HEADER_REFRESH = 1000000;
    public final static int ITEM_VIEW_TYPE_HEADER_INDEX = 1000002;
    public final static int ITEM_VIEW_TYPE_FOOTER = 1000001;

    @NonNull
    private final RecyclerView.Adapter mAdapter;
    @NonNull
    private final IEdgeView mHeaderView;
    @NonNull
    private final IEdgeView mFooterView;
    @NonNull
    private final HeaderList mHeaderList;
    private boolean mCanPullDown = true;
    private boolean mCanPullUp = true;

    public WrapAdapter(@NonNull RecyclerView.Adapter adapter,
                       @NonNull IEdgeView headerView,
                       @NonNull IEdgeView footerView,
                       @NonNull HeaderList headerList) {
        this.mAdapter = adapter;
        this.mHeaderView = headerView;
        this.mFooterView = footerView;
        this.mHeaderList = headerList;
    }

    public void setCanPullDown(boolean enable) {
        this.mCanPullDown = enable;
    }

    public void setCanPullUp(boolean enable) {
        this.mCanPullUp = enable;
    }

    @NonNull
    public RecyclerView.Adapter getOriginalAdapter() {
        return this.mAdapter;
    }

    public boolean isHeader(int position) {
        return mCanPullDown && position == 0;
    }

    public boolean isFooter(int position) {
        return mCanPullUp && position == getItemCount() - 1;
    }

    public boolean isHeaderList(int position) {
        return position >= (mCanPullDown ? 1 : 0)
                && position - (mCanPullDown ? 1 : 0) < mHeaderList.size();
    }

    public int getHeadersCount() {
        return (mCanPullDown ? 1 : 0) + mHeaderList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == WrapAdapter.ITEM_VIEW_TYPE_HEADER_REFRESH) {
            return new SimpleViewHolder((View) mHeaderView);
        } else if (isHeaderType(viewType)) {
            return new SimpleViewHolder(getHeaderViewByType(viewType));
        } else if (viewType == WrapAdapter.ITEM_VIEW_TYPE_FOOTER) {
            return new SimpleViewHolder((View) mFooterView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position) || isHeaderList(position)) {
            return;
        }
        final int adjPosition = getAdapterPosition(position);
        int adapterCount = mAdapter.getItemCount();
        if (adjPosition < adapterCount) {
            mAdapter.onBindViewHolder(holder, adjPosition);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (isHeader(position) || isHeaderList(position)) {
            return;
        }
        final int adjPosition = getAdapterPosition(position);
        int itemCount = mAdapter.getItemCount();
        if (adjPosition < itemCount) {
            if (payloads.isEmpty()) {
                mAdapter.onBindViewHolder(holder, adjPosition);
            } else {
                mAdapter.onBindViewHolder(holder, adjPosition, payloads);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter == null) {
            return 0;
        }
        return getHeadersCount()
                + mAdapter.getItemCount()
                + (mCanPullUp ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        final int adjPosition = getAdapterPosition(position);
        if (isHeader(position)) {
            return WrapAdapter.ITEM_VIEW_TYPE_HEADER_REFRESH;
        }
        if (isHeaderList(position)) {
            position = position - (mCanPullDown ? 1 : 0);
            return mHeaderList.getItemViewType(position);
        }
        if (isFooter(position)) {
            return WrapAdapter.ITEM_VIEW_TYPE_FOOTER;
        }
        int itemCount = mAdapter.getItemCount();
        if (adjPosition < itemCount) {
            int type = mAdapter.getItemViewType(adjPosition);
            if (isReservedItemViewType(type)) {
                throw new IllegalStateException("PullRecyclerView require itemViewType in adapter"
                        + " should be less than" + ITEM_VIEW_TYPE_HEADER_REFRESH);
            }
            return type;
        }
        return 0;
    }

    public int getAdapterPosition(int position) {
        return position - getHeadersCount();
    }

    public int getWrapPosition(int position) {
        return position + getHeadersCount();
    }

    @Override
    public long getItemId(int position) {
        final int adjPosition = getAdapterPosition(position);
        if (adjPosition >= 0 && adjPosition < mAdapter.getItemCount()) {
            return mAdapter.getItemId(adjPosition);
        }
        return -1;
    }

    /**
     * Determine if type is header type
     */
    private boolean isHeaderType(int itemViewType) {
        return mHeaderList.size() > 0 && mHeaderList.contains(itemViewType);
    }

    /**
     * Get Header according to Header's ViewType
     */
    @Nullable
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderList.getView(itemType);
    }

    /**
     * Determine if it is a reserved ItemViewType
     */
    private boolean isReservedItemViewType(int itemViewType) {
        return itemViewType == WrapAdapter.ITEM_VIEW_TYPE_HEADER_REFRESH
                || itemViewType == WrapAdapter.ITEM_VIEW_TYPE_FOOTER
                || mHeaderList.contains(itemViewType);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderList(position) || isFooter(position) || isHeader(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (isHeaderList(holder.getLayoutPosition())
                || isHeader(holder.getLayoutPosition())
                || isFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
        mAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        mAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        mAdapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        return mAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        mAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        mAdapter.registerAdapterDataObserver(observer);
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}