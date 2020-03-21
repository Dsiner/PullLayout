package com.d.lib.pulllayout.edge.ripple.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.d.lib.pulllayout.util.Utils;

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
        mSign = new Discon(mView);
        mContent = "网络无法连接";

        mPaintText.setColor(mColorWhite);
        mPaintText.setTextSize(Utils.dp2px(context, 14));
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mTextHeight = Utils.getTextHeight(mPaintText);

        mPaint.setColor(mColorError);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
