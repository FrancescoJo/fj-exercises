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
public final class Author implements Parcelable, DataStorable {
    private static final String SCHEMA_NAME = Author.class.getSimpleName();

    @DataStorable.Value(dataType = DataType.INTEGER, keyType = KeyType.PRIMARY_ROWID)
    private long   id;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String name;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String email;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String bio;

    @DataStorable.Value(dataType = DataType.TEXT)
    private Uri    homepage;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String organisation;

    @DataStorable.Value(dataType = DataType.TEXT)
    private Uri    avatarUri;

    @DataStorable.Value(dataType = DataType.TEXT)
    private String payload;

    // Required by DataStorable protocol
    @SuppressWarnings("unused")
    Author(Cursor cursor) {
        this(MmSdkUtils.getLong(cursor, SCHEMA_NAME, "id"),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "name", ""),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "email", ""),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "bio", ""),
                Uri.parse(MmSdkUtils.getString(cursor, SCHEMA_NAME, "homepage", "")),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "organisation", ""),
                Uri.parse(MmSdkUtils.getString(cursor, SCHEMA_NAME, "avatarUri", "")),
                MmSdkUtils.getString(cursor, SCHEMA_NAME, "payload", "")
        );
    }

    private Author(long id, String name, String email, String bio, Uri homepage,
                   String organisation, Uri avatarUri, String payload) {
        this.id           = id;
        this.name         = name;
        this.email        = email;
        this.bio          = bio;
        this.homepage     = homepage;
        this.organisation = organisation;
        this.avatarUri    = avatarUri;
        this.payload      = payload;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getBio() {
        return bio;
    }

    @Nullable
    public Uri getHomepage() {
        return homepage;
    }

    @Nullable
    public String getOrganisation() {
        return organisation;
    }

    @Nullable
    public Uri getAvatarUri() {
        return avatarUri;
    }

    @Nullable
    public String getPayload() {
        return payload;
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
        private long   id;
        private String name;
        private String email;
        private String bio;
        private Uri    homepage;
        private String organisation;
        private Uri    avatarUri;
        private String payload;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(@Nullable String name) {
            this.name = name;
            return this;
        }

        public Builder email(@Nullable String email) {
            this.email = email;
            return this;
        }

        public Builder bio(@Nullable String bio) {
            this.bio = bio;
            return this;
        }

        public Builder homepage(@Nullable Uri homepage) {
            this.homepage = homepage;
            return this;
        }

        public Builder organisation(@Nullable String organisation) {
            this.organisation = organisation;
            return this;
        }

        public Builder avatarUri(@Nullable Uri avatarUri) {
            this.avatarUri = avatarUri;
            return this;
        }

        public Builder payload(@Nullable String payload) {
            this.payload = payload;
            return this;
        }

        public Author create() {
            return new Author(id, name, email, bio, homepage, organisation, avatarUri, payload);
        }
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel in) {
            return new Author(in.readLong(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    (Uri) in.readParcelable(Uri.class.getClassLoader()),
                    in.readString(),
                    (Uri) in.readParcelable(Uri.class.getClassLoader()),
                    in.readString()
            );
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(bio);
        parcel.writeParcelable(homepage, i);
        parcel.writeString(organisation);
        parcel.writeParcelable(avatarUri, i);
        parcel.writeString(payload);
    }
}
