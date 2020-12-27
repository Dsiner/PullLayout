package com.d.pulllayout.list.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.d.lib.common.component.loader.MvpBaseLoaderPresenter;
import com.d.lib.common.event.bus.callback.SimpleCallback;
import com.d.pulllayout.list.model.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * LoadPresenter
 * Created by D on 2017/4/26.
 */
public class LoadPresenter extends MvpBaseLoaderPresenter<Bean> {

    public LoadPresenter(Context context) {
        super(context);
    }

    public void get(int page) {
        ApiClient.getInstance().request(page, new SimpleCallback<List<Bean>>() {
            @Override
            public void onSuccess(List<Bean> response) {
                if (getView() == null) {
                    return;
                }
                getView().loadSuccess(response);
            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null) {
                    return;
                }
                getView().loadError();
            }
        });
    }

    static class ApiClient {
        private static final int TIMEOUT = 1 * 1000;
        private volatile static ApiClient INSTANCE;
        private static int LAST_PAGE;
        private Handler mHandler = new Handler(Looper.getMainLooper());

        private ApiClient() {
        }

        public static ApiClient getInstance() {
            if (INSTANCE == null) {
                synchronized (ApiClient.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ApiClient();
                    }
                }
            }
            return INSTANCE;
        }

        public void request(final int page, final SimpleCallback<List<Bean>> callback) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (page > Bean.PAGE_MAX) {
                        // No more
                        callback.onSuccess(new ArrayList<Bean>());
                        return;
                    }
                    if (page > 2 && page < Bean.PAGE_MAX && page % 2 == 1) {
                        if (LAST_PAGE == page) {
                            // Retry
                            callback.onSuccess(Bean.createMultiple(page));
                        } else {
                            callback.onError(new Exception("Error"));
                        }
                    } else {
                        callback.onSuccess(Bean.createMultiple(page));
                    }
                    LAST_PAGE = page;
                }
            }, TIMEOUT);
        }
    }
}
