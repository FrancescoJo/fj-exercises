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
 * @since 25 - Nov - 2016
 */
public class ContentList implements Parcelable {
    @SuppressWarnings("ConstantConditions")
    public static final ContentList EMPTY = new ContentList.Builder()
            .displayType(DisplayType.UNDEFINED)
            .contentList(null)
            .hasNext(false)
            .create();

    private @DisplayType int displayType;
    private List<Content>    contentList;
    private boolean          hasNext;

    private ContentList(@DisplayType int displayType, List<Content> contentList,
                        boolean hasNext) {
        this.displayType = displayType;
        this.contentList = contentList;
        this.hasNext     = hasNext;
    }

    public @DisplayType int getDisplayType() {
        return displayType;
    }

    public List<Content> getContentList() {
        return contentList;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public static class Builder {
        private @DisplayType int  displayType;
        private List<Content>  contentList;
        private boolean           hasNext;

        public Builder displayType(@DisplayType int displayType) {
            this.displayType = displayType;
            return this;
        }

        @SuppressWarnings("ConstantConditions")
        public Builder contentList(@NonNull List<Content> contentList) {
            // Defensive logic to make programme never fails
            if (null == contentList) {
                this.contentList = new ArrayList<>();
            } else {
                this.contentList = contentList;
            }

            return this;
        }

        public Builder hasNext(boolean flag) {
            this.hasNext = flag;
            return this;
        }

        public ContentList create() {
            return new ContentList(displayType, contentList, hasNext);
        }
    }

    public static final Creator<ContentList> CREATOR = new Creator<ContentList>() {
        @Override
        public ContentList createFromParcel(Parcel in) {
            return new ContentList(IntDefConverter.toDisplayType(in.readInt()),
                    in.createTypedArrayList(Content.CREATOR),
                    in.readByte() != 0
            );
        }

        @Override
        public ContentList[] newArray(int size) {
            return new ContentList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(displayType);
        dest.writeTypedList(contentList);
        dest.writeByte((byte) (hasNext ? 1 : 0));
    }
}
