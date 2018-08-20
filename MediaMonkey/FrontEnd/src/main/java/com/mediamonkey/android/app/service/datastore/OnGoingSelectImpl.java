/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.database.sqlite.SQLiteDatabase;
import android.util.LongSparseArray;

import com.fj.android.mediamonkey.util.lang.ValueReference;
import com.mediamonkey.android.app.service.DataStoreService;
import com.mediamonkey.android.lib.util.Consumer;
import com.mediamonkey.android.lib.util.DataStorable;
import com.mediamonkey.android.lib.util.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.mediamonkey.android.app.service.datastore.SQLBaseHelper.forEachRow;
import static com.mediamonkey.android.app.service.datastore.SQLSelectHelper.sqlSelect;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
class OnGoingSelectImpl<T extends DataStorable> extends OnGoingWhereImpl<T>
        implements DataStoreService.OnGoingSelect<T> {
    private Class<T>     klass;
    private List<String> fieldExclusions;
    private Order        order;
    private int          count;
    private int          offset;

    OnGoingSelectImpl(final Class<T> klass, final SQLiteDatabase db) {
        super(db);
        this.klass = klass;
        this.count = DEFAULT_COUNT;
    }

    @Override
    public DataStoreService.OnGoingSelect<T> excludeField(String... fieldNames) {
        this.fieldExclusions = Arrays.asList(fieldNames);
        return this;
    }

    @Override
    public DataStoreService.OnGoingSelect<T> where(final Where<?> where) {
        return (DataStoreService.OnGoingSelect<T>) super.where(where);
    }

    @Override
    public DataStoreService.OnGoingSelect<T> order(final Order order) {
        this.order = order;
        return this;
    }

    @Override
    public DataStoreService.OnGoingSelect<T> count(final int count) {
        this.count = count;
        return this;
    }

    @Override
    public DataStoreService.OnGoingSelect<T> offset(final int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public List<T> executeList() {
        String sql = sqlSelect(klass, fieldExclusions, getConditions(), order, count, offset);
        final List<T> list = new ArrayList<>();
        forEachRow(getDatabase(), sql, klass, new Consumer<T>() {
            @Override
            public void accept(T value) {
                list.add(value);
            }
        });
        return list;
    }

    @Override
    public <K> Map<K, T> executeMap(final Function<T, K> keyFunction) {
        String sql = sqlSelect(klass, fieldExclusions, getConditions(), order, count, offset);
        final Map<K, T> map = new TreeMap<>();
        forEachRow(getDatabase(), sql, klass, new Consumer<T>() {
            @Override
            public void accept(T value) {
                map.put(keyFunction.apply(value), value);
            }
        });
        return map;
    }

    @Override
    public LongSparseArray<T> executeLongSparseArray(final Function<T, Long> keyFunction) {
        String sql = sqlSelect(klass, fieldExclusions, getConditions(), order, count, offset);
        final LongSparseArray<T> map = new LongSparseArray<>();
        forEachRow(getDatabase(), sql, klass, new Consumer<T>() {
            @Override
            public void accept(T value) {
                map.put(keyFunction.apply(value), value);
            }
        });
        return map;
    }

    @Override
    public T executeSingle() {
        String sql = sqlSelect(klass, fieldExclusions, getConditions(), order, 1, offset);
        final ValueReference<T> ref = new ValueReference<>();
        forEachRow(getDatabase(), sql, klass, new Consumer<T>() {
            @Override
            public void accept(T value) {
                ref.set(value);
            }
        });
        return ref.get();
    }
}
