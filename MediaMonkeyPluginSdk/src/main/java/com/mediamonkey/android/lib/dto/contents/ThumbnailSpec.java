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

import com.mediamonkey.android.lib.internal.EqualsBuilder;
import com.mediamonkey.android.lib.internal.HashCodeBuilder;
import com.mediamonkey.android.lib.internal.MmSdkUtils;
import com.mediamonkey.android.lib.util.DataStorable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
public final class ThumbnailSpec implements Parcelable, DataStorable {
    private static final String SCHEMA_NAME = ThumbnailSpec.class.getSimpleName();

    @DataStorable.Value(dataType = DataType.INTEGER, keyType = KeyType.PRIMARY_ROWID)
    private long   id;

    @DataStorable.Value(dataType = DataType.REAL)
    private float  width;

    @DataStorable.Value(dataType = DataType.REAL)
    private float  height;

    @DataStorable.Value(dataType = DataType.TEXT)
    private Uri    thumbnailUri;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String payload;

    // Required by DataStorable protocol
    @SuppressWarnings("unused")
    ThumbnailSpec(Cursor cursor) {
        this(MmSdkUtils.getLong(cursor, SCHEMA_NAME, "id"),
                MmSdkUtils.getFloat(cursor, SCHEMA_NAME, "width"),
                MmSdkUtils.getFloat(cursor, SCHEMA_NAME, "height"),
                Uri.parse(MmSdkUtils.getString(cursor, SCHEMA_NAME, "thumbnailUri", "")),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "payload", "")
        );
    }

    private ThumbnailSpec(long id, float width, float height, Uri thumbnailUri, String payload) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.thumbnailUri = thumbnailUri;
        this.payload = payload;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public @Nullable Uri getThumbnailUrl() {
        return thumbnailUri;
    }

    public @Nullable String getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    public static class Builder {
        private long   id;
        private float  width;
        private float  height;
        private Uri    thumbnailUrl;
        private String payload;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        /**
         * @param width Thumbnail width in DP unit,
         *              {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT} or
         *              {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}.
         */
        public Builder width(float width) {
            this.width = width;
            return this;
        }

        /**
         * @param height Thumbnail height in DP unit,
         *               {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT} or
         *               {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}.
         */
        public Builder height(float height) {
            this.height = height;
            return this;
        }

        public Builder thumbnailUri(@Nullable Uri thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Builder payload(@Nullable String payload) {
            this.payload = payload;
            return this;
        }

        public ThumbnailSpec create() {
            return new ThumbnailSpec(id, width, height, thumbnailUrl, payload);
        }
    }

    public static final Creator<ThumbnailSpec> CREATOR = new Creator<ThumbnailSpec>() {
        @Override
        public ThumbnailSpec createFromParcel(Parcel in) {
            return new ThumbnailSpec(in.readLong(),
                    in.readFloat(),
                    in.readFloat(),
                    (Uri) in.readParcelable(Uri.class.getClassLoader()),
                    in.readString()
            );
        }

        @Override
        public ThumbnailSpec[] newArray(int size) {
            return new ThumbnailSpec[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeFloat(width);
        parcel.writeFloat(height);
        parcel.writeParcelable(thumbnailUri, i);
        parcel.writeString(payload);
    }

    public static @Nullable Uri getThumbnailUri(@Nullable ThumbnailSpec thumbnailSpecDto) {
        return null != thumbnailSpecDto ? thumbnailSpecDto.getThumbnailUrl() : null;
    }
}
