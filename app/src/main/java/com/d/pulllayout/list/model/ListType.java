package com.d.pulllayout.list.model;

import com.d.lib.common.view.popup.MenuPopup;
import com.d.pulllayout.R;

import java.util.Arrays;
import java.util.List;

public class ListType {
    public static final int PULLRECYCLERLAYOUT_PULLRECYCLERVIEW = 0;
    public static final int PULLRECYCLERLAYOUT_RECYCLERVIEW = 1;
    public static final int PULLRECYCLERLAYOUT_LISTVIEW = 2;
    public static final int PULLRECYCLERVIEW = 3;

    public static int[] S_RES_IDS = new int[]{R.layout.lib_pub_fragment_abs,
            R.layout.fragment_list_pullrecyclerlayout_recyclerview,
            R.layout.fragment_list_pullrecyclerlayout_listview,
            R.layout.fragment_list_pullrecyclerview};

    public static List<MenuPopup.Bean> getTypeBeans(int type) {
        return Arrays.asList(
                new MenuPopup.Bean("PullRecyclerLayout\n(PullRecyclerView)",
                        type == ListType.PULLRECYCLERLAYOUT_PULLRECYCLERVIEW ?
                                R.color.lib_pub_color_main
                                : R.color.lib_pub_color_white, false),
                new MenuPopup.Bean("PullRecyclerLayout\n(RecyclerView)",
                        type == ListType.PULLRECYCLERLAYOUT_RECYCLERVIEW ?
                                R.color.lib_pub_color_main
                                : R.color.lib_pub_color_white, false),
                new MenuPopup.Bean("PullRecyclerLayout\n(ListView)",
                        type == ListType.PULLRECYCLERLAYOUT_LISTVIEW ?
                                R.color.lib_pub_color_main
                                : R.color.lib_pub_color_white, false),
                new MenuPopup.Bean("PullRecyclerView",
                        type == ListType.PULLRECYCLERVIEW ?
                                R.color.lib_pub_color_main
                                : R.color.lib_pub_color_white, false));
    }
}
