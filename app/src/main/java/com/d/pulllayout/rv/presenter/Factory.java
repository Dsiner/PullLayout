package com.d.pulllayout.rv.presenter;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.d.pulllayout.rv.model.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for create datas
 * Created by D on 2017/4/26.
 */
public class Factory {
    public static int PAGE_COUNT = 10;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static List<Bean> createDatas(int count) {
        List<Bean> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int type = i % 4;
            Bean bean = new Bean(type, i, "item_" + i, "mark_" + i);
            list.add(bean);
        }
        return list;
    }

    public static void onRefresh(final int refreshTime, final SimpleCallback<List<Bean>> callback) {
        final List<Bean> list = new ArrayList<>();
        for (int i = 0; i < PAGE_COUNT; i++) {
            int type = i % 4;
            Bean bean = new Bean(type, i, "item_" + i + " after " + refreshTime + " times of refresh", "mark_" + i);
            list.add(bean);
        }
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (callback != null) {
                    callback.onSuccess(list);
                }
            }
        }, 1000);
    }

    public static void onLoadMore(final int times, final int count, final SimpleCallback<List<Bean>> callback) {
        final List<Bean> list = new ArrayList<>();
        if (times < 6) {
            if (times % 2 == 0) {
                // Test type loadMoreError
                list.clear();
            } else {
                // Test type loadMoreComplete
                for (int i = 0; i < PAGE_COUNT; i++) {
                    int type = i % 4;
                    Bean bean = new Bean(type, i, "item_0" + (count + i), "mark_" + i);
                    list.add(bean);
                }
            }
        } else {
            // Test type noMore
            for (int i = 0; i < 6; i++) {
                int type = i % 4;
                Bean bean = new Bean(type, i, "item_0" + (count + i), "mark_" + i);
                list.add(bean);
            }
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(list);
                }
            }
        }, 1000);
    }

    public interface SimpleCallback<T> {
        void onSuccess(@NonNull T result);
    }
}
