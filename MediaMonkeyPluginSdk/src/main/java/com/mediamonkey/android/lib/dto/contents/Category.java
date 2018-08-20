/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto.contents;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;

import com.mediamonkey.android.lib.internal.EqualsBuilder;
import com.mediamonkey.android.lib.internal.HashCodeBuilder;
import com.mediamonkey.android.lib.internal.HashCodeExclude;
import com.mediamonkey.android.lib.internal.MmSdkUtils;
import com.mediamonkey.android.lib.util.DataStorable;

import java.util.Comparator;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
public final class Category implements Parcelable, DataStorable {
    private static final String SCHEMA_NAME = Category.class.getSimpleName();

    @DataStorable.Value(dataType = DataType.INTEGER, keyType = KeyType.PRIMARY)
    private long id;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String title;

    @DataStorable.Value(dataType = DataType.INTEGER, keyType = KeyType.FOREIGN, reference = "ThumbnailSpec_id")
    private ThumbnailSpec thumbnailSpec;

    @DataStorable.Value(dataType = DataType.INTEGER)
    private long lastAccessDate;

    @DataStorable.Value(dataType = DataType.INTEGER)
    private long browseCount;

    @DataStorable.Value(dataType = DataType.INTEGER)
    private boolean bookmarked;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String description;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String payload;

    @HashCodeExclude
    private transient Spanned titleText;

    @HashCodeExclude
    private transient Spanned descriptionText;

    // Required by DataStorable protocol
    @SuppressWarnings("unused")
    Category(Cursor cursor) {
        this(MmSdkUtils.getLong(cursor, SCHEMA_NAME, "id"),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "title", ""),
                new ThumbnailSpec(cursor),
                MmSdkUtils.getLong(cursor, SCHEMA_NAME, "lastAccessDate"),
                MmSdkUtils.getLong(cursor, SCHEMA_NAME, "browseCount"),
                (MmSdkUtils.getInt(cursor, SCHEMA_NAME, "bookmarked")) != 0,
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "description", ""),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "payload", "")
       );
    }

    private Category(long id, String title, ThumbnailSpec thumbnailSpec,
                     long lastAccessDate, long browseCount, boolean bookmarked,
                     String description, String payload) {
        this.id              = id;
        this.title           = title;
        this.thumbnailSpec   = thumbnailSpec;
        this.lastAccessDate  = lastAccessDate;
        this.browseCount     = browseCount;
        this.bookmarked      = bookmarked;
        this.description     = description;
        this.payload         = payload;
        this.titleText       = MmSdkUtils.fromHtml(title);
        this.descriptionText = MmSdkUtils.fromHtml(description);
    }

    public long getId() {
        return id;
    }

    /*default*/ String getTitle() {
        return title;
    }

    @Nullable
    public ThumbnailSpec getThumbnailSpec() {
        return thumbnailSpec;
    }

    public long getLastAccessDate() {
        return lastAccessDate;
    }

    public long getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(final long newCount) {
        this.browseCount = newCount;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean flag) {
        this.bookmarked = flag;
    }

    /*default*/ String getDescription() {
        return description;
    }

    @Nullable
    public String getPayload() {
        return payload;
    }

    public CharSequence getTitleText() {
        return titleText;
    }

    @Nullable
    public CharSequence getDescriptionText() {
        return descriptionText;
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    public static class Builder {
        private long             id;
        private String           title;
        private ThumbnailSpec thumbnailSpec;
        private long             lastAccessDate;
        private long             browseCount;
        private boolean          bookmarked;
        private String           description;
        private String           payload;

        public Builder() { }

        public Builder(Category source) {
            this.id             = source.getId();
            this.title          = source.getTitle();
            this.thumbnailSpec  = source.getThumbnailSpec();
            this.lastAccessDate = source.getLastAccessDate();
            this.browseCount    = source.getBrowseCount();
            this.bookmarked     = source.isBookmarked();
            this.description    = source.getDescription();
            this.payload        = source.getPayload();
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        @SuppressWarnings("ConstantConditions")
        public Builder title(@NonNull String title) {
            // Defensive logic to make programme never fails
            if (null == title) {
                this.title = "";
            } else {
                this.title = title;
            }

            return this;
        }

        /**
         * @param thumbnailSpec Thumbnail of this category, could be <code>null</code>
         */
        public Builder thumbnailSpec(@Nullable ThumbnailSpec thumbnailSpec) {
            this.thumbnailSpec = thumbnailSpec;
            return this;
        }

        public Builder lastAccessDate(long lastAccessDate) {
            this.lastAccessDate = lastAccessDate;
            return this;
        }

        public Builder browseCount(long browseCount) {
            this.browseCount = browseCount;
            return this;
        }

        public Builder bookmarked(boolean bookmarked) {
            this.bookmarked = bookmarked;
            return this;
        }

        public Builder description(@Nullable String description) {
            this.description = description;
            return this;
        }

        public Builder payload(@Nullable String payload) {
            this.payload = payload;
            return this;
        }

        public Category create() {
            return new Category(id, title, thumbnailSpec, lastAccessDate, browseCount,
                    bookmarked, description, payload);
        }
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in.readLong(),
                    in.readString(),
                    (ThumbnailSpec) in.readParcelable(ThumbnailSpec.class.getClassLoader()),
                    in.readLong(),
                    in.readLong(),
                    in.readByte() != 0,
                    in.readString(),
                    in.readString()
            );
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeParcelable(thumbnailSpec, i);
        parcel.writeLong(lastAccessDate);
        parcel.writeLong(browseCount);
        parcel.writeByte((byte) (bookmarked ? 1 : 0));
        parcel.writeString(description);
        parcel.writeString(payload);
    }

    public static Comparator<Category> BOOKMARK_COMPARATOR = new Comparator<Category>() {
        @Override
        public int compare(Category lhs, Category rhs) {
            if ((lhs.isBookmarked() && rhs.isBookmarked()) || (!lhs.isBookmarked() && !rhs.isBookmarked())) {
                return 0;
            } else if (lhs.isBookmarked() && !rhs.isBookmarked()) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    public static Comparator<Category> TITLE_COMPARATOR = new Comparator<Category>() {
        @Override
        public int compare(Category lhs, Category rhs) {
            return lhs.getTitle().compareTo(rhs.getTitle());
        }
    };
}
