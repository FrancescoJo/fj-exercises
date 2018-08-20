/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.util.view.ViewUtils;
import com.fj.android.mediamonkey.widget.GridSpacingItemDecoration;
import com.mediamonkey.android.lib.annotation.DisplayType;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Dec - 2016
 */
final class MainUiHelper {
    private static final int GRID_ITEM_COUNT = 2;
    private static final int GRID_SPACING    = ViewUtils.dp2Px(8.0f);

    @SuppressLint("SwitchIntDef")
    static RecyclerView createRootView(final Context context, final @DisplayType int displayType) {
        RecyclerView view;

        switch (displayType) {
            case DisplayType.LIST:
            case DisplayType.GRID:
            default:
                view = new RecyclerView(context);
        }

        view.setBackgroundResource(R.color.grey_ee);
        return view;
    }

    @SuppressLint("SwitchIntDef")
    static RecyclerView.LayoutManager createLayoutManager(final RecyclerView rootView,
                                                          final @DisplayType int displayType) {
        Context context = rootView.getContext();

        switch (displayType) {
            default:
            case DisplayType.LIST: {
                LinearLayoutManager llm = new LinearLayoutManager(context);
                DividerItemDecoration divider = new DividerItemDecoration(context, llm.getOrientation());
                rootView.addItemDecoration(divider);
                return llm;
            }
            case DisplayType.GRID: {
                GridLayoutManager glm = new GridLayoutManager(context, GRID_ITEM_COUNT);
                GridSpacingItemDecoration dec = new GridSpacingItemDecoration(GRID_ITEM_COUNT, GRID_SPACING, true);
                rootView.addItemDecoration(dec);
                return glm;
            }
        }
    }

    static boolean isLastItemShowing(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager llMgr = recyclerView.getLayoutManager();
        int lastVisiblePosition;
        if (llMgr instanceof GridLayoutManager) {
            lastVisiblePosition = ((GridLayoutManager) llMgr).findLastVisibleItemPosition();
        } else if (llMgr instanceof LinearLayoutManager) {
            lastVisiblePosition = ((LinearLayoutManager) llMgr).findLastVisibleItemPosition();
        } else if (llMgr instanceof StaggeredGridLayoutManager) {
            int[] lastVisiblePositions = ((StaggeredGridLayoutManager) llMgr).findLastVisibleItemPositions(null);
            int maximum = lastVisiblePositions[0];
            if (lastVisiblePositions.length > 1) {
                for (int i = 1; i < lastVisiblePositions.length; i++) {
                    if (lastVisiblePositions[i] > maximum) {
                        maximum = lastVisiblePositions[i];
                    }
                }
            }
            lastVisiblePosition = maximum;
        } else {
            throw new UnsupportedOperationException(recyclerView + " is using an unsupported " +
                    "layout manager: " + llMgr);
        }

        if (lastVisiblePosition == RecyclerView.NO_POSITION) {
            return false;
        }

        int itemCount = recyclerView.getAdapter().getItemCount();
        // -1 is logically right but -2 even in LinearLayoutManager
        return lastVisiblePosition >= itemCount - 2;
    }

    private MainUiHelper() { }
}
