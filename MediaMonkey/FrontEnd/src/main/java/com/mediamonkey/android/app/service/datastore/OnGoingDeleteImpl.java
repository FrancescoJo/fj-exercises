/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mediamonkey.android.app.service.DataStoreService;
import com.mediamonkey.android.lib.util.DataStorable;

import timber.log.Timber;

import static com.mediamonkey.android.app.service.datastore.SQLBaseHelper.executeSql;
import static com.mediamonkey.android.app.service.datastore.SQLDeleteHelper.sqlDelete;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
class OnGoingDeleteImpl<T extends DataStorable> extends OnGoingWhereImpl<T>
        implements DataStoreService.OnGoingDelete<T> {
    private final T value;

    OnGoingDeleteImpl(T value, SQLiteDatabase db) {
        super(db);
        this.value = value;
    }

    @Override
    public DataStoreService.OnGoingDelete<T> where(Where<?> where) {
        return (DataStoreService.OnGoingDelete<T>) super.where(where);
    }

    @Override
    public boolean execute() throws IllegalStateException {
        String sql = sqlDelete(value, getConditions());
        try {
            executeSql(getDatabase(), sql);
            return true;
        } catch (SQLException ignore) {
            Timber.d(ignore);
            return false;
        }
    }
}
