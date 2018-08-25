package com.d.xrv.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.lib.xrv.itemtouchhelper.ItemTouchHelperViewHolder;
import com.d.xrv.R;
import com.d.xrv.model.Bean;

import java.util.Collections;
import java.util.List;

/**
 * ItemTouchAdapter
 * Created by D on 2017/6/3.
 */
public class ItemTouchAdapter extends CommonAdapter<Bean> {
    private int colorSelected;
    private boolean isLinear = true;

    public ItemTouchAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
        colorSelected = Color.parseColor("#33333333");
    }

    public void toggle(boolean isLinear) {
        this.isLinear = isLinear;
        notifyDataSetChanged();
    }

    @Override
    public void convert(int position, final CommonHolder holder, Bean item) {
        if (isLinear) {
            holder.setViewVisibility(R.id.llyt_style0, View.VISIBLE);
            holder.setViewVisibility(R.id.v_style0, View.VISIBLE);
            holder.setViewVisibility(R.id.tv_style1, View.GONE);
            linearConvert(holder, item);
        } else {
            holder.setViewVisibility(R.id.llyt_style0, View.GONE);
            holder.setViewVisibility(R.id.v_style0, View.GONE);
            holder.setViewVisibility(R.id.tv_style1, View.VISIBLE);
            gridConvert(holder, item);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void gridConvert(final CommonHolder holder, Bean item) {
        final TextView tvHandler = holder.getView(R.id.tv_style1);
        tvHandler.setText(item.content);
        tvHandler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && getItemCount() > 1 && startDragListener != null) {
                    // Step 9-5: 只有调用onStartDrag才会触发拖拽 (这里在touch时开始拖拽，当然也可以单击或长按时才开始拖拽)
                    startDragListener.onStartDrag(holder);
                    return true;
                }
                return false;
            }
        });
        // Step 9-7: 设置ItemTouchListener
        holder.setOnItemTouchListener(new ItemTouchHelperViewHolder() {
            @Override
            public void onItemSelected() {
                // 触发拖拽时回调
                tvHandler.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.corner_bg_touch_select));
            }

            @Override
            public void onItemClear() {
                // 手指松开时回调
                tvHandler.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.corner_bg_touch_normal));
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void linearConvert(final CommonHolder holder, Bean item) {
        holder.setText(R.id.tv_style0, item.content);
        ImageView ivHandler = holder.getView(R.id.iv_style0_handler);
        ivHandler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && getItemCount() > 1 && startDragListener != null) {
                    // Step 9-5: 只有调用onStartDrag才会触发拖拽 (这里在touch时开始拖拽，当然也可以单击或长按时才开始拖拽)
                    startDragListener.onStartDrag(holder);
                    return true;
                }
                return false;
            }
        });
        // Step 9-7: 设置ItemTouchListener
        holder.setOnItemTouchListener(new ItemTouchHelperViewHolder() {
            @Override
            public void onItemSelected() {
                // 触发拖拽时回调
                holder.itemView.setBackgroundColor(colorSelected);
            }

            @Override
            public void onItemClear() {
                // 手指松开时回调
                holder.itemView.setBackgroundColor(0);
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        // Step 9-8: 回调, Item被删除
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        // Step 9-9: 回调, 两个Item之间交换位置
        if (Math.abs(fromPosition - toPosition) > 1) {
            Bean from = mDatas.get(fromPosition);
            mDatas.remove(fromPosition);
            mDatas.add(toPosition, from);
        } else {
            Collections.swap(mDatas, fromPosition, toPosition);
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
