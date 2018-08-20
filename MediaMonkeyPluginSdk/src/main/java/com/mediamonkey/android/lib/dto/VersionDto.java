/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mediamonkey.android.lib.internal.ComparatorChain;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Nov - 2016
 */
public final class VersionDto implements Comparable<VersionDto>, Parcelable {
    private static final Comparator<VersionDto> COMPARATOR_MAJOR_VER = new Comparator<VersionDto>() {
        @Override
        public int compare(VersionDto o1, VersionDto o2) {
            return compareInt(o1.getMajorVersion(), o2.getMajorVersion());
        }
    };

    private static final Comparator<VersionDto> COMPARATOR_MINOR_VER = new Comparator<VersionDto>() {
        @Override
        public int compare(VersionDto o1, VersionDto o2) {
            return compareInt(o1.getMinorVersion(), o2.getMinorVersion());
        }
    };

    private static final Comparator<VersionDto> COMPARATOR_BUILD_NUM = new Comparator<VersionDto>() {
        @Override
        public int compare(VersionDto o1, VersionDto o2) {
            return compareInt(o1.getBuildNumber(), o2.getBuildNumber());
        }
    };

    private static final ComparatorChain<VersionDto> COMPARATORS = new ComparatorChain<>(
            Arrays.asList(COMPARATOR_MAJOR_VER,
                    COMPARATOR_MINOR_VER,
                    COMPARATOR_BUILD_NUM)
    );

    private final String name;
    private final int majorVer;
    private final int minorVer;
    private final int buildNo;

    public @Nullable String getVersionName() {
        return name;
    }

    public int getMajorVersion() {
        return majorVer;
    }

    public int getMinorVersion() {
        return minorVer;
    }

    public int getBuildNumber() {
        return buildNo;
    }

    private static int compareInt(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    VersionDto(String name, int major, int minor, int buildNo) {
        this.name = name;
        this.majorVer = major;
        this.minorVer = minor;
        this.buildNo = buildNo;
    }

    public static class Builder {
        private String name;
        private int majorVer;
        private int minorVer;
        private int buildNo;

        public Builder versionName(@Nullable String name) {
            this.name = name;
            return this;
        }

        public Builder majorVer(int version) {
            this.majorVer = version;
            return this;
        }

        public Builder minorVer(int version) {
            this.minorVer = version;
            return this;
        }

        public Builder buildNumber(int version) {
            this.buildNo = version;
            return this;
        }

        public VersionDto create() {
            return new VersionDto(name, majorVer, minorVer, buildNo);
        }
    }

    public static final Creator<VersionDto> CREATOR = new Creator<VersionDto>() {
        @Override
        public VersionDto createFromParcel(Parcel in) {
            return new VersionDto(in.readString(),
                    in.readInt(),
                    in.readInt(),
                    in.readInt()
            );
        }

        @Override
        public VersionDto[] newArray(int size) {
            return new VersionDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(majorVer);
        parcel.writeInt(minorVer);
        parcel.writeInt(buildNo);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(majorVer).append(".").append(minorVer).append(".").append(buildNo);
        if (!TextUtils.isEmpty(name)) {
            sb.append("(").append(name).append(")");
        }

        return sb.toString();
    }

    @Override
    public int compareTo(@NonNull VersionDto o) {
        return COMPARATORS.compare(this, o);
    }
}
