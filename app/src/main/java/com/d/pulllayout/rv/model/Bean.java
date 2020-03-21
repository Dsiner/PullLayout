package com.d.pulllayout.rv.model;

/**
 * Bean
 * Created by D on 2017/2/10.
 */
public class Bean {
    public static final int TYPE_SIMPLE = 0;
    public static final int TYPE_MULTIPLE = 1;
    public static final int TYPE_ITEM_TOUCH = 2;

    public int type;
    public int index;
    public String content;
    public String mark;

    public Bean(int type, int index, String content, String mark) {
        this.type = type;
        this.index = index;
        this.content = content;
        this.mark = mark;
    }
}
