package com.d.lib.refreshlayout.refresh.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.d.lib.refreshlayout.utils.Util;

/**
 * Error
 * Created by D on 2018/3/30.
 */
public class Error extends Success {

    public Error(View view) {
        super(view);
    }

    @Override
    protected void initAttr(Context context) {
        sign = new Discon(view);
        content = "网络无法连接";

        paintText.setColor(colorWhite);
        paintText.setTextSize(Util.dip2px(context, 14));
        paintText.setTextAlign(Paint.Align.CENTER);
        textHeight = Util.getTextHeight(paintText);

        paint.setColor(colorError);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
