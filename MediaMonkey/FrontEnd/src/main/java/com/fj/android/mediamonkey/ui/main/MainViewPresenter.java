/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.annotation.PersistOnConfigChange;
import com.fj.android.mediamonkey.dto.PluginManifestDto;
import com.fj.android.mediamonkey.plugin.PluginException;
import com.fj.android.mediamonkey.plugin.PluginImplException;
import com.fj.android.mediamonkey.plugin.PluginManager;
import com.fj.android.mediamonkey.plugin.PluginNotLoadedException;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.fj.android.mediamonkey.ui.common.DialogHelper;
import com.fj.android.mediamonkey.util.ResourceUtils;
import com.mediamonkey.android.lib.dto.contents.Category;
import com.mediamonkey.android.lib.dto.contents.CategoryList;
import com.mediamonkey.android.lib.dto.contents.Chapter;
import com.mediamonkey.android.lib.dto.contents.ChapterList;
import com.mediamonkey.android.lib.dto.contents.Content;
import com.mediamonkey.android.lib.dto.contents.ContentList;
import com.mediamonkey.android.plugin.ContentsService;
import com.mediamonkey.android.plugin.PluginAccessHelper;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Nov - 2016
 */
@PersistOnConfigChange
class MainViewPresenter extends BasePresenter<MainView> {
    @Inject PluginManager      pluginMgr;
    @Inject PluginAccessHelper pluginHelper;

    @Inject
    MainViewPresenter() { }

    void loadCategoryList(final Category category, final int page, final Observable<?> untilWhen) {
        Timber.d("Load categoryList: %s, page: %s", category, page);
        getView().showLoadingView();
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> hasCategoryEmitter) throws Exception {
                checkPluginLoaded();
                hasCategoryEmitter.onNext(getContentsService().hasCategoryList());
            }
        }).flatMap(new Function<Boolean, ObservableSource<CategoryList>>() {
            @Override
            public ObservableSource<CategoryList> apply(final Boolean hasCategoryList) throws Exception {
                CategoryList list;
                if (hasCategoryList && null != (list = getContentsService().loadCategoryList(category, page))) {
                    return Observable.just(list);
                } else {
                    return Observable.just(CategoryList.EMPTY);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(untilWhen)
                .subscribe(new Consumer<CategoryList>() {
                    @Override
                    public void accept(final CategoryList categoryListDto) throws Exception {
                        if (CategoryList.EMPTY == categoryListDto) {
                            loadContentList(null, 0, untilWhen);
                        } else {
                            getView().showCategoryList(categoryListDto);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        getView().hideLoadingView();
                        if (throwable instanceof PluginNotLoadedException) {
                            getView().showPluginNotLoaded();
                        } else {
                            throwable.printStackTrace();
                            getView().showError(throwable, new Runnable() {
                                @Override
                                public void run() {
                                    loadCategoryList(category, page, untilWhen);
                                }
                            });
                        }
                    }
                });
    }

    void loadContentList(final Category category, final int page, final Observable<?> untilWhen) {
        getView().showLoadingView();
        Observable.create(new ObservableOnSubscribe<ContentList>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void subscribe(final ObservableEmitter<ContentList> contentListEmitter) throws Exception {
                ContentList contentList = getContentsService().loadContentList(category, page);
                if (null == contentList) {
                    String exceptionMsg;
                    if (null == category) {
                        exceptionMsg = "Plugin returned empty Content List - categoryId: NULL, page: " + page;
                    } else {
                        exceptionMsg = "Plugin returned empty Content List - categoryId: " + category.getId() + ", page: " + page;
                    }
                    throw new PluginImplException(exceptionMsg);
                }

                contentListEmitter.onNext(contentList);
                contentListEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(untilWhen)
                .subscribe(new Consumer<ContentList>() {
                    @Override
                    public void accept(final ContentList contentListDto) throws Exception {
                        getView().showContentList(category, contentListDto);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        getView().showError(throwable, new Runnable() {
                            @Override
                            public void run() {
                                loadContentList(category, page, untilWhen);
                            }
                        });
                    }
                });
    }

    Observable<Boolean> hasChapterList(final Content content) {
        getView().showLoadingView();
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(getContentsService().hasChapterList(content));
            }
        });
    }

    void loadChapterList(final Content content, final int page, final Observable<?> untilWhen) {
        getView().showLoadingView();
        Observable.create(new ObservableOnSubscribe<ChapterList>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void subscribe(final ObservableEmitter<ChapterList> contentListEmitter) throws Exception {
                ChapterList chapterList = getContentsService().loadChapterList(content, 0);
                if (null == chapterList) {
                    String exceptionMsg;
                    if (null == chapterList) {
                        exceptionMsg = "Plugin returned empty Chapter List - contentId: NULL, page: " + page;
                    } else {
                        exceptionMsg = "Plugin returned empty Chapter List - contentId: " + content.getId() + ", page: " + page;
                    }
                    throw new PluginImplException(exceptionMsg);
                }

                contentListEmitter.onNext(chapterList);
                contentListEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(untilWhen)
                .subscribe(new Consumer<ChapterList>() {
                    @Override
                    public void accept(final ChapterList chapterList) throws Exception {
                        getView().showChapterList(content.getTitle(), chapterList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        getView().showError(throwable, new Runnable() {
                            @Override
                            public void run() {
                                loadChapterList(content, page, untilWhen);
                            }
                        });
                    }
                });
    }

    void loadChapter(final int position, final Content content) {
        getView().showContent(position, content);
    }

    void loadChapter(final int position, final Chapter chapter) {
        getView().showChapter(position, chapter);
    }

    Observable<Boolean> updateCategory(final Category categoryDto) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(getContentsService().updateCategory(categoryDto));
                e.onComplete();
            }
        });
    }

    Observable<Boolean> updateContent(final Content content) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(getContentsService().updateContent(content));
                e.onComplete();
            }
        });
    }

    Observable<Boolean> updateChapter(final Chapter chapter) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(getContentsService().updateChapter(chapter));
                e.onComplete();
            }
        });
    }

    private void checkPluginLoaded() throws PluginNotLoadedException {
        if (!pluginHelper.isPluginLoaded()) {
            throw new PluginNotLoadedException("Plugin is not loaded");
        }
    }

    void loadPlugin(final PluginManifestDto manifest, final Observable<?> untilWhen) {
        loadPluginInternal(manifest, false, untilWhen);
    }

    private void loadPluginInternal(final PluginManifestDto manifest, final boolean isForce,
                                    final Observable<?> untilWhen) {
        final Dialog pg = DialogHelper.showModalSpinner(getView().getContext(), R.string.text_loading);
        pluginMgr.loadPlugin(manifest, isForce)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(untilWhen)
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        pg.dismiss();
                    }
                })
                .subscribe(new Consumer<PluginAccessHelper>() {
                    @Override
                    public void accept(final PluginAccessHelper mediaMonkeyPlugin) throws Exception {
                        getView().notifyPluginLoaded(mediaMonkeyPlugin);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        if (throwable instanceof PluginException) {
                            PluginException e = (PluginException) throwable;
                            Context context = getView().getContext();
                            DialogHelper.showYesNo(getView().getContext(), ResourceUtils.getString(R.string.text_confirm_required),
                                    context.getString(R.string.err_already_installed, e.getOldVersion(),
                                            manifest.getVersion()), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, final int i) {
                                            loadPluginInternal(manifest, true, untilWhen);
                                        }
                                    });
                        } else {
                            DialogHelper.showException(getView().getContext(), throwable);
                        }
                    }
                });
    }

    private ContentsService getContentsService() {
        return pluginHelper.getPluginInstance().getContentsService();
    }
}
