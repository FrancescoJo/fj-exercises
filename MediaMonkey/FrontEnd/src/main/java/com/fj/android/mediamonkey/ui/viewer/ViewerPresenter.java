/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.viewer;

import com.fj.android.mediamonkey.annotation.PersistOnConfigChange;
import com.fj.android.mediamonkey.plugin.PluginImplException;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.mediamonkey.android.lib.dto.contents.ChapterInventoryList;
import com.mediamonkey.android.lib.dto.contents.Content;
import com.mediamonkey.android.plugin.ContentsService;
import com.mediamonkey.android.plugin.PluginAccessHelper;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Dec - 2016
 */
@PersistOnConfigChange
class ViewerPresenter extends BasePresenter<ViewerView> {
    @Inject PluginAccessHelper pluginHelper;

    @Inject
    ViewerPresenter() { }

    void loadContents(final long contentId, final long chapterId,
                      final int page, final Observable<?> untilWhen) {
        Timber.d("Load inventory: contentId: %s, chapterId:%s, page: %s", contentId, chapterId, page);
        getView().showLoadingView();

        /*
         * Why no 'zip' operator? since zip finishes eagerly, therefore any observable watches
         * Thread.isInterrupted will works abnormally. For example, if contentService implementation
         * uses library such as HtmlCleaner, will not operates properly since the library strictly
         * checks Thread interruption.
         */
        Observable.create(new ObservableOnSubscribe<WrappedContent>() {
            @Override
            public void subscribe(final ObservableEmitter<WrappedContent> wrappedContentEmitter) throws Exception {
                wrappedContentEmitter.onNext(new WrappedContent(
                        Single.create(new SingleOnSubscribe<Content>() {
                            @Override
                            @SuppressWarnings("ConstantConditions")
                            public void subscribe(final SingleEmitter<Content> contentEmitter) throws Exception {
                                Content content = getContentsService().loadCachedContent(contentId);
                                if (null == content) {
                                    throw new PluginImplException("Plugin returned empty content - contentId: " + contentId + ", page: " + page);
                                }

                                contentEmitter.onSuccess(content);
                            }
                        }).blockingGet(),
                        Single.create(new SingleOnSubscribe<ChapterInventoryList>() {
                            @Override
                            @SuppressWarnings("ConstantConditions")
                            public void subscribe(final SingleEmitter<ChapterInventoryList> chapterInvenEmitter) throws Exception {
                                ChapterInventoryList chapterInvenList = getContentsService().loadChapterInventory(contentId, chapterId);
                                if (null == chapterInvenList) {
                                    throw new PluginImplException("Plugin returned empty chapter inventory - contentId: " +
                                            contentId + "chapterId: " + chapterId + ", page: " + page);
                                }

                                chapterInvenEmitter.onSuccess(chapterInvenList);
                            }
                        }).blockingGet()
                ));
                wrappedContentEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(untilWhen)
                .subscribe(new Consumer<WrappedContent>() {
                    @Override
                    public void accept(final WrappedContent wrappedContent) throws Exception {
                        getView().showContent(wrappedContent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        getView().showError(throwable);
                    }
                });
    }

    private ContentsService getContentsService() {
        return pluginHelper.getPluginInstance().getContentsService();
    }
}
