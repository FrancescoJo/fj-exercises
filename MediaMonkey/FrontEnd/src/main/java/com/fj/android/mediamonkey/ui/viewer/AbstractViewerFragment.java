/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.viewer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.dao.sharedpref.AppConfigPref;
import com.fj.android.mediamonkey.ui._base.AbstractBaseFragment;
import com.mediamonkey.android.lib.annotation.ContentDisplayType;

/**
 * As declared public due to Fragment's restriction,
 * this Fragment and its subclasses cannot be used outside of viewer.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jan - 2017
 */
public abstract class AbstractViewerFragment extends AbstractBaseFragment {
    private static final long SYS_UI_AUTOHIDE_DELAY = 5000L;

    @NonNull
    protected abstract View getInflatedView(final LayoutInflater inflater,
                                            final ViewGroup container, final Bundle savedInstanceState);

    private Handler uiHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.uiHandler = new Handler();
    }

    @Nullable
    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                   final Bundle savedInstanceState) {
        ViewerOverlayLayout contentView = (ViewerOverlayLayout)
                inflater.inflate(R.layout.fragment_viewer_overlay, container, false);
        contentView.setOnScrollListener(new ViewerOverlayLayout.OnTouchStartListener() {
            @Override
            public void onTouchStart(final MotionEvent ev) {
                if (getViewerView().isSysUiShowing()) {
                    uiHandler.removeCallbacks(hideSysUiCallback);
                    uiHandler.postDelayed(hideSysUiCallback, SYS_UI_AUTOHIDE_DELAY);
                }
            }
        });
        contentView.setOnSingleTapListener(new ViewerOverlayLayout.SingleTapListener() {
            @Override
            public boolean onSingleTap(final MotionEvent e) {
                if (onContentViewSingleTap(e)) {
                    return true;
                }

                uiHandler.removeCallbacks(hideSysUiCallback);
                getViewerView().toggleSysUi();
                if (getViewerView().isSysUiShowing()) {
                    uiHandler.postDelayed(hideSysUiCallback, SYS_UI_AUTOHIDE_DELAY);
                }

                return false;
            }
        });
        contentView.addView(getInflatedView(inflater, container, savedInstanceState));
        return contentView;
    }

    @Override
    public void onStart() {
        ViewerView viewerView = getViewerView();
        if (viewerView.isSysUiShowing()) {
            viewerView.toggleSysUi();
        }

        super.onStart();
    }

    @Override
    public void onPause() {
        ViewerView viewerView = getViewerView();
        if (!viewerView.isSysUiShowing()) {
            viewerView.toggleSysUi();
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        uiHandler.removeCallbacks(hideSysUiCallback);
        super.onDestroy();
    }

    /**
     * This method must be called after {@link android.app.Fragment#onActivityCreated(Bundle)}.
     */
    final ViewerView getViewerView() {
        Activity activity = getActivity();
        if (!(activity instanceof ViewerView)) {
            throw new IllegalStateException("Attached Activity class " + activity +
                    " is not implementing " + ViewerView.class.getCanonicalName() + ".");
        }

        return (ViewerView) activity;
    }

    /**
     * Called when single tap event has been occurred on contents overlay view.
     *
     * @param event Low-level touch event created by framework
     * @return <code>true</code> if click/tap event is handled by Viewer itself,
     * <code>false</code> otherwise.
     */
    // Parameter is designed for inheritance
    protected boolean onContentViewSingleTap(@SuppressWarnings("UnusedParameters") final MotionEvent event) {
        return false;
    }

    @SuppressLint("SwitchIntDef")
    static AbstractViewerFragment newInstance() {
        AppConfigPref appConfigPref = MediaMonkeyApplication.getInstance()
                .getAppComponent().sharedPrefMgr().getAppConfig();

        String className;
        switch (appConfigPref.getViewerMode()) {
            case ContentDisplayType.VERTICAL:
                className = VerticalViewerFragment.class.getCanonicalName();
                break;
            case ContentDisplayType.VERTICAL_PAGING:
                className = VerticalViewerFragment.class.getCanonicalName();
                break;
            case ContentDisplayType.HORIZONTAL:
                className = HorizontalViewerFragment.class.getCanonicalName();
                break;
            default:
                className = VerticalViewerFragment.class.getCanonicalName();
        }

        return (AbstractViewerFragment) Fragment.instantiate(MediaMonkeyApplication.getInstance(), className);
    }

    private Runnable hideSysUiCallback = new Runnable() {
        @Override
        public void run() {
            if (getViewerView().isSysUiShowing()) {
                getViewerView().toggleSysUi();
            }
        }
    };
}
