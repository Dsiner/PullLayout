package com.d.xrv.presenter;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.d.xrv.model.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for create datas
 * Created by D on 2017/4/26.
 */
public class Factory {
    public static int PAGE_COUNT = 10;
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static List<Bean> createList() {
        List<Bean> datas = new ArrayList<>();

        Bean bean = new Bean(10, 0, "", "Simple Type List");
        datas.add(bean);

        bean = new Bean(10, 1, "", "Multiple Type List");
        datas.add(bean);

        bean = new Bean(10, 2, "", "Simple Type Pull Xlist");
        datas.add(bean);

        bean = new Bean(10, 3, "", "Multiple Type Pull Xlist");
        datas.add(bean);

        bean = new Bean(10, 4, "", "Item Touch");
        datas.add(bean);

        return datas;
    }

    public static List<Bean> createDatas(int count) {
        List<Bean> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int type = i % 4;
            Bean bean = new Bean(type, i, "item_" + i, "mark_" + i);
            datas.add(bean);
        }
        return datas;
    }

    public static void onRefreshTest(final int refreshTime, final SimpleCallback<List<Bean>> callback) {
        final List<Bean> list = new ArrayList<>();
        for (int i = 0; i < PAGE_COUNT; i++) {
            int type = i % 4;
            Bean bean = new Bean(type, i, "item_" + i + " after " + refreshTime + " times of refresh", "mark_" + i);
            list.add(bean);
        }
        handler.postDelayed(new Runnable() {
            public void run() {
                if (callback != null) {
                    callback.onSuccess(list);
                }
            }
        }, 1000);
    }

    public static void onLoadMoreTest(final int times, final int count, final SimpleCallback<List<Bean>> callback) {
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

        handler.postDelayed(new Runnable() {
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
