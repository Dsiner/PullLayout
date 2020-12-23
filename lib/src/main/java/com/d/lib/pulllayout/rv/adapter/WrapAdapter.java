package com.d.lib.pulllayout.rv.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.pulllayout.edge.IEdgeView;
import com.d.lib.pulllayout.rv.ItemViewList;

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
    public static final int ITEM_VIEW_TYPE_HEADER = 1000000;
    public static final int ITEM_VIEW_TYPE_FOOTER = 1000001;
    public static final int ITEM_VIEW_TYPE_HEADER_LIST_INDEX = 1100000;
    public static final int ITEM_VIEW_TYPE_FOOTER_LIST_INDEX = 1200000;

    @NonNull
    private final RecyclerView.Adapter mAdapter;
    @NonNull
    private final IEdgeView mHeaderView;
    @NonNull
    private final IEdgeView mFooterView;
    @NonNull
    private final ItemViewList mHeaderList, mFooterList;
    private boolean mCanPullDown = false;
    private boolean mCanPullUp = false;

    public WrapAdapter(@NonNull RecyclerView.Adapter adapter,
                       @NonNull IEdgeView headerView,
                       @NonNull IEdgeView footerView,
                       @NonNull ItemViewList headerList,
                       @NonNull ItemViewList footerList) {
        this.mAdapter = adapter;
        this.mHeaderView = headerView;
        this.mFooterView = footerView;
        this.mHeaderList = headerList;
        this.mFooterList = footerList;
    }

    @NonNull
    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public void setCanPullDown(boolean enable) {
        this.mCanPullDown = enable;
    }

    public void setCanPullUp(boolean enable) {
        this.mCanPullUp = enable;
    }

    public int getAdapterPosition(int position) {
        return position - getHeadersCount();
    }

    public int getWrapPosition(int position) {
        return position + getHeadersCount();
    }

    private boolean isHeader(int position) {
        return mCanPullDown && position == 0;
    }

    private boolean isFooter(int position) {
        return mCanPullUp && position == getItemCount() - 1;
    }

    private boolean isHeaderList(int position) {
        int start = mCanPullDown ? 1 : 0;
        int end = (mCanPullDown ? 1 : 0) + mHeaderList.size();
        return position >= start && position < end;
    }

    private boolean isFooterList(int position) {
        int itemCount = getItemCount();
        int start = itemCount - (mCanPullUp ? 1 : 0) - mFooterList.size();
        int end = itemCount - (mCanPullUp ? 1 : 0);
        return position >= start && position < end;
    }

    private int getHeadersCount() {
        return (mCanPullDown ? 1 : 0) + mHeaderList.size();
    }

    private int getFootersCount() {
        return (mCanPullUp ? 1 : 0) + mFooterList.size();
    }

    /**
     * Determine if it is the reserved view type of the item
     */
    public boolean isReservedItemViewType(int viewType) {
        return ITEM_VIEW_TYPE_HEADER == viewType
                || ITEM_VIEW_TYPE_FOOTER == viewType
                || mHeaderList.contains(viewType)
                || mFooterList.contains(viewType);
    }

    /**
     * Determine if it is the reserved position
     */
    public boolean isReservedPosition(int position) {
        return isHeader(position) || isHeaderList(position)
                || isFooter(position) || isFooterList(position);
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + mAdapter.getItemCount() + getFootersCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return ITEM_VIEW_TYPE_HEADER;

        } else if (isHeaderList(position)) {
            int start = mCanPullDown ? 1 : 0;
            int index = position - start;
            return mHeaderList.getItemViewType(index);

        } else if (isFooterList(position)) {
            int start = getItemCount() - (mCanPullUp ? 1 : 0) - mFooterList.size();
            int index = position - start;
            return mFooterList.getItemViewType(index);

        } else if (isFooter(position)) {
            return ITEM_VIEW_TYPE_FOOTER;
        }

        final int type = mAdapter.getItemViewType(getAdapterPosition(position));
        if (isReservedItemViewType(type)) {
            throw new IllegalStateException("The view type of the item in adapter"
                    + " should be less than" + ITEM_VIEW_TYPE_HEADER);
        }
        return type;
    }

    @Override
    public long getItemId(int position) {
        if (isReservedPosition(position)) {
            return super.getItemId(position);
        }
        return mAdapter.getItemId(getAdapterPosition(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (ITEM_VIEW_TYPE_HEADER == viewType) {
            return new SimpleViewHolder((View) mHeaderView);

        } else if (mHeaderList.contains(viewType)) {
            return new SimpleViewHolder(mHeaderList.getView(viewType));

        } else if (mFooterList.contains(viewType)) {
            return new SimpleViewHolder(mFooterList.getView(viewType));

        } else if (ITEM_VIEW_TYPE_FOOTER == viewType) {
            return new SimpleViewHolder((View) mFooterView);
        }

        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isReservedPosition(position)) {
            return;
        }
        mAdapter.onBindViewHolder(holder, getAdapterPosition(position));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (isReservedPosition(position)) {
            return;
        }
        mAdapter.onBindViewHolder(holder, getAdapterPosition(position), payloads);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridManager.getSpanSizeLookup();
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isReservedPosition(position)) {
                        return gridManager.getSpanCount();
                    }
                    return spanSizeLookup != null
                            ? spanSizeLookup.getSpanSize(getAdapterPosition(position)) : 1;
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
        final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams
                && isReservedPosition(holder.getLayoutPosition())) {
            ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
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