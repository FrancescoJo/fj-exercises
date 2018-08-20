/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.filepicker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.fj.android.mediamonkey.Constants;
import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.dto.PluginManifestDto;
import com.fj.android.mediamonkey.plugin.PluginManager;
import com.fj.android.mediamonkey.ui._base.AbstractBaseActivity;
import com.fj.android.mediamonkey.ui._base.ActivityLifecycle;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.fj.android.mediamonkey.ui.common.DialogHelper;
import com.fj.android.mediamonkey.util.PlatformUtils;
import com.fj.android.mediamonkey.util.io.FileUtils;
import com.fj.android.mediamonkey.util.lang.NullSafeUtils;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Nov - 2016
 */
public class FilePickerActivity extends AbstractBaseActivity implements FilePickerView {
    public static final String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final String KEY_DEFAULT_LOCATION = "kDefLoc";
    private static final String KEY_PICK_RESULT = "kPickRes";

    @Inject PluginManager pluginMgr;
    @Inject FilePickerViewPresenter presenter;

    @BindView(R.id.toolbar)                     Toolbar         toolbar;
    @BindView(R.id.layout_file_picker_contents) View            vContents;
    @BindView(R.id.layout_file_picker_loading)  View            vLoading;
    @BindView(R.id.txt_file_picker_currentdir)  TextView        tvCurrentPath;
    @BindView(R.id.list_file_picker)            RecyclerView    listFiles;
    private                                     FileListAdapter listAdapter;

    private Stack<String> pathStack;
    private FileObserver  pathUpdateObserver;

    @Override
    protected List<? extends BasePresenter> getPresenters() {
        return Collections.singletonList(presenter);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);

        setSupportActionBar(toolbar);
        LinearLayoutManager llMgr = new LinearLayoutManager(this);
        listFiles.setLayoutManager(llMgr);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listFiles.getContext(),
                llMgr.getOrientation());
        listFiles.addItemDecoration(dividerItemDecoration);

        this.pathStack = new Stack<>();
        this.listAdapter = new FileListAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginPkgVo pkgInfo = listAdapter.getBoundPkgInfo(view);
                if (null == pkgInfo) {
                    return;
                }

                if (pkgInfo.isDirectory()) {
                    if (PluginPkgVo.Type.DIRECTORY_PARENT == pkgInfo.getType()) {
                        pathStack.pop();
                        browsePath(pathStack.peek());
                    } else {
                        String path = pkgInfo.getFile().getAbsolutePath();
                        pathStack.push(path);
                        browsePath(path);
                    }
                } else {
                    final Dialog pg = DialogHelper.showModalSpinner(FilePickerActivity.this, R.string.text_loading);
                    pluginMgr.readPluginFile(pkgInfo.getFile())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<PluginManifestDto>() {
                                @Override
                                public void accept(final PluginManifestDto packageManifest) throws Exception {
                                    DialogHelper.showYesNo(FilePickerActivity.this,
                                            R.string.text_confirm_required,
                                            R.string.msg_check_plugin_info,
                                            packageManifest.asInfoList(), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    onLoadPlugin(packageManifest);
                                                }
                                            });
                                }
                            }, new Consumer<Throwable>() {
                                // IllegalArgumentException: wrong file
                                // InstantiationException: file exist but wrong classes.dex
                                // IOException: general I/O exception
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    DialogHelper.showPluginException(FilePickerActivity.this, throwable);
                                }
                            }, new Action() {
                                @Override
                                public void run() throws Exception {
                                    pg.dismiss();
                                }
                            });
                }
            }
        });
        listFiles.setAdapter(listAdapter);
        String initialPath = NullSafeUtils.get(getIntent().getStringExtra(KEY_DEFAULT_LOCATION), Constants.DEFAULT_FILE_PICKER_PATH);
        pathStack.push(initialPath);
        browsePath(initialPath);
    }

    protected void onResume() {
        super.onResume();
        FileUtils.startPathUpdateObserver(pathUpdateObserver);
    }

    @Override
    protected void onPause() {
        FileUtils.stopPathUpdateObserver(pathUpdateObserver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (pathStack.size() == 1) {
            super.onBackPressed();
        } else {
            pathStack.pop();
            browsePath(pathStack.peek());
        }
    }

    void onLoadPlugin(PluginManifestDto manifest) {
        Intent data = new Intent();
        data.putExtra(KEY_PICK_RESULT, manifest);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    void browsePath(final String path) {
        /*
         * Terminate previous watcher if allocated before allocating new one,
         * to prevent duplicated event listening & memory leak
         */
        FileUtils.stopPathUpdateObserver(pathUpdateObserver);
        this.pathUpdateObserver = new FileObserver(path) {
            @Override
            public void onEvent(final int event, final String path) {
                // http://stackoverflow.com/questions/6336422/fileobserver-getting-strange-events/10455467
                int myEvent = event & FileObserver.ALL_EVENTS;
                if (0 == myEvent) {
                    return;
                }

                switch (myEvent) {
                    case FileObserver.ATTRIB:
                    case FileObserver.CLOSE_WRITE:
                    case FileObserver.CREATE:
                    case FileObserver.MODIFY:
                    case FileObserver.MOVED_FROM:
                        // reload self
                        browsePath(path);
                        break;
                    case FileObserver.MOVED_TO:
                    case FileObserver.MOVE_SELF:
                    case FileObserver.DELETE:
                    case FileObserver.DELETE_SELF:
                        pathStack.clear();
                        browsePath(Constants.DEFAULT_FILE_PICKER_PATH);
                        break;
                    case FileObserver.OPEN:
                    case FileObserver.CLOSE_NOWRITE:
                    default:
                        break;
                }
            }
        };

        pathUpdateObserver.startWatching();
        presenter.loadFileList(path, getObservableLifeCycle(ActivityLifecycle.ON_DESTROY));
    }

    @Override
    public void showLoading() {
        vLoading.setVisibility(View.VISIBLE);
        vContents.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        vLoading.setVisibility(View.GONE);
        vContents.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearList() {
        listAdapter.clear();
    }

    @Override
    public boolean isContentLoaded() {
        return listAdapter.getItemCount() > 0;
    }

    @Override
    public void showFileList(String path, List<PluginPkgVo> fileList) {
        tvCurrentPath.setText(path);
        listAdapter.addAll(fileList);
    }

    public static Intent newLaunchIntent(Activity callerActivity, String defaultLocation) {
        if (!PlatformUtils.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return null;
        }

        Intent intent = new Intent(callerActivity, FilePickerActivity.class);
        intent.putExtra(KEY_DEFAULT_LOCATION, defaultLocation);
        return intent;
    }

    public static PluginManifestDto parseResultIntent(Intent intent) {
        return intent.getParcelableExtra(KEY_PICK_RESULT);
    }
}
