package com.d.lib.pulllayout.edge;

import android.animation.TimeInterpolator;

public interface INestedAnim {
    void setDuration(int duration);

    void setInterpolator(TimeInterpolator value);

    void startNestedAnim(int startX, int startY, int destX, int destY);

    boolean stopNestedAnim();
}
