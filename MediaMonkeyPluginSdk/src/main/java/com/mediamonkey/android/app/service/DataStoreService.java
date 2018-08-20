/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service;

import android.util.LongSparseArray;

import com.mediamonkey.android.app.service.datastore.Order;
import com.mediamonkey.android.app.service.datastore.Where;
import com.mediamonkey.android.lib.util.DataStorable;
import com.mediamonkey.android.lib.util.Function;

import java.util.List;
import java.util.Map;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Dec - 2016
 */
public interface DataStoreService {
    /**
     * Inserts given DataStorable into enclosing schema.
     *
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    <T extends DataStorable> long insert(T value);

    /**
     * Derives a default SELECT operator.
     *
     * @param klass schema name and type information holding class you want to search.
     */
    <T extends DataStorable> OnGoingSelect<T> select(Class<T> klass);

    /**
     * Derives a default UPDATE operator.
     *
     * @param value a value to update in DataStore.
     */
    <T extends DataStorable> OnGoingUpdate<T> update(T value);

    /**
     * Derives a default DELETE operator.
     *
     * @param value a value to delete in DataStore.
     */
    <T extends DataStorable> OnGoingDelete<T> delete(T value);

    /**
     * Derives a default SELECT_COUNT operator.
     */
    <T extends DataStorable> OnGoingSelectCount<T> selectCount(T value);

    /**
     * DataStore WHERE operator wrapper.
     */
    interface OnGoingWhere<T extends DataStorable> {
        /**
         * Specifies search range.
         */
        OnGoingWhere<T> where(Where<?> where);
    }

    /**
     * DataStore SELECT_COUNT operator wrapper.
     */
    interface OnGoingSelectCount<T extends DataStorable> extends OnGoingWhere<T> {
        /**
         * Specifies search range.
         */
        OnGoingSelectCount<T> where(Where<?> where);

        /**
         * Performs search operation. If you're planning to call this method without setting
         * any conditions, this method will automatically looking for a value that is annotated
         * as {@link com.mediamonkey.android.lib.util.DataStorable.KeyType#PRIMARY} or
         * {@link com.mediamonkey.android.lib.util.DataStorable.KeyType#PRIMARY_ROWID}.
         *
         * @return number of record count meets provided condition(s), -1 if error occurred.
         */
        int execute();
    }

    /**
     * DataStore SELECT operator wrapper.
     */
    interface OnGoingSelect<T extends DataStorable> extends OnGoingWhere<T> {
        int DEFAULT_COUNT = 30;

        /**
         * Specifies field(s) for exclusion.
         */
        OnGoingSelect<T> excludeField(String... fieldNames);

        /**
         * Specifies search range.
         */
        OnGoingSelect<T> where(Where<?> where);

        /**
         * Specifies result ordering.
         */
        OnGoingSelect<T> order(Order order);

        /**
         * Specifies result count. Default is {@link com.mediamonkey.android.app.service.DataStoreService.OnGoingSelect#DEFAULT_COUNT}.
         */
        OnGoingSelect<T> count(int count);

        /**
         * Specifies search offset. Default is 0.
         */
        OnGoingSelect<T> offset(int offset);

        /**
         * Performs search operation and wraps the very first result as given type T.
         */
        T executeSingle();

        /**
         * Performs search operation and wraps all results as {@link java.util.List} of T.
         */
        List<T> executeList();

        /**
         * Performs search operation and wraps all results as {@link java.util.Map} of T.
         *
         * @param keyFunction key deriving function which converts T to type K.
         * @param <K>         key type of result type T.
         */
        <K> Map<K, T> executeMap(final Function<T, K> keyFunction);

        /**
         * Performs search operation and wraps all results as {@link android.util.LongSparseArray} of T.
         *
         * @param keyFunction key deriving function which converts T to long.
         */
        LongSparseArray<T> executeLongSparseArray(final Function<T, Long> keyFunction);
    }

    /**
     * DataStore UPDATE operator wrapper.
     * This is a destructive operation with improper condition(s). Use with extreme care.
     */
    interface OnGoingUpdate<T extends DataStorable> extends OnGoingWhere<T> {
        /**
         * Specifies update range.
         */
        OnGoingUpdate<T> where(final Where<?> where);

        /**
         * Performs update operation. If you're planning to call this method without setting
         * any conditions, this method will automatically looking for a value that is annotated
         * as {@link com.mediamonkey.android.lib.util.DataStorable.KeyType#PRIMARY} or
         * {@link com.mediamonkey.android.lib.util.DataStorable.KeyType#PRIMARY_ROWID}.
         *
         * @return number of affected records.
         * @throws UnsupportedOperationException if no condition(s) is/are set and this
         *                                       {@link com.mediamonkey.android.app.service.DataStoreService.OnGoingUpdate}
         *                                       operator is bound to a DataStorable without any primary key.
         */
        int execute() throws UnsupportedOperationException;
    }

    /**
     * DataStore DELETE operator wrapper.
     * This is a destructive operation with improper condition(s). Use with extreme care.
     */
    interface OnGoingDelete<T extends DataStorable> extends OnGoingWhere<T> {
        /**
         * Specifies delete range. Note that, if you want to erase ALL records
         * of T, you must set this as <code>null</code>, to prevent human error.
         */
        OnGoingDelete<T> where(final Where<?> where);

        /**
         * Performs delete operation. If you're planning to call this method without setting
         * any conditions, this method will automatically looking for a value that is annotated
         * as {@link com.mediamonkey.android.lib.util.DataStorable.KeyType#PRIMARY} or
         * {@link com.mediamonkey.android.lib.util.DataStorable.KeyType#PRIMARY_ROWID}.
         *
         * @throws UnsupportedOperationException if no condition(s) is/are set and this
         *                                       {@link com.mediamonkey.android.app.service.DataStoreService.OnGoingDelete}
         *                                       operator is bound to a DataStorable without any primary key.
         */
        boolean execute() throws UnsupportedOperationException;
    }
}
