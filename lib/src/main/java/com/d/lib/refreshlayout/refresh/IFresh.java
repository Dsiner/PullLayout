package com.d.lib.refreshlayout.refresh;

/**
 * Fresher
 * Created by D on 2018/3/30.
 */
public interface IFresh {
    int STATE_DONE = 0;
    int STATE_LOADING = 1;
    int STATE_SUCCESS = 2;
    int STATE_ERROR = 3;
    int STATE_NOMORE = 4;

    void setState(int state);
}
