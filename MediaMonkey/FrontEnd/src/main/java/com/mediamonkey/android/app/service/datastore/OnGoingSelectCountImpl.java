/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.fj.android.mediamonkey.util.io.IOUtils;
import com.mediamonkey.android.app.service.DataStoreService;
import com.mediamonkey.android.lib.util.DataStorable;

import timber.log.Timber;

import static com.mediamonkey.android.app.service.datastore.SQLBaseHelper.executeCursor;
import static com.mediamonkey.android.app.service.datastore.SQLSelectHelper.sqlSelectCount;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
class OnGoingSelectCountImpl<T extends DataStorable> extends OnGoingWhereImpl<T>
        implements DataStoreService.OnGoingSelectCount<T> {
    private final T value;

    OnGoingSelectCountImpl(T value, SQLiteDatabase db) {
        super(db);
        this.value = value;
        super.where(Where.EMPTY);
    }

    @Override
    public DataStoreService.OnGoingSelectCount<T> where(Where<?> where) {
        return (DataStoreService.OnGoingSelectCount<T>) super.where(where);
    }

    @Override
    public int execute() {
        String sql = sqlSelectCount(value, getConditions());
        Cursor cursor = null;
        try {
            cursor = executeCursor(getDatabase(), sql);
            if (cursor.moveToNext()) {
                return cursor.getInt(0);
            } else {
                return -1;
            }
        } catch (SQLException ignore) {
            Timber.d(ignore);
            return -1;
        } finally {
            IOUtils.closeQuietly(cursor);
        }
    }
}
