package com.d.pulllayout.list.model;

import android.content.Context;

import com.d.lib.common.view.popup.MenuPopup;
import com.d.lib.pulllayout.edge.IEdgeView;
import com.d.pulllayout.R;

import java.util.Arrays;
import java.util.List;

public class EdgeType {
    public static final int TYPE_CLASSIC = 0;
    public static final int TYPE_RIPPLE = 1;
    public static final int TYPE_ELASTIC = 2;

    public static List<MenuPopup.Bean> getMenus(int type) {
        return Arrays.asList(
                new MenuPopup.Bean("Classic",
                        type == EdgeType.TYPE_CLASSIC ?
                                R.color.lib_pub_color_main
                                : R.color.lib_pub_color_white, false),
                new MenuPopup.Bean("Ripple",
                        type == EdgeType.TYPE_RIPPLE ?
                                R.color.lib_pub_color_main
                                : R.color.lib_pub_color_white, false),
                new MenuPopup.Bean("Elastic",
                        type == EdgeType.TYPE_ELASTIC ?
                                R.color.lib_pub_color_main
                                : R.color.lib_pub_color_white, false));
    }

    public static String getTypeTitle(int type) {
        if (TYPE_CLASSIC == type) {
            return "Classic";
        } else if (TYPE_RIPPLE == type) {
            return "Ripple";
        } else if (TYPE_ELASTIC == type) {
            return "Elastic";
        } else {
            return "";
        }
    }

    public static IEdgeView[] getEdgeView(Context context, int type) {
        if (TYPE_CLASSIC == type) {
            return new IEdgeView[]{
                    new com.d.lib.pulllayout.edge.arrow.HeaderView(context),
                    new com.d.lib.pulllayout.edge.arrow.FooterView(context)
            };
        } else if (TYPE_RIPPLE == type) {
            return new IEdgeView[]{
                    new com.d.lib.pulllayout.edge.ripple.HeaderView(context),
                    new com.d.lib.pulllayout.edge.ripple.FooterView(context)
            };
        } else if (TYPE_ELASTIC == type) {
            return new IEdgeView[]{
                    new com.d.lib.pulllayout.edge.elastic.HeaderView(context),
                    new com.d.lib.pulllayout.edge.arrow.FooterView(context)
            };
        } else {
            return new IEdgeView[]{
                    new com.d.lib.pulllayout.edge.arrow.HeaderView(context),
                    new com.d.lib.pulllayout.edge.arrow.FooterView(context)
            };
        }
    }
}
