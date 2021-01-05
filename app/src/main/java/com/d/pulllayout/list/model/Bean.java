package com.d.pulllayout.list.model;

import com.d.lib.pulllayout.loader.CommonLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean
 * Created by D on 2017/2/10.
 */
public class Bean {
    public static final int PAGE_MAX = 5;

    public static final int TYPE_SIMPLE = 0;
    public static final int TYPE_MULTIPLE = 1;
    public static final int TYPE_ITEM_TOUCH = 2;

    public int type;
    public int index;
    public String content;
    public String mark;

    public Bean(String content) {
        this.content = content;
    }

    public Bean(int type, int index, String content, String mark) {
        this.type = type;
        this.index = index;
        this.content = content;
        this.mark = mark;
    }

    public static List<Bean> create(int count) {
        List<Bean> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new Bean("" + i));
        }
        return list;
    }

    public static List<Bean> createMultiple(int page) {
        final List<Bean> list = new ArrayList<>();
        final int count = CommonLoader.PAGE_COUNT;
        for (int i = 0; i < count; i++) {
            final int type = i % 4;
            final int index = count * (page - 1) + i;
            Bean bean = new Bean(type, index, "" + index, "mark");
            list.add(bean);
        }
        return list;
    }
}
