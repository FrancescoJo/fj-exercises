/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto.contents;

import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Jan - 2016
 */
public class ChapterInventoryList {
    private final long                   contentId;
    private final long                   chapterId;
    private final List<ChapterInventory> inventoryList;
    private final CharSequence           title;
    private final boolean                hasPrevious;
    private final boolean                hasNext;

    public long getContentId() {
        return contentId;
    }

    public long getChapterId() {
        return chapterId;
    }

    public List<ChapterInventory> getInventoryList() {
        return inventoryList;
    }

    public CharSequence getTitle() {
        return title;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    public boolean hasNext() {
        return hasNext;
    }

    private ChapterInventoryList(long contentId, long chapterId, List<ChapterInventory> inventoryList,
                         CharSequence title, boolean hasPrevious, boolean hasNext) {
        this.contentId     = contentId;
        this.chapterId     = chapterId;
        this.inventoryList = inventoryList;
        this.title         = title;
        this.hasPrevious   = hasPrevious;
        this.hasNext       = hasNext;
    }

    public static class Builder {
        private long                   contentId;
        private long                   chapterId;
        private List<ChapterInventory> inventoryList;
        private CharSequence           title;
        private boolean                hasPrevious;
        private boolean                hasNext;

        public Builder contentId(final long id) {
            this.contentId = id;
            return this;
        }

        public Builder chapterId(final long id) {
            this.chapterId = id;
            return this;
        }

        public Builder inventoryList(final List<ChapterInventory> inventoryList) {
            this.inventoryList = inventoryList;
            return this;
        }

        public Builder title(final CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder hasPrevious(final boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
            return this;
        }

        public Builder hasNext(final boolean hasNext) {
            this.hasNext = hasNext;
            return this;
        }

        public ChapterInventoryList create() {
            return new ChapterInventoryList(contentId, chapterId, inventoryList, title, hasPrevious, hasNext);
        }
    }
}
