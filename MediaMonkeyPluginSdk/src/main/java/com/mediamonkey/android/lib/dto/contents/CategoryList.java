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
public class CategoryList implements Parcelable {
    @SuppressWarnings("ConstantConditions")
    public static final CategoryList EMPTY = new Builder()
            .displayType(DisplayType.UNDEFINED)
            .categoryList(null)
            .hasNext(false)
            .create();

    private @DisplayType int displayType;
    private List<Category>   categoryList;
    private boolean          hasNext;

    private CategoryList(@DisplayType int displayType, List<Category> categoryList,
                         boolean hasNext) {
        this.displayType  = displayType;
        this.categoryList = categoryList;
        this.hasNext      = hasNext;
    }

    public @DisplayType int getDisplayType() {
        return displayType;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public static class Builder {
        private @DisplayType int  displayType;
        private List<Category> categoryList;
        private boolean           hasNext;

        public Builder displayType(@DisplayType int displayType) {
            this.displayType = displayType;
            return this;
        }

        @SuppressWarnings("ConstantConditions")
        public Builder categoryList(@NonNull List<Category> categoryList) {
            // Defensive logic to make programme never fails
            if (null == categoryList) {
                this.categoryList = new ArrayList<>();
            } else {
                this.categoryList = categoryList;
            }
            return this;
        }

        public Builder hasNext(boolean flag) {
            this.hasNext = flag;
            return this;
        }

        public CategoryList create() {
            return new CategoryList(displayType, categoryList, hasNext);
        }
    }

    public static final Creator<CategoryList> CREATOR = new Creator<CategoryList>() {
        @Override
        public CategoryList createFromParcel(Parcel in) {
            return new CategoryList(IntDefConverter.toDisplayType(in.readInt()),
                    in.createTypedArrayList(Category.CREATOR),
                    in.readByte() != 0
            );
        }

        @Override
        public CategoryList[] newArray(int size) {
            return new CategoryList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(displayType);
        dest.writeTypedList(categoryList);
        dest.writeByte((byte) (hasNext ? 1 : 0));
    }
}
