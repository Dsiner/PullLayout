package com.d.lib.pulllayout.edge;

/**
 * IState
 * Created by D on 2018/3/30.
 */
public interface IState {
    int STATE_NONE = 0;
    int STATE_EXPANDED = 1;
    int STATE_LOADING = 2;
    int STATE_SUCCESS = 3;
    int STATE_ERROR = 4;
    int STATE_NO_MORE = 5;

    boolean setState(int state);

    int getState();
}
