/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.main;

import android.content.Context;

import com.mediamonkey.android.lib.dto.contents.Category;
import com.mediamonkey.android.lib.dto.contents.CategoryList;
import com.mediamonkey.android.lib.dto.contents.Chapter;
import com.mediamonkey.android.lib.dto.contents.ChapterList;
import com.mediamonkey.android.lib.dto.contents.Content;
import com.mediamonkey.android.lib.dto.contents.ContentList;
import com.mediamonkey.android.plugin.PluginAccessHelper;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Nov - 2016
 */
interface MainView {
    void showPluginNotLoaded();

    void showLoadingView();

    void hideLoadingView();

    void showError(final Throwable e, final Runnable retryAction);

    void showCategoryList(final CategoryList categoryList);

    void showContentList(final Category category, final ContentList contentList);

    void showChapterList(final CharSequence title, final ChapterList chapterList);

    void showContent(final int position, final Content content);

    void showChapter(final int position, final Chapter chapter);

    void notifyPluginLoaded(final PluginAccessHelper pluginHelper);

    CharSequence getTitleText();

    void setTitleText(CharSequence title);

    Context getContext();
}
