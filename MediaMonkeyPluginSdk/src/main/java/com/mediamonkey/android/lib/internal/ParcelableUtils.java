/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
public final class ParcelableUtils {
    public static void writeIntList(Parcel dest, List<Integer> intList, int flags) {
        ParcelableIntList pil = new ParcelableIntList(intList);
        dest.writeParcelable(pil, flags);
    }

    public static List<Integer> readIntList(Parcel in) {
        ParcelableIntList pil = in.readParcelable(ParcelableIntList.class.getClassLoader());
        return pil.getList();
    }

    public static void writeLongList(Parcel dest, List<Long> intList, int flags) {
        ParcelableLongList pil = new ParcelableLongList(intList);
        dest.writeParcelable(pil, flags);
    }

    public static List<Long> readLongList(Parcel in) {
        ParcelableLongList pil = in.readParcelable(ParcelableIntList.class.getClassLoader());
        return pil.getList();
    }

    public static <T extends Parcelable> void writeLongSparseArray(Parcel dest, LongSparseArray<T> list, int flags) {
        ParcelableLongSparseArray plsa = new ParcelableLongSparseArray<>(list);
        dest.writeParcelable(plsa, flags);
    }

    public static <T extends Parcelable> LongSparseArray<T> readLongSparseArray(Parcel in) {
        ParcelableLongSparseArray<T> plsa = in.readParcelable(ParcelableLongSparseArray.class.getClassLoader());
        return plsa.getLongSparseArray();
    }

    private ParcelableUtils() { }
}

abstract class ParcelableList<T> implements Parcelable {
    private List<T> list;

    ParcelableList(List<T> list) {
        this.list = list;
    }

    ParcelableList(Parcel in) {
        int size = in.readInt();
        this.list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(readFromParcel(in));
        }
    }

    List<T> getList() {
        return list;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(list.size());
        for (T value : list) {
            writeToParcel(dest, value, flags);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected abstract T readFromParcel(Parcel in);

    protected abstract void writeToParcel(Parcel dest, T value, int flags);
}

class ParcelableIntList extends ParcelableList<Integer> {
    ParcelableIntList(List<Integer> list) {
        super(list);
    }

    private ParcelableIntList(Parcel in) {
        super(in);
    }

    @Override
    protected Integer readFromParcel(Parcel in) {
        return in.readInt();
    }

    @Override
    protected void writeToParcel(Parcel dest, Integer value, int flags) {
        dest.writeInt(value);
    }

    public static final Creator<ParcelableIntList> CREATOR = new Creator<ParcelableIntList>() {
        @Override
        public ParcelableIntList createFromParcel(Parcel in) {
            return new ParcelableIntList(in);
        }

        @Override
        public ParcelableIntList[] newArray(int size) {
            return new ParcelableIntList[size];
        }
    };
}

class ParcelableLongList extends ParcelableList<Long> {
    ParcelableLongList(List<Long> list) {
        super(list);
    }

    private ParcelableLongList(Parcel in) {
        super(in);
    }

    @Override
    protected Long readFromParcel(Parcel in) {
        return in.readLong();
    }

    @Override
    protected void writeToParcel(Parcel dest, Long value, int flags) {
        dest.writeLong(value);
    }

    public static final Creator<ParcelableLongList> CREATOR = new Creator<ParcelableLongList>() {
        @Override
        public ParcelableLongList createFromParcel(Parcel in) {
            return new ParcelableLongList(in);
        }

        @Override
        public ParcelableLongList[] newArray(int size) {
            return new ParcelableLongList[size];
        }
    };
}


class ParcelableLongSparseArray<T extends Parcelable> implements Parcelable {
    private LongSparseArray<T> list;

    ParcelableLongSparseArray(LongSparseArray<T> list) {
        this.list = list;
    }

    private ParcelableLongSparseArray(Parcel in) {
        int size = in.readInt();
        this.list = new LongSparseArray<>(size);

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                Entry<T> entry = in.readParcelable(Entry.class.getClassLoader());
                list.put(entry.key, entry.value);
            }
        }
    }

    LongSparseArray<T> getLongSparseArray() {
        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = list.size();
        dest.writeInt(size);

        for (int i = 0; i < size; i++) {
            long key = list.keyAt(i);
            T value  = list.get(key);
            dest.writeParcelable(new Entry<>(key, value), flags);
        }
    }

    public static final Creator<ParcelableLongSparseArray> CREATOR = new Creator<ParcelableLongSparseArray>() {
        @Override
        public ParcelableLongSparseArray createFromParcel(Parcel in) {
            return new ParcelableLongSparseArray(in);
        }

        @Override
        public ParcelableLongSparseArray[] newArray(int size) {
            return new ParcelableLongSparseArray[size];
        }
    };

    private static class Entry<T extends Parcelable> implements Parcelable {
        long   key;
        T      value;
        private String className;

        Entry(long key, T value) {
            this.key   = key;
            this.value = value;
            if (null != value) {
                className = value.getClass().getCanonicalName();
            } else {
                className = "";
            }
        }

        private Entry(Parcel in) {
            this.key = in.readLong();
            this.className = in.readString();
            if (!TextUtils.isEmpty(className)) {
                try {
                    @SuppressWarnings("unchecked")
                    Class<T> klass = (Class<T>) Class.forName(className);
                    this.value = in.readParcelable(klass.getClassLoader());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Class of name " + className + " is not found via current class loader", e);
                }
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(key);
            dest.writeString(className);
            dest.writeParcelable(value, flags);
        }

        public static final Creator<Entry> CREATOR = new Creator<Entry>() {
            @Override
            public Entry createFromParcel(Parcel in) {
                return new Entry(in);
            }

            @Override
            public Entry[] newArray(int size) {
                return new Entry[size];
            }
        };
    }
}