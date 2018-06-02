package com.d.lib.refreshlayout.refresh.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;

import com.d.lib.refreshlayout.utils.Util;

/**
 * Done
 * Created by D on 2018/3/30.
 */
public class Done extends State {
    float scaleAX = 0.2659f;
    float scaleAY = 0.4588f;
    float scaleBX = 0.4541f;
    float scaleBY = 0.6306f;
    float scaleCX = 0.7553f;
    float scaleCY = 0.3388f;

    private float strokeWidth;
    private Path path;
    private Path pathTick;
    private Paint paintTick;
    private Paint paintCircle;
    private PathMeasure tickPathMeasure;

    public Done(View view) {
        super(view);
        init(context);
    }

    private void init(Context context) {
        strokeWidth = Util.dip2px(context, 2.5f);

        path = new Path();
        pathTick = new Path();

        tickPathMeasure = new PathMeasure();

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(color);

        paintTick = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTick.setColor(colorWhite);
        paintTick.setStrokeCap(Paint.Cap.ROUND);
        paintTick.setStyle(Paint.Style.STROKE);
        paintTick.setStrokeWidth(strokeWidth);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float h = height * 0.35f;
        float w = h;
        float startX = (width - w) / 2;
        float startY = (height - h) / 2;
        pathTick.moveTo(startX + w * scaleAX, startY + h * scaleAY);
        pathTick.lineTo(startX + w * scaleBX, startY + h * scaleBY);
        pathTick.lineTo(startX + w * scaleCX, startY + h * scaleCY);
        tickPathMeasure.setPath(pathTick, false);
        /*
         * On KITKAT and earlier releases, the resulting path may not display on a hardware-accelerated Canvas.
         * A simple workaround is to add a single operation to this path, such as dst.rLineTo(0, 0).
         */
        tickPathMeasure.getSegment(0, tickPathMeasure.getLength(), path, true);
        path.rLineTo(0, 0);
        canvas.drawCircle(width / 2f, height / 2f, h / 2f, paintCircle);
        canvas.drawPath(path, paintTick);
    }
}
