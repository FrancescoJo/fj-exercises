/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.database.sqlite.SQLiteDatabase;

import com.mediamonkey.android.app.service.DataStoreService;
import com.mediamonkey.android.lib.util.DataStorable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
class OnGoingUpdateImpl<T extends DataStorable> extends OnGoingWhereImpl<T>
        implements DataStoreService.OnGoingUpdate<T> {
    OnGoingUpdateImpl(T value, SQLiteDatabase db) {
        super(db);
    }

    @Override
    public DataStoreService.OnGoingUpdate<T> where(Where<?> where) {
        return (DataStoreService.OnGoingUpdate<T>) super.where(where);
    }

    @Override
    public int execute() throws UnsupportedOperationException {
        return 0;
    }
}
