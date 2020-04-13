package com.d.pulllayout.rv.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;
import com.d.lib.pulllayout.rv.itemtouchhelper.ItemTouchHelperViewHolder;
import com.d.pulllayout.R;
import com.d.pulllayout.rv.model.Bean;

import java.util.Collections;
import java.util.List;

/**
 * ItemTouchLinearAdapter
 * Created by D on 2017/6/3.
 */
public class ItemTouchLinearAdapter extends CommonAdapter<Bean> {
    private int mColorSelected;

    public ItemTouchLinearAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
        mColorSelected = Color.parseColor("#33333333");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void convert(int position, final CommonHolder holder, Bean item) {
        holder.setText(R.id.tv_content, item.content);
        ImageView ivHandler = holder.getView(R.id.iv_handler);
        ivHandler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN
                        && getItemCount() > 1 && mStartDragListener != null) {
                    // Step 3-3: Only calling onStartDrag will trigger the drag,
                    // here the drag will start when touched,
                    // of course, you can also click or long press to start
                    mStartDragListener.onStartDrag(holder);
                    return true;
                }
                return false;
            }
        });
        holder.setOnItemTouchListener(new ItemTouchHelperViewHolder() {
            @Override
            public void onItemSelected() {
                // Callback when dragging is triggered
                holder.itemView.setBackgroundColor(mColorSelected);
            }

            @Override
            public void onItemClear() {
                // Callback when finger is released
                holder.itemView.setBackgroundColor(0);
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
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
