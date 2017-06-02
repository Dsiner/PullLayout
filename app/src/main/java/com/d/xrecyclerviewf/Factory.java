package com.d.xrecyclerviewf;

import android.os.Handler;

import com.d.xrecyclerviewf.model.Bean;
import com.d.lib.xrv.XRecyclerView;
import com.d.lib.xrv.adapter.CommonAdapter;

import java.util.ArrayList;

/**
 * Factory for create datas
 * Created by D on 2017/4/26.
 */
public class Factory {

    public static ArrayList<Bean> createMList() {
        ArrayList<Bean> datas = new ArrayList<>();

        Bean bean = new Bean(10, 0, "", "simple type list");
        datas.add(bean);

        bean = new Bean(10, 1, "", "multiple type list");
        datas.add(bean);

        bean = new Bean(10, 2, "", "simple type pull xlist");
        datas.add(bean);

        bean = new Bean(10, 3, "", "multiple type pull xlist");
        datas.add(bean);

        return datas;
    }

    public static ArrayList<Bean> createDatas() {
        ArrayList<Bean> datas = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            int type = i % 4;
            Bean bean = new Bean(type, i, "item_" + i, "mark_" + i);
            datas.add(bean);
        }
        return datas;
    }

    public static void onRefreshTest(final int refreshTime
            , final int times, final ArrayList<Bean> datas,
                                     final CommonAdapter adapter,
                                     final XRecyclerView recyclerView) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                datas.clear();
                for (int i = 0; i < 15; i++) {
                    int type = i % 4;
                    Bean bean = new Bean(type, i, "item_" + i + "after " + refreshTime + " times of refresh", "mark_" + i);
                    datas.add(bean);
                }
                adapter.notifyDataSetChanged();
                recyclerView.refreshComplete();
            }
        }, 1000);
    }

    public static void onLoadMoreTest(final int refreshTime
            , final int times, final ArrayList<Bean> datas,
                                      final CommonAdapter adapter,
                                      final XRecyclerView recyclerView) {
        if (times < 6) {
            if (times % 2 == 0) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        recyclerView.loadMoreError();
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            } else {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        for (int i = 0; i < 15; i++) {
                            int type = i % 4;
                            Bean bean = new Bean(type, i, "item_0" + datas.size(), "mark_" + i);
                            datas.add(bean);
                        }
                        recyclerView.loadMoreComplete();
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    for (int i = 0; i < 9; i++) {
                        int type = i % 4;
                        Bean bean = new Bean(type, i, "item_0" + datas.size(), "mark_" + i);
                        datas.add(bean);
                    }
                    recyclerView.setNoMore(true);
                    adapter.notifyDataSetChanged();
                }
            }, 1000);
        }
    }
}
