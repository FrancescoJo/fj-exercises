/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.viewer;

import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;

import com.fj.android.mediamonkey.BuildConfig;
import com.fj.android.mediamonkey.plugin.PluginImplException;
import com.fj.android.mediamonkey.util.RxUtils;
import com.fj.android.mediamonkey.util.lang.CollectionUtils;
import com.mediamonkey.android.lib.dto.ContentType;
import com.mediamonkey.android.lib.dto.contents.ChapterInventory;
import com.mediamonkey.android.lib.dto.contents.ChapterInventoryList;
import com.mediamonkey.android.lib.dto.contents.Content;
import com.mediamonkey.android.plugin.ContentsService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Jan - 2017
*/
class ViewerDataManager {
    private static final ViewerData PRELOAD_VIEW                = new ViewerData(null, ViewType.LOADING);
    private static final long       CHAPTER_EDGE_DISPLAY_PERIOD = 2 * 60 * 1000L;
    private static final Object     CHAPTER_EDGE_LOCK           = new Object();

    interface DataObserver {
        void onDataChanged(final int oldSize);
    }

    private final ArrayList<ViewerData> data;

    private Content                              content;
    private TreeMap<Long, ChapterInventoryList>  loadedChapters;
    private int                                  currentViewIndex;
    private SparseArray<String>                  chapterEdges;
    private SparseArray<Long>                    chapterEdgeTimestamp;
    private WeakReference<ContentsService>       contentsServiceRef;
    private LongSparseArray<Observable<Boolean>> hasNextChapterTasks;
    private Disposable                           loadNextChapterDisposable;
    private List<DataObserver>                   dataObservers;

    static ViewerDataManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    void attachContentsService(final ContentsService contentsService) {
        this.contentsServiceRef = new WeakReference<>(contentsService);
    }

    /**
     * Call this method only once.
     */
    void setContent(final WrappedContent wrappedContent) {
        if (null != content) {
            if (BuildConfig.DEBUG) {
                throw new IllegalStateException("This method must not be called twice");
            }
            return;
        }

        this.content = wrappedContent.getContent();
        addInventories(wrappedContent.getInventoryList());
    }

    void clear() {
        synchronized (data) {
            data.clear();
        }
        this.content = null;
        loadedChapters.clear();
        this.currentViewIndex = 0;
        synchronized (CHAPTER_EDGE_LOCK) {
            chapterEdges.clear();
            chapterEdgeTimestamp.clear();
        }
        contentsServiceRef.clear();
        hasNextChapterTasks.clear();
        RxUtils.dispose(loadNextChapterDisposable);
        dataObservers.clear();
    }

    void registerDataObserver(final DataObserver observer) {
        if (!dataObservers.contains(observer)) {
            dataObservers.add(observer);
        }
    }

    void unregisterDataObserver(final DataObserver observer) {
        dataObservers.remove(observer);
    }

    private void addInventories(final ChapterInventoryList invenList) {
        if (!loadedChapters.containsKey(invenList.getChapterId())) {
            loadedChapters.put(invenList.getChapterId(), invenList);
            synchronized (data) {
                addChapterEdgeAt(data.size(), invenList);
                if (!TextUtils.isEmpty(content.getContent())) {
                    data.add(contentToData(content, invenList.getChapterId()));
                }
                for (ChapterInventory inventory : invenList.getInventoryList()) {
                    data.add(new ViewerData(inventory, ViewType.byContentType(inventory.getType())));
                }
            }
        }
    }

    boolean hasData() {
        synchronized (data) {
            return CollectionUtils.size(data) > 0;
        }
    }

    void setCurrentViewIndex(final int index) {
        this.currentViewIndex = index;
    }

    int getCurrentViewIndex() {
        return currentViewIndex;
    }

    ViewerData getViewerDataAt(final int position) {
        synchronized (data) {
            return data.get(position);
        }
    }

    ViewType getTypeAt(final int position) {
        return getViewerDataAt(position).getType();
    }

    int getCount() {
        synchronized (data) {
            return data.size();
        }
    }

    /**
     * @return An observable to tell whether next of current chapter(based by currentViewIndex)
     * can be pre-loadable or not.
     */
    Observable<Boolean> nextChapterPreloadable() {
        final long currentChapterId = getCurrentDisplayingChapter();
        Long nextChapterId = loadedChapters.higherKey(currentChapterId);

        if (null != nextChapterId) {
            return Observable.just(false);
        }

        ChapterInventoryList inventoryList = loadedChapters.get(currentChapterId);
        if (!inventoryList.hasNext()) {
            return Observable.just(false);
        }

        final ContentsService contentsService = contentsServiceRef.get();
        if (null == contentsService) {
            return Observable.just(false);
        }

        Observable<Boolean> hasNextChapterTask = hasNextChapterTasks.get(currentChapterId);
        if (hasNextChapterTask != null) {
            return hasNextChapterTask;
        }

        hasNextChapterTask = BehaviorSubject.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> hasNextChapterEmitter) throws Exception {
                try {
                    hasNextChapterEmitter.onNext(contentsService.getNextChapterId(currentChapterId) != -1);
                } catch (Exception e) {
                    Timber.d(e, "Error occurred on contentService#getNextChapterId invocation");
                    hasNextChapterEmitter.onNext(false);
                } finally {
                    hasNextChapterEmitter.onComplete();
                }
            }
        });
        hasNextChapterTasks.put(currentChapterId, hasNextChapterTask);
        return hasNextChapterTask;
    }

    long getCurrentDisplayingChapter() {
        ChapterInventory inventory;
        synchronized (data) {
            inventory = data.get(currentViewIndex).getData();
        }
        return inventory.getChapterId();
    }

    void addPreloadViewAtLast() {
        synchronized (data) {
            if (PRELOAD_VIEW != data.get(data.size() - 1)) {
                data.add(PRELOAD_VIEW);
            }
        }
    }

    private boolean canLoadNextChapter() {
        final long currentChapterId = getCurrentDisplayingChapter();
        return loadedChapters.higherKey(currentChapterId) == null && !isLoadingNextChapter();
    }

    boolean canDisplayChapterEdge(final int position) {
        synchronized (CHAPTER_EDGE_LOCK) {
            long timeDelta = System.currentTimeMillis() - chapterEdgeTimestamp.get(position, 0L);
            return chapterEdges.indexOfKey(position) >= 0 && timeDelta > CHAPTER_EDGE_DISPLAY_PERIOD;
        }
    }

    String getChapterEdge(final int position) {
        synchronized (CHAPTER_EDGE_LOCK) {
            return chapterEdges.get(position);
        }
    }

    void setChapterEdgeDisplayed(final int position) {
        synchronized (CHAPTER_EDGE_LOCK) {
            chapterEdgeTimestamp.put(position, System.currentTimeMillis());
        }
    }

    void loadNextChapter(final long currentChapterId) {
        if (!canLoadNextChapter()) {
            return;
        }

        this.loadNextChapterDisposable = Single.create(new SingleOnSubscribe<ChapterInventoryList>() {
            @Override
            // Defensive logic
            @SuppressWarnings("ConstantConditions")
            public void subscribe(final SingleEmitter<ChapterInventoryList> e) throws Exception {
                Timber.d("Loading next chapter of %s", currentChapterId);
                ContentsService service = contentsServiceRef.get();
                Long nextChapterId = service.getNextChapterId(currentChapterId);
                if (null == nextChapterId) {
                    e.onError(new PluginImplException("Cannot load next chapter id of " + currentChapterId));
                    return;
                }
                ChapterInventoryList list = service.loadChapterInventory(content.getId(), nextChapterId);
                if (null == list || 0 == CollectionUtils.size(list.getInventoryList())) {
                    e.onError(new PluginImplException("Empty chapter inventory list of " + currentChapterId));
                    return;
                }
                Timber.d("Loaded next chapter: %s, size: %s", nextChapterId, list.getInventoryList().size());

                e.onSuccess(list);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ChapterInventoryList>() {
                    @Override
                    public void accept(final ChapterInventoryList chapterInventoryList) throws Exception {
                        synchronized (data) {
                            int lastIndex = data.size() - 1;
                            if (data.get(lastIndex) == PRELOAD_VIEW) {
                                data.remove(lastIndex);
                            }
                        }
                        int oldSize = data.size();
                        addInventories(chapterInventoryList);
                        for (DataObserver observer : dataObservers) {
                            observer.onDataChanged(oldSize);
                        }
                        RxUtils.dispose(loadNextChapterDisposable);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        // There will be silent retry by user input on AdapterView
                    }
                });
    }

    private void addChapterEdgeAt(final int position, final ChapterInventoryList inventoryList) {
        synchronized (CHAPTER_EDGE_LOCK) {
            chapterEdges.put(position, content.getTitle() + " " + inventoryList.getTitle());
        }
    }

    boolean isLoadingNextChapter() {
        return RxUtils.isDisposed(loadNextChapterDisposable);
    }

    private static ViewerData contentToData(final Content content,
                                            final long chapterId) {
        return new ViewerData(new ChapterInventory(
                chapterId, content.getType(), content.getAccessUri(), content.getContent()),
                ViewType.byContentType(content.getType()));
    }

    // False alarm
    @SuppressWarnings("WeakerAccess")
    static class ViewerData {
        private final ChapterInventory data;
        private final ViewType viewType;

        ViewerData(ChapterInventory inventory, ViewType viewType) {
            this.data = inventory;
            this.viewType = viewType;
        }

        ChapterInventory getData() {
            return data;
        }

        ViewType getType() {
            return viewType;
        }
    }

    enum ViewType {
        // Make values compatible as ContentType to reuse byValue logic
        TEXT(0),
        RICH_TEXT(1),
        IMAGE(2),
        MUSIC(3),
        VIDEO(4),
        LOADING(1024),
        UNDEFINED(Integer.MIN_VALUE);

        final int value;

        ViewType(final int type) {
            this.value = type;
        }

        static ViewType byValue(final int value) {
            for (ViewType viewType : ViewType.values()) {
                if (viewType.value == value) {
                    return viewType;
                }
            }

            return UNDEFINED;
        }

        static ViewType byContentType(final ContentType contentType) {
            if (null != contentType) {
                return byValue(contentType.value);
            } else {
                return UNDEFINED;
            }
        }
    }

    private ViewerDataManager() {
        this.data                 = new ArrayList<>();
        this.loadedChapters       = new TreeMap<>();
        this.currentViewIndex     = 0;
        this.chapterEdges         = new SparseArray<>();
        this.chapterEdgeTimestamp = new SparseArray<>();
        this.hasNextChapterTasks  = new LongSparseArray<>();
        this.dataObservers        = new ArrayList<>();
    }

    private static class InstanceHolder {
        static final ViewerDataManager INSTANCE = new ViewerDataManager();
    }
}
