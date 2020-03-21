package com.d.lib.pulllayout.rv.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.d.lib.pulllayout.rv.itemtouchhelper.ItemTouchHelperViewHolder;

public class CommonHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    public final View mItemView;
    public final int mLayoutId;
    private final SparseArray<View> mViews;
    private ItemTouchHelperViewHolder mItemTouchListener;

    private CommonHolder(View itemView, int layoutId) {
        super(itemView);
        this.mItemView = itemView;
        this.mLayoutId = layoutId;
        this.mViews = new SparseArray<>();
    }

    @NonNull
    public static CommonHolder createViewHolder(View itemView, int layoutId) {
        return new CommonHolder(itemView, layoutId);
    }

    @NonNull
    public static CommonHolder createViewHolder(@NonNull Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new CommonHolder(itemView, layoutId);
    }

    public View itemView() {
        return mItemView;
    }

    /**
     * Finds the first descendant view with the given ID
     */
    public <T extends View> T getView(@IdRes int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mItemView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }

    /**
     * Sets the text to be displayed.
     */
    public CommonHolder setText(@IdRes int id, String text) {
        TextView view = getView(id);
        view.setText(text);
        return this;
    }

    public CommonHolder setEnable(@IdRes int id, boolean enable) {
        TextView view = getView(id);
        view.setEnabled(enable);
        return this;
    }

    public CommonHolder setChecked(@IdRes int id, boolean checked) {
        CompoundButton view = getView(id);
        view.setChecked(checked);
        return this;
    }

    /**
     * Set the visibility state of this view.
     */
    public CommonHolder setVisibility(@IdRes int id, int visibility) {
        View view = getView(id);
        view.setVisibility(visibility);
        return this;
    }

    /**
     * Register a callback to be invoked when this view is clicked.
     */
    public CommonHolder setOnClickListener(@IdRes int id, @Nullable View.OnClickListener listener) {
        View view = getView(id);
        view.setOnClickListener(listener);
        return this;
    }

    public CommonHolder setTag(@IdRes int id, Object tag) {
        View view = getView(id);
        view.setTag(tag);
        return this;
    }

    public Object getTag(@IdRes int id) {
        return getView(id).getTag();
    }

    /**
     * Sets a drawable as the content of this ImageView.
     */
    public CommonHolder setImageResource(@IdRes int id, int resId) {
        ImageView view = getView(id);
        view.setImageResource(resId);
        return this;
    }

    /**
     * Sets a Bitmap as the content of this ImageView.
     */
    public CommonHolder setImageBitmap(@IdRes int id, Bitmap bitmap) {
        ImageView view = getView(id);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Set the background to a given resource.
     */
    public CommonHolder setBackgroundResource(@IdRes int id, int res) {
        View view = getView(id);
        view.setBackgroundResource(res);
        return this;
    }

    /**
     * Sets the text color for all the states (normal, selected,
     * focused) to be this color.
     */
    public CommonHolder setTextColor(@IdRes int id, int res) {
        TextView view = getView(id);
        view.setTextColor(res);
        return this;
    }

    /**
     * 3-1: Just for ItemTouch (optional)
     */
    public void setOnItemTouchListener(ItemTouchHelperViewHolder listener) {
        this.mItemTouchListener = listener;
    }

    /**
     * 3-2: Just for ItemTouch
     */
    @Override
    public void onItemSelected() {
        if (mItemTouchListener != null) {
            mItemTouchListener.onItemSelected();
        }
    }

    /**
     * 3-3: Just for ItemTouch
     */
    @Override
    public void onItemClear() {
        if (mItemTouchListener != null) {
            mItemTouchListener.onItemClear();
        }
    }
}
