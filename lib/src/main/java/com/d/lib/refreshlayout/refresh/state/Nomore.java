package com.d.lib.refreshlayout.refresh.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.d.lib.refreshlayout.utils.Util;

/**
 * Nomore
 * Created by D on 2018/3/30.
 */
public class Nomore extends Success {

    public Nomore(View view) {
        super(view);
    }

    @Override
    protected void initAttr(Context context) {
        sign = new Done(view);
        content = "没有更多了";

        paintText.setColor(colorWhite);
        paintText.setTextSize(Util.dip2px(context, 14));
        paintText.setTextAlign(Paint.Align.CENTER);
        textHeight = Util.getTextHeight(paintText);

        paint.setColor(color);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
