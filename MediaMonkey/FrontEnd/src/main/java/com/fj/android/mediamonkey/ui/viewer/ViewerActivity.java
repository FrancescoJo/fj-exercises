/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.viewer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.ui._base.AbstractBaseActivity;
import com.fj.android.mediamonkey.ui._base.ActivityLifecycle;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.fj.android.mediamonkey.ui.common.DialogHelper;
import com.fj.android.mediamonkey.ui.common.DrawerMenuEventHandler;
import com.fj.android.mediamonkey.ui.common.DrawerMenuTopViewHolder;
import com.fj.android.mediamonkey.util.view.ActionBarUtils;
import com.mediamonkey.android.lib.dto.contents.Chapter;
import com.mediamonkey.android.lib.dto.contents.Content;
import com.mediamonkey.android.plugin.PluginAccessHelper;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fj.android.mediamonkey.ui.viewer.ViewerActivityUtils.getHideSysUiFlags;
import static com.fj.android.mediamonkey.ui.viewer.ViewerActivityUtils.getShowSysUiFlags;
import static com.fj.android.mediamonkey.ui.viewer.ViewerActivityUtils.isLandscape;
import static com.fj.android.mediamonkey.ui.viewer.ViewerActivityUtils.isPortrait;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Dec - 2016
 */
public class ViewerActivity extends AbstractBaseActivity implements ViewerView {
    private static final String KEY_CONTENT_ID       = "kCntId";
    private static final String KEY_CHAPTER_ID       = "kChpId";
    private static final long   SYS_UI_HIDE_DURATION = 500L;

    @Inject PluginAccessHelper pluginHelper;
    @Inject ViewerPresenter    presenter;

    @BindView(R.id.toolbar)               Toolbar        toolbar;
    @BindView(R.id.nav_view_viewer)       NavigationView navMenu;
    @BindView(R.id.layout_drawer_viewer)  DrawerLayout   drawerLayout;
    @BindView(R.id.layout_viewer_content) RelativeLayout contentLayout;
    private                               Dialog         loadingDialog;

    private int               currentOrientation;
    private boolean           sysUiShowing       = true;
    private ViewerDataManager dataMgr;

    @Override
    protected List<? extends BasePresenter> getPresenters() {
        return Collections.singletonList(presenter);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);

        long contentId = getIntent().getLongExtra(KEY_CONTENT_ID, 0L);
        long chapterId = getIntent().getLongExtra(KEY_CHAPTER_ID, 0L);

        if (0L == contentId || 0L == chapterId) {
            throw new IllegalStateException("Given content/chapter id is 0!!");
        }

        this.dataMgr      = ViewerDataManager.getInstance();
        dataMgr.attachContentsService(pluginHelper.getPluginInstance().getContentsService());
        // Start orientation: portrait (in manifest)
        this.currentOrientation = getResources().getConfiguration().orientation;
        this.sysUiShowing = true;

        ActionBarDrawerToggle drawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.text_menu_open, R.string.text_menu_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final @NonNull MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        Menu menu = navMenu.getMenu();
        menu.findItem(R.id.menu_drawer_plugin_load).setVisible(false);
        menu.findItem(R.id.menu_drawer_plugin_info).setEnabled(pluginHelper.isPluginLoaded());
        DrawerMenuTopViewHolder menuTopViewHolder = new DrawerMenuTopViewHolder(navMenu.getHeaderView(0));
        menuTopViewHolder.draw(pluginHelper);

        if (dataMgr.hasData()) {
            // TODO: Restore view status
        } else {
            presenter.loadContents(contentId, chapterId, 0, getObservableLifeCycle(ActivityLifecycle.ON_DESTROY));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_activity_viewer);
        tb.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_viewer_rotate_screen:
                toggleOrientation();
                break;
            case R.id.menu_activity_viewer_adjust_font:
                return super.onOptionsItemSelected(item);
            default:
                DrawerMenuEventHandler.onOptionsItemSelected(this, item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        DialogHelper.dismiss(loadingDialog);
        if (isFinishing()) {
            ViewerDataManager.getInstance().clear();
        }
        super.onDestroy();
    }

    @Override
    public void showLoadingView() {
        if (DialogHelper.isShowing(loadingDialog)) {
            return;
        }

        this.loadingDialog = DialogHelper.showFullScreenLoading(this);
        loadingDialog.setCancelable(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void hideLoadingView() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        DialogHelper.dismiss(loadingDialog);
    }

    @Override
    public void showContent(final WrappedContent wrappedContent) {
        hideLoadingView();
        dataMgr.setContent(wrappedContent);

        getFragmentManager().beginTransaction()
                .replace(contentLayout.getId(), AbstractViewerFragment.newInstance())
                .commit();
    }

    @Override
    public void showError(final Throwable e) {
        hideLoadingView();
        DialogHelper.showException(this, e);
    }

    @Override
    public boolean isSysUiShowing() {
        return sysUiShowing;
    }

    @Override
    public void toggleOrientation() {
        if (isLandscape(currentOrientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        } else if (isPortrait(currentOrientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            currentOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            // Default to portrait
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
    }

    @Override
    public void toggleSysUi() {
        View decorView = getWindow().getDecorView();
        if (sysUiShowing) {
            decorView.setSystemUiVisibility(getHideSysUiFlags());
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            toolbar.animate().alpha(0.0f).setDuration(SYS_UI_HIDE_DURATION)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            ActionBarUtils.hide(getSupportActionBar());
                        }
                    }).start();
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            decorView.setSystemUiVisibility(getShowSysUiFlags());
            ActionBarUtils.show(getSupportActionBar());
            toolbar.animate().alpha(1.0f).setDuration(SYS_UI_HIDE_DURATION).start();
        }
        sysUiShowing = !sysUiShowing;
    }

    @Override
    public ViewerDataManager getDataManager() {
        return dataMgr;
    }

    public static Intent newIntent(final Activity callerActivity, final Content content) {
        Intent intent = new Intent(callerActivity, ViewerActivity.class);
        return buildNewIntentData(intent, content.getId(), 0L);
    }

    public static Intent newIntent(final Activity callerActivity, final Chapter chapter) {
        Intent intent = new Intent(callerActivity, ViewerActivity.class);
        return buildNewIntentData(intent, chapter.getContentId(), chapter.getId());
    }

    private static Intent buildNewIntentData(final Intent intent,
                                             final long contentId, final long chapterId) {
        intent.putExtra(KEY_CONTENT_ID, contentId);
        intent.putExtra(KEY_CHAPTER_ID, chapterId);

        return intent;
    }
}
