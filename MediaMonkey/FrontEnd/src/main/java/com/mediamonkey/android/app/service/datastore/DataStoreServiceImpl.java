/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.fj.android.mediamonkey.BuildConfig;
import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.dao.sharedpref.AppConfigPref;
import com.fj.android.mediamonkey.dto.PluginManifestDto;
import com.fj.android.mediamonkey.util.io.IOUtils;
import com.mediamonkey.android.app.service.DataStoreService;
import com.mediamonkey.android.lib.util.DataStorable;
import com.mediamonkey.android.plugin.DataStoreInterface;

import timber.log.Timber;

import static com.mediamonkey.android.app.service.datastore.SQLCreateHelper.sqlInsertValues;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Dec - 2016
 */
public class DataStoreServiceImpl implements DataStoreService {
    private String         dbName;
    private SQLiteDatabase db;

    public DataStoreServiceImpl(PluginManifestDto manifest) {
        this.dbName = manifest.getPackageName().replaceAll("\\.", "_");
    }

    public void onPluginRequestsDatabase(DataStoreInterface dsInterface) {
        DBOpenHelper helper = new DBOpenHelper(MediaMonkeyApplication.getInstance(), dsInterface, dbName);
        if (null != db) {
            Timber.d("Closing old connection: %s", db);
            IOUtils.closeQuietly(db);
        }
        this.db = helper.getWritableDatabase();
        Timber.d("Connected database: %s", db);
        AppConfigPref pref = MediaMonkeyApplication.getInstance().getAppComponent().sharedPrefMgr().getAppConfig();

        // To send 'create' signal explicitly
        Timber.d("Database created  ? %s", pref.isDatabaseCreated(dbName));
        if (!pref.isDatabaseCreated(dbName)) {
            helper.onCreate(db);
            pref.setDatabaseCreated(dbName);
        }
    }

    public SQLiteDatabase getConnectedDatabase() {
        return db;
    }

    @Override
    public <T extends DataStorable> long insert(T value) {
        ContentValues contentValues = sqlInsertValues(value);
        String tableName = value.getClass().getSimpleName();
        if (BuildConfig.DEBUG) {
            Timber.v("SQL Insert: %s, values: %s", tableName, contentValues);
            long time1 = System.currentTimeMillis();
            long rowId = db.insert(tableName, null, contentValues);
            long delta = System.currentTimeMillis() - time1;
            Timber.v("SQL execution time: %s ms", delta);
            return rowId;
        } else {
            return db.insert(tableName, null, contentValues);
        }
    }

    @Override
    public <T extends DataStorable> OnGoingSelect<T> select(Class<T> klass) {
        return new OnGoingSelectImpl<>(klass, db);
    }

    @Override
    public <T extends DataStorable> OnGoingUpdate<T> update(T value) {
        return new OnGoingUpdateImpl<>(value, db);
    }

    @Override
    public <T extends DataStorable> OnGoingDelete<T> delete(T value) {
        return new OnGoingDeleteImpl<>(value, db);
    }

    @Override
    public <T extends DataStorable> OnGoingSelectCount<T> selectCount(T value) {
        return new OnGoingSelectCountImpl<>(value, db);
    }
}
