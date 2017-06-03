package com.d.xrecyclerviewf.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.lib.xrv.itemtouchhelper.ItemTouchHelperViewHolder;
import com.d.xrecyclerviewf.R;
import com.d.xrecyclerviewf.model.Bean;

import java.util.Collections;
import java.util.List;

/**
 * ItemTouchAdapter
 * Created by D on 2017/6/3.
 */
public class ItemTouchAdapter extends CommonAdapter<Bean> {
    private int colorSelected;

    public ItemTouchAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
        colorSelected = Color.parseColor("#99CCCCCC");
    }

    @Override
    public void convert(int position, final CommonHolder holder, Bean item) {
        holder.setText(R.id.tv_des, item.name);
        ImageView ivHandler = holder.getView(R.id.iv_handler);
        ivHandler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && getItemCount() > 1 && startDragListener != null) {
                    // step9-5:只有调用onStartDrag才会触发拖拽 (这里在touch时开始拖拽，当然也可以单击或长按时才开始拖拽)
                    startDragListener.onStartDrag(holder);
                    return true;
                }
                return false;
            }
        });
        //step9-7:设置ItemTouchListener
        holder.setOnItemTouchListener(new ItemTouchHelperViewHolder() {
            @Override
            public void onItemSelected() {
                //触发拖拽时回调
                holder.itemView.setBackgroundColor(colorSelected);
            }

            @Override
            public void onItemClear() {
                //手指松开时回调
                holder.itemView.setBackgroundColor(0);
            }
        });

    }

    @Override
    public void onItemDismiss(int position) {
        //9-8:回调、item被删除
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //9-9:回调、两个item之间交换位置
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
