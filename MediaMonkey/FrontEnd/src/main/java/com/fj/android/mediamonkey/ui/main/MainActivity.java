/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.fj.android.mediamonkey.Constants;
import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.dto.PluginManifestDto;
import com.fj.android.mediamonkey.inject.objects.SharedPrefManager;
import com.fj.android.mediamonkey.plugin.PluginManager;
import com.fj.android.mediamonkey.ui._base.AbstractBaseActivity;
import com.fj.android.mediamonkey.ui._base.ActivityLifecycle;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.fj.android.mediamonkey.ui.common.DialogHelper;
import com.fj.android.mediamonkey.ui.common.DrawerMenuEventHandler;
import com.fj.android.mediamonkey.ui.common.DrawerMenuTopViewHolder;
import com.fj.android.mediamonkey.ui.filepicker.FilePickerActivity;
import com.fj.android.mediamonkey.ui.settings.plugin.PluginSettingsActivity;
import com.fj.android.mediamonkey.ui.viewer.ViewerActivity;
import com.fj.android.mediamonkey.util.lang.CollectionUtils;
import com.mediamonkey.android.lib.dto.contents.Category;
import com.mediamonkey.android.lib.dto.contents.CategoryList;
import com.mediamonkey.android.lib.dto.contents.Chapter;
import com.mediamonkey.android.lib.dto.contents.ChapterList;
import com.mediamonkey.android.lib.dto.contents.Content;
import com.mediamonkey.android.lib.dto.contents.ContentList;
import com.mediamonkey.android.plugin.PluginAccessHelper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Nov - 2016
 */
public class MainActivity extends AbstractBaseActivity implements MainView {
    private static final int REQ_CODE_FILE_PICKER     = 0x1001;
    private static final int REQ_CODE_PLUGIN_SETTINGS = 0x1002;

    @Inject PluginAccessHelper pluginHelper;
    @Inject PluginManager      pluginMgr;
    @Inject MainViewPresenter  presenter;
    @Inject SharedPrefManager  prefManager;

    @BindView(R.id.toolbar)              Toolbar        toolbar;
    @BindView(R.id.nav_view_main)        NavigationView navMenu;
    @BindView(R.id.layout_drawer_main)   DrawerLayout   drawerLayout;
    @BindView(R.id.layout_main_content)  ViewGroup      contentLayout;
    private View                                        emptyView;
    private TextView                                    tvEmptyView;
    private TextView                                    btnEmptyView;
    private DrawerMenuTopViewHolder                     menuTopViewHolder;
    private Dialog                                      loadingDialog;

    private Set<String> activeFragmentTags;

    @Override
    protected List<? extends BasePresenter> getPresenters() {
        return Collections.singletonList(presenter);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        this.activeFragmentTags = new HashSet<>();

        this.menuTopViewHolder = new DrawerMenuTopViewHolder(navMenu.getHeaderView(0));
        navMenu.setNavigationItemSelectedListener(menuListener);

        ActionBarDrawerToggle drawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.text_menu_open, R.string.text_menu_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        startLoading();
    }

    @Override
    protected void onStart() {
        super.onStart();
        invalidateMenu(pluginHelper);
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
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions,
                                           final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE_FILE_PICKER: {
                if (grantResults.length > 0) {
                    showFilePicker();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(final int reqCode, final int resultCode, final Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch(reqCode) {
                case REQ_CODE_FILE_PICKER:
                    PluginManifestDto manifest = FilePickerActivity.parseResultIntent(data);
                    presenter.loadPlugin(manifest, getObservableLifeCycle(ActivityLifecycle.ON_DESTROY));
                    break;
                case REQ_CODE_PLUGIN_SETTINGS:
                    removeAllFragments();
                    startLoading();
                    break;
            }
        }
    }

    @Override
    public void notifyPluginLoaded(final PluginAccessHelper pluginHelper) {
        invalidateMenu(pluginHelper);
        emptyView.setVisibility(View.GONE);
        startLoading();
    }

    private void invalidateMenu(final PluginAccessHelper pluginHelper) {
        if (null == pluginHelper) {
            return;
        }

        Menu menu = navMenu.getMenu();
        menuTopViewHolder.draw(pluginHelper);
        boolean isPluginLoaded = pluginHelper.isPluginLoaded();
        menu.findItem(R.id.menu_drawer_plugin_info).setEnabled(isPluginLoaded);
        boolean hasPluginSettings = pluginHelper.hasSettings();
        menu.findItem(R.id.menu_drawer_plugin_settings).setVisible(hasPluginSettings);
    }

    void showFilePicker() {
        Intent intent = FilePickerActivity.newLaunchIntent(this, Constants.DEFAULT_FILE_PICKER_PATH);
        if (null == intent) {
            ActivityCompat.requestPermissions(this, FilePickerActivity.REQUIRED_PERMISSIONS, REQ_CODE_FILE_PICKER);
        } else {
            startActivityForResult(intent, REQ_CODE_FILE_PICKER);
        }
    }

    private void startLoading() {
        presenter.loadCategoryList(null, 0, getObservableLifeCycle(ActivityLifecycle.ON_DESTROY));
    }

    public void showPluginNotLoaded() {
        hideLoadingView();
        showEmptyView();

        tvEmptyView.setText(R.string.msg_no_contents_to_display);
        btnEmptyView.setText(R.string.text_select_plugin);
        btnEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilePicker();
            }
        });
    }

    private void showEmptyView() {
        if (null == emptyView) {
            ViewStub vsEmpty  = ButterKnife.findById(this, R.id.layout_main_contents_empty);
            this.emptyView    = vsEmpty.inflate();
            this.tvEmptyView  = ButterKnife.findById(emptyView, R.id.txt_main_empty);
            this.btnEmptyView = ButterKnife.findById(emptyView, R.id.btn_main_empty);
        }
        emptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        if (null != emptyView) {
            emptyView.setVisibility(View.GONE);
        }
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
    public void showError(final Throwable e, final Runnable retryAction) {
        showEmptyView();
        hideLoadingView();
        DialogHelper.showException(this, e);

        tvEmptyView.setText(R.string.msg_problem_occurred);
        btnEmptyView.setText(R.string.text_retry);
        if (null != retryAction) {
            btnEmptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retryAction.run();
                }
            });
        }
    }

    @Override
    public void showCategoryList(final CategoryList categoryList) {
        hideEmptyView();
        hideLoadingView();

        fragmentTransactionByAdding(CategoryFragment.newInstance(categoryList, getString(R.string.app_name)))
                .commit();
    }

    @Override
    public void showContentList(final Category category, final ContentList contentList) {
        hideEmptyView();
        hideLoadingView();

        ContentFragment currentFragment = findVisibleFragment(ContentFragment.class);
        if (null == currentFragment) {
            FragmentTransaction fragTransaction =
                    fragmentTransactionByAdding(ContentFragment.newInstance(category, contentList));
            fragTransaction.addToBackStack(null);
            fragTransaction.commit();
        } else {
            currentFragment.onContentListAdded(contentList);
        }
    }

    @Override
    public void showChapterList(final CharSequence title, final ChapterList chapterList) {
        hideEmptyView();
        hideLoadingView();

        FragmentTransaction fragTransaction = fragmentTransactionByAdding(ChapterFragment.newInstance(chapterList, title));
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
    }

    @Override
    public void showContent(final int position, final Content content) {
        Intent intent = ViewerActivity.newIntent(this, content);
        launchViewer(intent);
    }

    @Override
    public void showChapter(final int position, final Chapter chapter) {
        Intent intent = ViewerActivity.newIntent(this, chapter);
        launchViewer(intent);
    }

    private void launchViewer(final Intent viewerIntent) {
        hideEmptyView();
        hideLoadingView();

        startActivity(viewerIntent);
    }

    Observable<ActivityLifecycle> until(final ActivityLifecycle cycle) {
        return getObservableLifeCycle(cycle);
    }

    private <T extends Fragment> T findVisibleFragment(final Class<T> klass) {
        @SuppressWarnings("unchecked")
        T fragment = (T) getFragmentManager().findFragmentByTag(klass.getSimpleName());
        if (null != fragment && fragment.isVisible()) {
            return fragment;
        } else {
            return null;
        }
    }

    private FragmentTransaction fragmentTransactionByAdding(final Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        FragmentManager fragMan = getFragmentManager();

        activeFragmentTags.add(tag);
        FragmentTransaction fragTransaction = fragMan.beginTransaction();
        fragTransaction.add(contentLayout.getId(), fragment, tag);
        return fragTransaction;
    }

    private void removeAllFragments() {
        if (0 == CollectionUtils.size(activeFragmentTags)) {
            return;
        }

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransact = fragMan.beginTransaction();
        for (String tag : activeFragmentTags) {
            Fragment fragment = fragMan.findFragmentByTag(tag);
            fragTransact.remove(fragment);
        }
        fragTransact.commit();
        activeFragmentTags.clear();
    }

    @Override
    public void setTitleText(CharSequence title) {
        toolbar.setTitle(title);
    }

    @Override
    public CharSequence getTitleText() {
        return toolbar.getTitle();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private NavigationView.OnNavigationItemSelectedListener menuListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(final @NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_drawer_plugin_load:
                            showFilePicker();
                            return true;
                        case R.id.menu_drawer_plugin_settings:
                            startActivityForResult(PluginSettingsActivity.newLaunchIntent(MainActivity.this),
                                    REQ_CODE_PLUGIN_SETTINGS);
                            break;
                    }

                    DrawerMenuEventHandler.onOptionsItemSelected(MainActivity.this, item);

                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    return true;
                }
    };
}
