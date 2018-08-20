/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fj.android.mediamonkey.BuildConfig;
import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.dao.sharedpref.AppConfigPref;
import com.fj.android.mediamonkey.util.lang.EmptyCheckUtils;
import com.mediamonkey.android.lib.dto.contents.Author;
import com.mediamonkey.android.lib.dto.contents.Category;
import com.mediamonkey.android.lib.dto.contents.Chapter;
import com.mediamonkey.android.lib.dto.contents.ChapterInventory;
import com.mediamonkey.android.lib.dto.contents.Content;
import com.mediamonkey.android.lib.dto.contents.ThumbnailSpec;
import com.mediamonkey.android.lib.util.DataStorable;
import com.mediamonkey.android.plugin.DataStoreInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

import static com.mediamonkey.android.app.service.datastore.SQLBaseHelper.executeSql;
import static com.mediamonkey.android.app.service.datastore.SQLCreateHelper.sqlCreate;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Dec - 2016
 */
class DBOpenHelper extends SQLiteOpenHelper {
    private DataStoreInterface dsInterface;
    private String             dbName;

    DBOpenHelper(Context context, DataStoreInterface dsInterface, String dbName) {
        super(context, dbName, null, dsInterface.getDataStoreVersion());
        this.dsInterface = dsInterface;
        this.dbName      = dbName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<Class<? extends DataStorable>> schemaList = new ArrayList<>(Arrays.<Class<? extends DataStorable>>asList(
                Author.class, Category.class, Content.class, ThumbnailSpec.class,
                Chapter.class, ChapterInventory.class
        ));
        List<Class<? extends DataStorable>> storables = dsInterface.getStorables();
        if (!EmptyCheckUtils.isEmpty(storables)) {
            schemaList.addAll(storables);
        }

        if (BuildConfig.DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("Creating database with schemas: \n");
            for (Class<? extends DataStorable> klass : schemaList) {
                sb.append(klass).append("\n");
            }
            sb.append("Database version: ").append(dsInterface.getDataStoreVersion());
            Timber.d(sb.toString());
        }

        for (Class<? extends DataStorable> schemaClass : schemaList) {
            String dropSql = "DROP TABLE IF EXISTS " + schemaClass.getSimpleName();
            executeSql(db, dropSql);

            String createSql = sqlCreate(schemaClass);
            executeSql(db, createSql);
        }

        AppConfigPref pref = MediaMonkeyApplication.getInstance().getAppComponent().sharedPrefMgr().getAppConfig();
        pref.setDatabaseCreated(dbName);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}