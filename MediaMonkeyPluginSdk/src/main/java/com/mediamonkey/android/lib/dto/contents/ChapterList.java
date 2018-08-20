/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto.contents;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.mediamonkey.android.lib.annotation.DisplayType;
import com.mediamonkey.android.lib.internal.IntDefConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Dec - 2016
 */
public class ChapterList implements Parcelable {
    @SuppressWarnings("ConstantConditions")
    public static final ChapterList EMPTY = new ChapterList.Builder()
            .displayType(DisplayType.UNDEFINED)
            .chapterList(null)
            .hasNext(false)
            .create();

    private @DisplayType int displayType;
    private List<Chapter>    chapterList;
    private boolean          hasNext;

    private ChapterList(@DisplayType int displayType, List<Chapter> chapterList,
                        boolean hasNext) {
        this.displayType = displayType;
        this.chapterList = chapterList;
        this.hasNext     = hasNext;
    }

    public @DisplayType int getDisplayType() {
        return displayType;
    }

    public List<Chapter> getChapterList() {
        return chapterList;
    }

    public static class Builder {
        private @DisplayType int displayType;
        private List<Chapter>    chapterList;
        private boolean          hasNext;

        public ChapterList.Builder displayType(@DisplayType int displayType) {
            this.displayType = displayType;
            return this;
        }

        @SuppressWarnings("ConstantConditions")
        public ChapterList.Builder chapterList(@NonNull List<Chapter> chapterList) {
            // Defensive logic to make programme never fails
            if (null == chapterList) {
                this.chapterList = new ArrayList<>();
            } else {
                this.chapterList = chapterList;
            }

            return this;
        }

        public ChapterList.Builder hasNext(boolean flag) {
            this.hasNext = flag;
            return this;
        }

        public ChapterList create() {
            return new ChapterList(displayType, chapterList, hasNext);
        }
    }

    public static final Creator<ChapterList> CREATOR = new Creator<ChapterList>() {
        @Override
        public ChapterList createFromParcel(Parcel in) {
            return new ChapterList(IntDefConverter.toDisplayType(in.readInt()),
                    in.createTypedArrayList(Chapter.CREATOR),
                    in.readByte() != 0
            );
        }

        @Override
        public ChapterList[] newArray(int size) {
            return new ChapterList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(displayType);
        dest.writeTypedList(chapterList);
        dest.writeByte((byte) (hasNext ? 1 : 0));
    }
}
