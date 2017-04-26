package com.d.xrv.view;

/**
 * Created by jianghejie on 15/11/22.
 */
public interface BaseRefreshHeader {

    void onMove(float delta);

    boolean releaseAction();

    void refreshComplete();
}