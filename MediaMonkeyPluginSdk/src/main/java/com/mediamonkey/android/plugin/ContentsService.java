/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.plugin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.mediamonkey.android.lib.dto.contents.Category;
import com.mediamonkey.android.lib.dto.contents.CategoryList;
import com.mediamonkey.android.lib.dto.contents.Chapter;
import com.mediamonkey.android.lib.dto.contents.ChapterInventoryList;
import com.mediamonkey.android.lib.dto.contents.ChapterList;
import com.mediamonkey.android.lib.dto.contents.Content;
import com.mediamonkey.android.lib.dto.contents.ContentList;

import java.io.IOException;

/**
 * Contents related service interface.
 * Most methods in this interface is invoked by Contents screen.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Nov - 2016
 */
public interface ContentsService extends ShutdownableService {
    /**
     * Tells that this table of contents can be categorised.
     *
     * @return  <code>true</code> if could be categorised, or <code>false</code> could not.
     */
    @WorkerThread
    boolean hasCategoryList();

    /**
     * Called when no contents were loaded. This is the top hierarchy of contents;
     * therefore this class is always called first if {@link ContentsService#hasCategoryList()}
     * returns <code>true</code>.
     *
     * @param category <code>null</code> when user requests the top of contents category hierarchy,
     *                 non-null otherwise.
     * @param page     Displaying page. Zero based.
     * @return List of category.
     */
    @Nullable
    @WorkerThread
    CategoryList loadCategoryList(final @Nullable Category category, final int page) throws IOException;

    /**
     * @param category Category item to update.
     * @return <code>true</code> if request was successful, <code>false</code> otherwise.
     * @throws IOException when underlying operation was failed due to I/O exception.
     */
    @WorkerThread
    boolean updateCategory(final @NonNull Category category) throws IOException;

    /**
     * Called when certain category is picked, or first-time if plugin does not
     * provides any categorised contents.
     *
     * @param category <code>null</code> when this content list could not be categorised,
     *                 non-null if this content list belongs to certain category.
     * @param page     displaying page. Zero based.
     * @return List of contents
     */
    @NonNull
    @WorkerThread
    ContentList loadContentList(final @Nullable Category category, final int page) throws IOException;

    /**
     * Called when viewer requires locally cached {@link com.mediamonkey.android.lib.dto.contents.Content} object.
     *
     * @param contentId id of cached Content object
     */
    @NonNull
    @WorkerThread
    Content loadCachedContent(final long contentId);

    /**
     * @param content Content item to update.
     *
     * @return <code>true</code> if request was successful, <code>false</code> otherwise.
     * @throws IOException when underlying operation was failed due to I/O exception.
     */
    @WorkerThread
    boolean updateContent(final @NonNull Content content) throws IOException;

    /**
     * @param chapter Chapter item to update.
     *
     * @return <code>true</code> if request was successful, <code>false</code> otherwise.
     * @throws IOException when underlying operation was failed due to I/O exception.
     */
    @WorkerThread
    boolean updateChapter(final @NonNull Chapter chapter) throws IOException;

    /**
     * Tells that given category has <em>chapter list</em>.
     *
     * @param category A category object which user choose.
     * @return <code>true</code> if chapter list is available for given <code>category</code>, <code>false</code> otherwise.
     */
    @WorkerThread
    boolean hasChapterList(final @NonNull Content category) throws IOException;

    /**
     * Called when certain content is picked.
     *
     * @param content A content object which user choose.
     * @param page    displaying page. Zero based.
     * @return List of chapters
     */
    @NonNull
    @WorkerThread
    ChapterList loadChapterList(final @NonNull Content content, final int page) throws IOException;

    /**
     * Called when certain chapter is picked. 'Chapter' is the last selection,
     * that is shown in Viewer.
     *
     * @param contentId    content id that is associated with target chapter object.
     * @param chapterId    id of selected chapter object.
     * @return Chapter Inventory list object to display
     */
    @NonNull
    @WorkerThread
    ChapterInventoryList loadChapterInventory(final long contentId, final long chapterId) throws IOException;

    /**
     * Called when there is/are need(s) to determine that current chapter is not the end of
     * content.
     *
     * @param currentChapterId    a pivot value to compute next chapter Id.
     * @return                    next chapter id of given chapter with <code>currentChapterId</code>.
     * -1 if given <code>currentChapterId</code> represents the end of content.
     */
    @WorkerThread
    long getNextChapterId(final long currentChapterId) throws IOException;
}
