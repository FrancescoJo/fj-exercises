/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto.contents;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.mediamonkey.android.lib.dto.ContentType;
import com.mediamonkey.android.lib.internal.EqualsBuilder;
import com.mediamonkey.android.lib.internal.HashCodeBuilder;
import com.mediamonkey.android.lib.internal.HashCodeExclude;
import com.mediamonkey.android.lib.internal.MmSdkUtils;
import com.mediamonkey.android.lib.util.DataStorable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Dec - 2016
 */
public final class ChapterInventory implements Parcelable, DataStorable {
    private static final String SCHEMA_NAME = ChapterInventory.class.getSimpleName();

    @DataStorable.Value(dataType = DataStorable.DataType.INTEGER, keyType = DataStorable.KeyType.PRIMARY)
    private long id;

    // Linked to Chapter_id
    @DataStorable.Value(dataType = DataStorable.DataType.INTEGER)
    private long chapterId;

    @DataStorable.Value(dataType = DataStorable.DataType.INTEGER)
    private int typeInt;

    @DataStorable.Value(dataType = DataStorable.DataType.TEXT)
    private Uri resourceUri;

    @DataStorable.Value(dataType = DataStorable.DataType.TEXT)
    private String payload;

    @HashCodeExclude
    private transient ContentType type;

    // Required by DataStorable protocol
    @SuppressWarnings("unused")
    ChapterInventory(final Cursor cursor) {
        this(MmSdkUtils.getLong(cursor, SCHEMA_NAME, "id"),
                MmSdkUtils.getLong(cursor, SCHEMA_NAME, "chapterId"),
                MmSdkUtils.getInt(cursor, SCHEMA_NAME, "type"),
                Uri.parse(MmSdkUtils.getString(cursor, SCHEMA_NAME, "resourceUri", "")),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "payload", ""));
    }

    public ChapterInventory(final long chapterId, final ContentType type,
                            final Uri resourceUri, final String payload) {
        this(-1L, chapterId, type.value, resourceUri, payload);
    }

    private ChapterInventory(final long id, final long chapterId, final int typeInt,
                             final Uri resourceUri, final String payload) {
        this.id          = id;
        this.chapterId   = chapterId;
        this.typeInt     = typeInt;
        this.payload     = payload;
        this.resourceUri = resourceUri;
        this.type        = ContentType.byValue(typeInt);
    }

    public static final Creator<ChapterInventory> CREATOR = new Creator<ChapterInventory>() {
        @Override
        public ChapterInventory createFromParcel(final Parcel in) {
            return new ChapterInventory(in.readLong(),
                    in.readLong(),
                    in.readInt(),
                    (Uri) in.readParcelable(Uri.class.getClassLoader()),
                    in.readString());
        }

        @Override
        public ChapterInventory[] newArray(final int size) {
            return new ChapterInventory[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getChapterId() {
        return chapterId;
    }

    public ContentType getType() {
        return type;
    }

    public Uri getResourceUri() {
        return resourceUri;
    }

    public String getPayload() {
        return payload;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(id);
        dest.writeLong(chapterId);
        dest.writeInt(typeInt);
        dest.writeParcelable(resourceUri, flags);
        dest.writeString(payload);
    }
}
