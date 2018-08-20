/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.viewer;

import com.mediamonkey.android.lib.dto.contents.ChapterInventoryList;
import com.mediamonkey.android.lib.dto.contents.Content;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jan - 2017
 */
class WrappedContent {
    private final Content              content;
    private final ChapterInventoryList inventoryList;

    WrappedContent(Content content, ChapterInventoryList inventoryList) {
        this.content       = content;
        this.inventoryList = inventoryList;
    }

    Content getContent() {
        return content;
    }

    ChapterInventoryList getInventoryList() {
        return inventoryList;
    }
}
