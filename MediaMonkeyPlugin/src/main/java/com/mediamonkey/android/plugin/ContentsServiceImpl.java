/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.plugin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mediamonkey.android.lib.dto.contents.CategoryDto;
import com.mediamonkey.android.lib.dto.contents.CategoryListDto;
import com.mediamonkey.android.lib.dto.contents.ContentListDto;
import com.mediamonkey.android.lib.service.ContentsService;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Nov - 2016
 */
public class ContentsServiceImpl implements ContentsService {
    @Override
    public boolean hasCategoryList() {
        return false;
    }

    @NonNull
    @Override
    public CategoryListDto loadCategoryList(@Nullable CategoryDto categoryDto, int i) {
        return null;
    }

    @NonNull
    @Override
    public ContentListDto loadContentList(@Nullable CategoryDto categoryDto, int i) {
        return null;
    }
}
