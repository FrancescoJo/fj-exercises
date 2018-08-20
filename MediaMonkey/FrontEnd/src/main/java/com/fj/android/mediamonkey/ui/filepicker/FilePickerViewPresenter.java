/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.filepicker;

import com.fj.android.mediamonkey.Constants;
import com.fj.android.mediamonkey.annotation.PersistOnConfigChange;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.fj.android.mediamonkey.util.io.FileUtils;
import com.mediamonkey.android.lib.internal.ComparatorChain;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Nov - 2016
 */
@PersistOnConfigChange
class FilePickerViewPresenter extends BasePresenter<FilePickerView> {
    @Inject
    FilePickerViewPresenter() { }

    private static final Comparator<PluginPkgVo> FILE_TYPE_COMPARATOR = new Comparator<PluginPkgVo>() {
        @Override
        public int compare(PluginPkgVo lVal, PluginPkgVo rVal) {
            if (lVal.isDirectory() && rVal.isDirectory() ||
                !lVal.isDirectory() && !rVal.isDirectory()) {
                return 0;
            } else if (lVal.isDirectory() && !rVal.isDirectory()) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    private static final Comparator<PluginPkgVo> FILE_NAME_COMPARATOR = new Comparator<PluginPkgVo>() {
        @Override
        public int compare(PluginPkgVo lVal, PluginPkgVo rVal) {
            String lName = lVal.getFileName().toString();
            String rName = rVal.getFileName().toString();
            return lName.compareTo(rName);
        }
    };

    void loadFileList(final String path, final Observable<?> untilWhen) {
        // To prevent screen flickering
        if (!getView().isContentLoaded()) {
            getView().showLoading();
        }
        getView().clearList();
        Observable.create(new ObservableOnSubscribe<List<PluginPkgVo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<PluginPkgVo>> e) throws Exception {
                List<PluginPkgVo> list = new ArrayList<>();
                File file = new File(path).getCanonicalFile();
                File[] files = file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        try {
                            if (FileUtils.isSymbolicLink(file)) {
                                Timber.v("Skipping to add symbolic link %s", file.getAbsoluteFile());
                                return false;
                            }

                            if (FileUtils.isDirEmpty(file)) {
                                Timber.v("Skipping to add empty directory %s", file.getAbsoluteFile());
                                return false;
                            }
                        } catch (IOException err) {
                            Timber.w(err, "I/O exception while accessing %s", file.getAbsoluteFile());
                            return false;
                        }

                        return file.getName().endsWith(Constants.PLUGIN_PACKAGE_SUFFIX);
                    }
                });
                if (null != files) {
                    for (File fileItem : files) {
                        PluginPkgVo vo = new PluginPkgVo();
                        vo.setFile(fileItem);
                        list.add(vo);
                    }
                }

                Collections.sort(list, new ComparatorChain<>(
                        Arrays.asList(FILE_TYPE_COMPARATOR, FILE_NAME_COMPARATOR)
                ));

                File parent = file.getParentFile();
                if (null != parent) {
                    list.add(0, PluginPkgVo.createAsParent(parent));
                } else {
                    list.add(0, PluginPkgVo.createAsExternal());
                }

                e.onNext(list);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(untilWhen)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().hideLoading();
                    }
                })
                .subscribe(new Consumer<List<PluginPkgVo>>() {
                    @Override
                    public void accept(List<PluginPkgVo> fileList) throws Exception {
                        getView().showFileList(path, fileList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }
}
