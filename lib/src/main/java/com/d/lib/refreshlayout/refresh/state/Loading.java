package com.d.lib.refreshlayout.refresh.state;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.d.lib.refreshlayout.utils.Util;

import java.lang.ref.WeakReference;

/**
 * Loading
 * Created by D on 2018/3/30.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Loading extends State {
    private Paint paint;
    private Paint paintCircle;

    private Rect rect;
    private RectF rectF;
    private int space;

    private ValueAnimator animator;
    private float factor;
    private boolean taskRunning;

    static class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final WeakReference<Loading> reference;

        AnimUpdateListener(Loading view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Loading view = reference.get();
            if (view == null || view.context == null
                    || view.context instanceof Activity && ((Activity) view.context).isFinishing()
                    || view.view == null) {
                return;
            }
            view.factor = (float) animation.getAnimatedValue();
            view.view.invalidate();
        }
    }

    @Override
    public void reStart() {
        stop();
        taskRunning = true;
        animator.start();
    }

    @Override
    public void stop() {
        animator.cancel();
        taskRunning = false;
    }

    public Loading(View view) {
        super(view);
        init(context);
    }

    private void init(Context context) {
        space = Util.dip2px(context, 2.5f);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colorWhite);
        paint.setStrokeWidth(Util.dip2px(context, 2));
        paint.setStyle(Paint.Style.STROKE);

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(color);

        rect = new Rect();
        rectF = new RectF();

        animator = ValueAnimator.ofFloat(225f, 585f);
        animator.setDuration(1000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new AnimUpdateListener(this));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float h = height * 0.35f;
        float w = h;
        float startX = (width - w) / 2;
        float startY = (height - h) / 2;

        canvas.drawCircle(width / 2f, height / 2f, h / 2f, paintCircle);

        rect.set((int) startX + space, (int) startY + space, (int) (startX + w) - space, (int) (startY + h) - space);
        rectF.set(rect);
        canvas.drawArc(rectF, factor, 90, false, paint);
    }
}
