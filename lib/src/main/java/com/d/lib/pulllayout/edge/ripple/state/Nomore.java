package com.d.lib.pulllayout.edge.ripple.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.d.lib.pulllayout.util.Utils;

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
        mSign = new Done(mView);
        mContent = "没有更多了";

        mPaintText.setColor(mColorWhite);
        mPaintText.setTextSize(Utils.dp2px(context, 14));
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mTextHeight = Utils.getTextHeight(mPaintText);

        mPaint.setColor(mColor);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
