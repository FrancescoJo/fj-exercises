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

import com.mediamonkey.android.lib.dto.ContentType;
import com.mediamonkey.android.lib.internal.DataConverter;
import com.mediamonkey.android.lib.internal.EqualsBuilder;
import com.mediamonkey.android.lib.internal.HashCodeBuilder;
import com.mediamonkey.android.lib.internal.HashCodeExclude;
import com.mediamonkey.android.lib.internal.MmSdkUtils;
import com.mediamonkey.android.lib.internal.ParcelableUtils;
import com.mediamonkey.android.lib.util.DataStorable;

import java.util.Comparator;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
public final class Content implements Parcelable, DataStorable {
    private static final String SCHEMA_NAME = Content.class.getSimpleName();

    @DataStorable.Value(dataType = DataType.INTEGER, keyType = KeyType.PRIMARY)
    private long id;

    @DataStorable.Value(dataType = DataType.TEXT)
    private List<Long> categoryIdList;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String title;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String content;

    @DataStorable.Value(dataType = DataType.INTEGER)
    private int contentType;

    @DataStorable.Value(dataType = DataType.INTEGER, keyType = KeyType.FOREIGN, reference = "ThumbnailSpec_id")
    private ThumbnailSpec thumbnailSpec;

    @DataStorable.Value(dataType = DataType.INTEGER, keyType = KeyType.FOREIGN, reference = "Author_id")
    private Author author;

    @DataStorable.Value(dataType = DataType.TEXT)
    private Uri accessUri;

    @DataStorable.Value(dataType = DataType.INTEGER)
    private long lastAccessDate;

    @DataStorable.Value(dataType = DataType.INTEGER)
    private long browseCount;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String description;

    @DataStorable.Value(dataType = DataType.INTEGER)
    private boolean bookmarked;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String payload;

    @HashCodeExclude
    private transient Spanned titleText;

    @HashCodeExclude
    private transient ContentType type;

    @HashCodeExclude
    private transient Spanned descriptionText;

    // Required by DataStorable protocol
    @SuppressWarnings("unused")
    Content(final Cursor cursor) {
        this(MmSdkUtils.getLong(cursor, SCHEMA_NAME, "id"),
                DataConverter.numStrsToLongList(MmSdkUtils.getString(cursor, SCHEMA_NAME, "categoryIdList", "").split(",")),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "title", ""),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "content", ""),
                MmSdkUtils.getInt(cursor, SCHEMA_NAME, "contentType"),
                new ThumbnailSpec(cursor),
                new Author(cursor),
                Uri.parse(MmSdkUtils.getString(cursor, SCHEMA_NAME, "accessUri", "")),
                MmSdkUtils.getLong(cursor, SCHEMA_NAME, "lastAccessDate"),
                MmSdkUtils.getLong(cursor, SCHEMA_NAME, "browseCount"),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "description", ""),
                (MmSdkUtils.getInt(cursor, SCHEMA_NAME, "bookmarked")) != 0,
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "payload", "")
        );
    }

    private Content(final long id, final List<Long> categoryIdList, final String title,
                    final String content, final int contentType, final ThumbnailSpec thumbnailSpec,
                    final Author author, final Uri accessUri, final long lastAccessDate,
                    final long browseCount, final String description, final boolean bookmarked,
                    final String payload) {
        this.id              = id;
        this.categoryIdList  = categoryIdList;
        this.title           = title;
        this.content         = content;
        this.contentType     = contentType;
        this.thumbnailSpec   = thumbnailSpec;
        this.author          = author;
        this.accessUri       = accessUri;
        this.lastAccessDate  = lastAccessDate;
        this.browseCount     = browseCount;
        this.bookmarked      = bookmarked;
        this.description     = description;
        this.payload         = payload;
        this.titleText       = MmSdkUtils.fromHtml(title);
        this.descriptionText = MmSdkUtils.fromHtml(description);
        this.type            = ContentType.byValue(contentType);
    }

    public long getId() {
        return id;
    }

    @Nullable
    public List<Long> getCategoryIdList() {
        return categoryIdList;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    @Nullable
    public ThumbnailSpec getThumbnailSpec() {
        return thumbnailSpec;
    }

    @Nullable
    public Author getAuthor() {
        return author;
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

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(final boolean flag) {
        this.bookmarked = flag;
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

    @Nullable
    public ContentType getType() {
        return type;
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
        private long          id;
        private List<Long>    categoryIdList;
        private String        title;
        private String        content;
        private ContentType   contentType;
        private ThumbnailSpec thumbnailSpec;
        private Author        author;
        private Uri           accessUri;
        private long          lastAccessDate;
        private long          browseCount;
        private boolean       bookmarked;
        private String        description;
        private String        payload;

        public Builder(final Content content) {
            this.id             = content.getId();
            this.categoryIdList = content.getCategoryIdList();
            this.title          = content.getTitle();
            this.content        = content.getContent();
            this.contentType    = content.getType();
            this.thumbnailSpec  = content.getThumbnailSpec();
            this.author         = content.getAuthor();
            this.accessUri      = content.getAccessUri();
            this.lastAccessDate = content.getLastAccessDate();
            this.browseCount    = content.getBrowseCount();
            this.bookmarked     = content.isBookmarked();
            this.description    = content.getDescription();
            this.payload        = content.getPayload();
        }

        public Builder() { }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder categoryIdList(final @Nullable List<Long> categoryIdList) {
            this.categoryIdList = categoryIdList;
            return this;
        }

        public Builder title(final @Nullable String title) {
            this.title = title;
            return this;
        }

        public Builder content(final @Nullable String content) {
            this.content = content;
            return this;
        }

        public Builder contentType(final @Nullable ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder thumbnailSpec(final @Nullable ThumbnailSpec thumbnailSpec) {
            this.thumbnailSpec = thumbnailSpec;
            return this;
        }

        public Builder author(final @Nullable Author author) {
            this.author = author;
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

        public Builder bookmarked(final boolean bookmarked) {
            this.bookmarked = bookmarked;
            return this;
        }

        public Builder payload(final @Nullable String payload) {
            this.payload = payload;
            return this;
        }

        public Content create() {
            return new Content(id, categoryIdList, title, content, contentType.value, thumbnailSpec,
                    author, accessUri, lastAccessDate, browseCount, description, bookmarked, payload);
        }
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(final Parcel in) {
            return new Content(in.readLong(),
                    ParcelableUtils.readLongList(in),
                    in.readString(),
                    in.readString(),
                    in.readInt(),
                    (ThumbnailSpec) in.readParcelable(ThumbnailSpec.class.getClassLoader()),
                    (Author) in.readParcelable(Author.class.getClassLoader()),
                    (Uri) in.readParcelable(Uri.class.getClassLoader()),
                    in.readLong(),
                    in.readLong(),
                    in.readString(),
                    in.readByte() != 0,
                    in.readString()
            );
        }

        @Override
        public Content[] newArray(final int size) {
            return new Content[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(id);
        ParcelableUtils.writeLongList(dest, categoryIdList, flags);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeInt(contentType);
        dest.writeParcelable(thumbnailSpec, flags);
        dest.writeParcelable(author, flags);
        dest.writeParcelable(accessUri, flags);
        dest.writeLong(lastAccessDate);
        dest.writeLong(browseCount);
        dest.writeString(description);
        dest.writeByte((byte) (bookmarked ? 1 : 0));
        dest.writeString(payload);
    }

    public static Comparator<Content> TITLE_COMPARATOR = new Comparator<Content>() {
        @Override
        public int compare(final Content lhs, final Content rhs) {
            return lhs.getTitle().compareTo(rhs.getTitle());
        }
    };
}
