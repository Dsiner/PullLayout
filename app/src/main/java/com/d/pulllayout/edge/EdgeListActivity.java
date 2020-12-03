package com.d.pulllayout.edge;

import android.view.View;
import android.widget.PopupWindow;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.common.view.popup.MenuPopup;
import com.d.lib.common.view.popup.PopupWindowFactory;
import com.d.pulllayout.R;
import com.d.pulllayout.list.activity.ListActivity;
import com.d.pulllayout.list.model.EdgeType;
import com.d.pulllayout.list.model.ListType;

public class EdgeListActivity extends ListActivity {

    @Override
    protected void onMore(final int listType) {
        MenuPopup menuPopup = PopupWindowFactory.createFactory(this)
                .getMenuPopup(EdgeType.getMenus(mEdgeType),
                        new MenuPopup.OnMenuListener() {
                            @Override
                            public void onClick(PopupWindow popup, int position, String item) {
                                if (mEdgeType == position) {
                                    return;
                                }
                                mEdgeType = position;
                                replace(mType, mListType, mEdgeType);
                            }
                        });
        menuPopup.showAsDropDown((View) ViewHelper.findViewById(this, R.id.iv_title_right));
    }

    @Override
    public void replace(int type, int listType, int edgeType) {
        listType = ListType.PULLRECYCLERLAYOUT_PULLRECYCLERVIEW;
        super.replace(type, listType, edgeType);
        final String title = EdgeType.getTypeTitle(edgeType);
        tl_title.setText(R.id.tv_title_title, title);
    }
}
