/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto.contents;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Spanned;

import com.mediamonkey.android.lib.internal.EqualsBuilder;
import com.mediamonkey.android.lib.internal.HashCodeBuilder;
import com.mediamonkey.android.lib.internal.HashCodeExclude;
import com.mediamonkey.android.lib.internal.MmSdkUtils;
import com.mediamonkey.android.lib.util.DataStorable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Dec - 2016
 */
public final class Chapter implements Parcelable, DataStorable {
    private static final String SCHEMA_NAME = Chapter.class.getSimpleName();

    @DataStorable.Value(dataType = DataStorable.DataType.INTEGER, keyType = DataStorable.KeyType.PRIMARY)
    private long id;

    @DataStorable.Value(dataType = DataStorable.DataType.INTEGER)
    private long contentId;

    @DataStorable.Value(dataType = DataStorable.DataType.TEXT)
    private String title;

    @DataStorable.Value(dataType = DataStorable.DataType.INTEGER, keyType = DataStorable.KeyType.FOREIGN, reference = "ThumbnailSpec_id")
    private ThumbnailSpec thumbnailSpec;

    @DataStorable.Value(dataType = DataStorable.DataType.TEXT)
    private Uri accessUri;

    @DataStorable.Value(dataType = DataStorable.DataType.INTEGER)
    private long lastAccessDate;

    @DataStorable.Value(dataType = DataStorable.DataType.INTEGER)
    private long browseCount;

    @DataStorable.Value(dataType = DataStorable.DataType.TEXT)
    private String description;

    @DataStorable.Value(dataType = DataStorable.DataType.TEXT)
    private String payload;

    @HashCodeExclude
    private transient Spanned titleText;

    @HashCodeExclude
    private transient Spanned descriptionText;

    // Required by DataStorable protocol
    @SuppressWarnings("unused")
    Chapter(final Cursor cursor) {
        this(MmSdkUtils.getLong(cursor, SCHEMA_NAME, "id"),
                MmSdkUtils.getLong(cursor, SCHEMA_NAME, "contentId"),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "title", ""),
                new ThumbnailSpec(cursor),
                Uri.parse(MmSdkUtils.getString(cursor, SCHEMA_NAME, "accessUri", "")),
                MmSdkUtils.getLong(cursor, SCHEMA_NAME, "lastAccessDate"),
                MmSdkUtils.getLong(cursor, SCHEMA_NAME, "browseCount"),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "description", ""),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "payload", "")
        );
    }

    private Chapter(final long id, final long contentId, final String title,
                    final ThumbnailSpec thumbnailSpec, final Uri accessUri,
                    final long lastAccessDate, final long browseCount,
                    final String description, final String payload) {
        this.id              = id;
        this.contentId       = contentId;
        this.title           = title;
        this.thumbnailSpec   = thumbnailSpec;
        this.accessUri       = accessUri;
        this.lastAccessDate  = lastAccessDate;
        this.browseCount     = browseCount;
        this.description     = description;
        this.payload         = payload;
        this.titleText       = MmSdkUtils.fromHtml(title);
        this.descriptionText = MmSdkUtils.fromHtml(description);
    }

    public long getId() {
        return id;
    }

    public long getContentId() {
        return contentId;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public ThumbnailSpec getThumbnailSpec() {
        return thumbnailSpec;
    }

    @Nullable
    public Uri getAccessUri() {
        return accessUri;
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

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getPayload() {
        return payload;
    }

    @Nullable
    public CharSequence getTitleText() {
        return titleText;
    }

    @Nullable
    public CharSequence getDescriptionText() {
        return descriptionText;
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    public static class Builder {
        private long                   id;
        private long                   contentId;
        private String                 title;
        private ThumbnailSpec          thumbnailSpec;
        private Uri                    accessUri;
        private long                   lastAccessDate;
        private long                   browseCount;
        private String                 description;
        private String                 payload;

        public Builder() { }

        public Builder(final Chapter chapter) {
            this.id             = chapter.id;
            this.contentId      = chapter.contentId;
            this.title          = chapter.title;
            this.thumbnailSpec  = chapter.thumbnailSpec;
            this.accessUri      = chapter.accessUri;
            this.lastAccessDate = chapter.lastAccessDate;
            this.browseCount    = chapter.browseCount;
            this.description    = chapter.description;
            this.payload        = chapter.payload;
        }

        public Builder id(final long id) {
            this.id = id;
            return this;
        }

        public Builder contentId(final long contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder thumbnailSpec(final ThumbnailSpec thumbnailSpec) {
            this.thumbnailSpec = thumbnailSpec;
            return this;
        }

        public Builder accessUri(final @Nullable Uri accessUri) {
            this.accessUri = accessUri;
            return this;
        }

        public Builder lastAccessDate(final long lastAccessDate) {
            this.lastAccessDate = lastAccessDate;
            return this;
        }

        public Builder browseCount(final long browseCount) {
            this.browseCount = browseCount;
            return this;
        }

        public Builder description(final @Nullable String description) {
            this.description = description;
            return this;
        }

        public Builder payload(final @Nullable String payload) {
            this.payload = payload;
            return this;
        }

        public Chapter create() {
            return new Chapter(id, contentId, title, thumbnailSpec, accessUri,
                    lastAccessDate, browseCount, description, payload);
        }
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(final Parcel in) {
            Chapter chapter = new Chapter(in.readLong(),
                    in.readLong(),
                    in.readString(),
                    (ThumbnailSpec) in.readParcelable(ThumbnailSpec.class.getClassLoader()),
                    (Uri) in.readParcelable(Uri.class.getClassLoader()),
                    in.readLong(),
                    in.readLong(),
                    in.readString(),
                    in.readString()
            );
            List<ChapterInventory> inventoryList = new ArrayList<>();
            in.readTypedList(inventoryList, ChapterInventory.CREATOR);

            return chapter;
        }

        @Override
        public Chapter[] newArray(final int size) {
            return new Chapter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(id);
        dest.writeLong(contentId);
        dest.writeString(title);
        dest.writeParcelable(thumbnailSpec, flags);
        dest.writeParcelable(accessUri, flags);
        dest.writeLong(lastAccessDate);
        dest.writeLong(browseCount);
        dest.writeString(description);
        dest.writeString(payload);
    }

    public static Comparator<Chapter> TITLE_COMPARATOR = new Comparator<Chapter>() {
        @Override
        public int compare(final Chapter lhs, final Chapter rhs) {
            return lhs.getTitle().compareTo(rhs.getTitle());
        }
    };
}
