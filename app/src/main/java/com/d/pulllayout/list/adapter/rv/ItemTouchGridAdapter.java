package com.d.pulllayout.list.adapter.rv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;
import com.d.lib.pulllayout.rv.itemtouchhelper.ItemTouchHelperViewHolder;
import com.d.pulllayout.R;
import com.d.pulllayout.list.model.Bean;

import java.util.Collections;
import java.util.List;

/**
 * ItemTouchGridAdapter
 * Created by D on 2017/6/3.
 */
public class ItemTouchGridAdapter extends CommonAdapter<Bean> {

    public ItemTouchGridAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void convert(int position, final CommonHolder holder, Bean item) {
        final TextView tvHandler = holder.getView(R.id.tv_content);
        tvHandler.setText(item.content);
        tvHandler.setOnTouchListener(new View.OnTouchListener() {
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
        // Step 9-7: Set ItemTouchListener
        holder.setOnItemTouchListener(new ItemTouchHelperViewHolder() {
            @Override
            public void onItemSelected() {
                // Callback when dragging is triggered
                tvHandler.setBackgroundDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.corner_bg_touch_select));
            }

            @Override
            public void onItemClear() {
                // Callback when finger is released
                tvHandler.setBackgroundDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.corner_bg_touch_normal));
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
