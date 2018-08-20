/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.fj.android.mediamonkey.BuildConfig;
import com.fj.android.mediamonkey.util.io.IOUtils;
import com.fj.android.mediamonkey.util.lang.ValueReference;
import com.mediamonkey.android.lib.util.Consumer;
import com.mediamonkey.android.lib.util.DataStorable;
import com.mediamonkey.android.lib.util.Predicate;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Dec - 2016
 */
class SQLBaseHelper {
    static Cursor executeCursor(SQLiteDatabase db, String sql) {
        if (BuildConfig.DEBUG) {
            Timber.v("SQL: %s", sql);
            long time1 = System.currentTimeMillis();
            Cursor cursor = db.rawQuery(sql, null);
            long delta = System.currentTimeMillis() - time1;
            Timber.v("SQL execution time: %s ms", delta);
            return cursor;
        } else {
            return db.rawQuery(sql, null);
        }
    }

    static void executeSql(SQLiteDatabase db, String sql) {
        Timber.v("SQL: %s", sql);
        long time1 = System.currentTimeMillis();
        db.execSQL(sql);
        long delta = System.currentTimeMillis() - time1;
        Timber.v("SQL execution time: %s ms", delta);
    }

    static <T extends DataStorable> void forEachRow(final SQLiteDatabase db, String sql,
                                                    final Class<T> klass, final Consumer<T> consumer) {
        Constructor<T> classInstantiator = getDataStorableConstructor(klass);
        Cursor cursor = null;
        try {
            cursor = executeCursor(db, sql);
            while (cursor.moveToNext()) {
                try {
                    AccessibleObject.setAccessible(new AccessibleObject[]{classInstantiator}, true);
                    T data = classInstantiator.newInstance(cursor);
                    consumer.accept(data);
                } catch (Exception ignore) {
                    throw new UnsupportedOperationException("Impossible thing has happen", ignore);
                }
            }
        } finally {
            IOUtils.closeQuietly(cursor);
        }
    }


    static <T extends DataStorable> String getPrimaryKeyName(T dataStorable) {
        @SuppressWarnings("unchecked")
        Class<T> klass = (Class<T>) dataStorable.getClass();
        final ValueReference<String> pkNameRef = new ValueReference<>();
        forEachValueField(klass, new Predicate<Field>() {
            @Override
            public boolean test(Field field) {
                DataStorable.Value columnInfo = field.getAnnotation(DataStorable.Value.class);
                if (null == columnInfo) {
                    return true;
                }

                if (DataStorable.KeyType.PRIMARY == columnInfo.keyType() ||
                        DataStorable.KeyType.PRIMARY_ROWID == columnInfo.keyType()) {
                    pkNameRef.set(field.getName());
                    return false;
                }

                return true;
            }
        });

        return pkNameRef.get();
    }

    static <T extends DataStorable> Object getPrimaryValue(T dataStorable, String primaryKeyName) {
        @SuppressWarnings("unchecked")
        Class<T> klass = (Class<T>) dataStorable.getClass();
        try {
            Field pkField = klass.getDeclaredField(primaryKeyName);
            AccessibleObject.setAccessible(new AccessibleObject[]{pkField}, true);
            return pkField.get(dataStorable);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param foreignField must be annotated as {@link DataStorable.KeyType#FOREIGN}.
     *                     This is not a public API!
     */
    static <T extends DataStorable> Object getForeignValue(T dataStorable, Field foreignField)
            throws IllegalAccessException, NoSuchFieldException {
        DataStorable.Value columnInfo = foreignField.getAnnotation(DataStorable.Value.class);
        String conjunction = columnInfo.reference();
        String fieldName = conjunction.substring(conjunction.indexOf("_") + 1);
        Object foreignObject = foreignField.get(dataStorable);
        if (null == foreignObject) {
            Timber.v("[getForeignValue] %s declared as FOREIGN KEY but has no value in %s", conjunction, dataStorable);
            return null;
        }

        Class<?> foreignClass = foreignObject.getClass();
        Field foreignKeyField = foreignClass.getDeclaredField(fieldName);
        AccessibleObject.setAccessible(new AccessibleObject[]{foreignKeyField}, true);
        return foreignKeyField.get(foreignObject);
    }

    static <T extends DataStorable> void forEachValueField(final Class<T> klass,
                                                           final Consumer<Field> fieldConsumer) {
        forEachValueField(klass, new Predicate<Field>() {
            @Override
            public boolean test(Field field) {
                fieldConsumer.accept(field);
                return true;
            }
        });
    }

    static <T extends DataStorable> void forEachValueField(Class<T> klass,
                                                           Predicate<Field> iterationPredicate) {
        Field[] fields = klass.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (Field field : fields) {
            DataStorable.Value columnInfo = field.getAnnotation(DataStorable.Value.class);
            if (null == columnInfo) {
                Timber.v("Skipping non-DataStorable.Value field: %s", field);
                continue;
            }

            if (!iterationPredicate.test(field)) {
                return;
            }
        }
    }

    private static <T extends DataStorable> Constructor<T> getDataStorableConstructor(Class<T> klass) {
        try {
            return klass.getDeclaredConstructor(Cursor.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(klass + " does not has: <init>(android.database.Cursor)");
        }
    }

    static String valueAsSQL(Object value) {
        if (null == value) {
            return "NULL";
        } else if (isBooleanType(value)) {
            boolean bVal = (boolean) value;
            return '\'' + convertBoolean(bVal) + '\'';
        } else {
            return DatabaseUtils.sqlEscapeString(value.toString());
        }
    }

    private static boolean isBooleanType(Object value) {
        return value.getClass() == boolean.class || value.getClass() == Boolean.class;
    }

    private static String convertBoolean(boolean value) {
        return value ? "1" : "0";
    }
}
