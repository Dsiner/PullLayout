package com.d.lib.refreshlayout.refresh.state;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.d.lib.refreshlayout.utils.Util;

import java.lang.ref.WeakReference;

/**
 * Success
 * Created by D on 2018/3/30.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Success extends State {
    protected String content;

    protected State sign;
    protected Paint paint;
    protected Paint paintText;
    protected float textHeight;
    private Rect rect;
    private RectF rectF;
    private float rectRadius;

    private ValueAnimator animator;
    private float factor;
    private boolean taskRunning;
    private int step;
    private Task task;
    private Handler handler;

    static class AnimListenerAdapter extends AnimatorListenerAdapter {
        private final WeakReference<Success> reference;

        AnimListenerAdapter(Success view) {
            this.reference = new WeakReference<Success>(view);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            Success view = reference.get();
            if (view == null || view.context == null
                    || view.context instanceof Activity && ((Activity) view.context).isFinishing()) {
                return;
            }
            view.step = 1;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            Success view = reference.get();
            if (view == null || view.context == null
                    || view.context instanceof Activity && ((Activity) view.context).isFinishing()) {
                return;
            }
            view.step = 0;
        }
    }

    static class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final WeakReference<Success> reference;

        AnimUpdateListener(Success view) {
            this.reference = new WeakReference<Success>(view);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Success view = reference.get();
            if (view == null || view.context == null
                    || view.context instanceof Activity && ((Activity) view.context).isFinishing()
                    || view.view == null) {
                return;
            }
            view.step = 1;
            view.factor = (float) animation.getAnimatedValue();
            view.view.postInvalidate();
        }
    }

    static class Task implements Runnable {

        private final WeakReference<Success> reference;

        Task(Success view) {
            this.reference = new WeakReference<Success>(view);
        }

        @Override
        public void run() {
            Success view = reference.get();
            if (view == null || view.context == null
                    || view.context instanceof Activity && ((Activity) view.context).isFinishing()
                    || !view.taskRunning) {
                return;
            }
            view.animator.start();
        }
    }

    @Override
    public void reStart() {
        stop();
        taskRunning = true;
        step = 0;
        view.invalidate();
        handler.postDelayed(task, 700);
    }

    @Override
    public void stop() {
        animator.cancel();
        taskRunning = false;
        step = 0;
        handler.removeCallbacks(task);
    }

    public Success(View view) {
        super(view);
        init(context);
        initAttr(context);
    }

    private void init(Context context) {
        task = new Task(this);
        handler = new Handler();

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        rect = new Rect();
        rectF = new RectF();

        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new AnimListenerAdapter(this));
        animator.addUpdateListener(new AnimUpdateListener(this));
    }

    protected void initAttr(Context context) {
        sign = new Done(view);
        content = "刷新成功";

        paintText.setColor(colorWhite);
        paintText.setTextSize(Util.dip2px(context, 14));
        paintText.setTextAlign(Paint.Align.CENTER);
        textHeight = Util.getTextHeight(paintText);

        paint.setColor(color);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (step == 0) {
            sign.onDraw(canvas);
            return;
        }
        float offset = width / 2f * factor;
        float radius = rectRadius * (1 - factor);
        rect.set((int) (width / 2f - offset), 0, (int) (width / 2f + offset), height);
        rectF.set(rect);
        canvas.drawRoundRect(rectF, radius, radius, paint);//在原有矩形基础上，画成圆角矩形

        canvas.drawText(content, width / 2f, height / 2 + textHeight / 2, paintText);
    }

    @Override
    public void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        super.setMeasuredDimension(measuredWidth, measuredHeight);
        sign.setMeasuredDimension(measuredWidth, measuredHeight);
        rectRadius = height / 3f;
    }
}
