package com.d.pulllayout.list.model;

import com.d.lib.common.view.popup.MenuPopup;
import com.d.pulllayout.R;

import java.util.Arrays;
import java.util.List;

public class EdgeType {
    public static final int TYPE_CLASSIC = 0;
    public static final int TYPE_RIPPLE = 1;

    public static List<MenuPopup.Bean> getMenus(int type) {
        return Arrays.asList(
                new MenuPopup.Bean("Classic",
                        type == EdgeType.TYPE_CLASSIC ?
                                R.color.lib_pub_color_main
                                : R.color.lib_pub_color_white, false),
                new MenuPopup.Bean("Ripple",
                        type == EdgeType.TYPE_RIPPLE ?
                                R.color.lib_pub_color_main
                                : R.color.lib_pub_color_white, false));
    }

    public static String getTypeTitle(int type) {
        if (TYPE_CLASSIC == type) {
            return "Classic";
        } else if (TYPE_RIPPLE == type) {
            return "Ripple";
        } else {
            return "";
        }
    }
}
