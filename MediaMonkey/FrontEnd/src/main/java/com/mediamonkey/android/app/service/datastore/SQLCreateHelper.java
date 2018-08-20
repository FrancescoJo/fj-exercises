/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.content.ContentValues;
import android.text.TextUtils;

import com.fj.android.mediamonkey.util.lang.ValueReference;
import com.mediamonkey.android.lib.util.Consumer;
import com.mediamonkey.android.lib.util.DataStorable;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import timber.log.Timber;

import static com.mediamonkey.android.app.service.datastore.SQLBaseHelper.forEachValueField;
import static com.mediamonkey.android.app.service.datastore.SQLBaseHelper.getForeignValue;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
class SQLCreateHelper {
    static <T extends DataStorable> String sqlCreate(final Class<T> schemaClass) {
        String tableName = schemaClass.getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE `").append(tableName).append("` (");
        final ValueReference<Boolean> hasPrimaryKey = new ValueReference<>(false);
        final ValueReference<Boolean> useRowId = new ValueReference<>(false);
        final List<String> clauses = new ArrayList<>();
        Timber.v("[sqlCreateTable] Iterating over fields");
        forEachValueField(schemaClass, new Consumer<Field>() {
            @Override
            public void accept(Field field) {
                DataStorable.Value columnInfo = field.getAnnotation(DataStorable.Value.class);
                DataStorable.KeyType keyType = columnInfo.keyType();
                if (isPrimaryKey(keyType)) {
                    if (hasPrimaryKey.get()) {
                        throw new UnsupportedOperationException(schemaClass + "has more than 2 PRIMARY KEY fields!!");
                    } else {
                        hasPrimaryKey.set(true);
                    }
                }

                DataStorable.DataType dataType = columnInfo.dataType();
                String columnName = field.getName();
                Timber.v("[sqlCreateTable] Annotated as DataStorable.Value field: %s(%s, %s)", columnName, dataType, keyType);


                StringBuilder sb2 = new StringBuilder();
                sb2.append("`").append(columnName).append("`").append(" ").append(dataType.value);
                switch (keyType) {
                    case PRIMARY:
                        sb2.append(" PRIMARY KEY");
                        break;
                    case PRIMARY_ROWID:
                        sb2.append(" PRIMARY KEY AUTOINCREMENT");
                        useRowId.set(true);
                        break;
                    default:
                }

                clauses.add(sb2.toString());
            }

            private boolean isPrimaryKey(DataStorable.KeyType keyType) {
                return DataStorable.KeyType.PRIMARY == keyType || DataStorable.KeyType.PRIMARY_ROWID == keyType;
            }
        });

        if (!hasPrimaryKey.get()) {
            throw new IllegalArgumentException(String.format("Schema %s does not have any key annotated as PRIMARY KEY!!", tableName));
        }

        sb.append(TextUtils.join(",", clauses)).append(");");
        return sb.toString();
    }

    static <T extends DataStorable> ContentValues sqlInsertValues(final T dataStorable) {
        @SuppressWarnings("unchecked")
        Class<T> klass = (Class<T>) dataStorable.getClass();

        final ContentValues values = new ContentValues();
        forEachValueField(klass, new Consumer<Field>() {
            @Override
            public void accept(Field field) {
                DataStorable.Value columnInfo = field.getAnnotation(DataStorable.Value.class);
                try {
                    Object value;
                    if (DataStorable.KeyType.PRIMARY_ROWID == columnInfo.keyType()) {
                        Timber.v("[sqlInsert] Skipping primary key field %s, schema is using row id", field.getName());
                        return;
                    } else if (DataStorable.KeyType.FOREIGN == columnInfo.keyType()) {
                        value = getForeignValue(dataStorable, field);
                    } else {
                        value = field.get(dataStorable);
                    }

                    String valueStr;
                    if (null == value) {
                        valueStr = null;
                    } else {
                        if (value.getClass().isArray()) {
                            valueStr = flattenArray(value);
                        } else if (value instanceof Collection) {
                            valueStr = flattenCollection((Collection) value);
                        } else {
                            valueStr = value.toString();
                        }
                    }

                    values.put(field.getName(), valueStr);
                } catch (Exception ignore) {
                    throw new UnsupportedOperationException("Impossible thing has happen", ignore);
                }
            }

            private String flattenArray(Object array) {
                List<String> flatList = new ArrayList<>();
                for (int i = 0, limit = Array.getLength(array); i < limit; i ++) {
                    Object arrayElement = Array.get(array, i);
                    flatList.add(asValueStr(arrayElement));
                }

                return TextUtils.join(",", flatList);
            }

            private String flattenCollection(Collection<?> coll) {
                List<String> flatList = new ArrayList<>();
                for (Object value : coll) {
                    flatList.add(asValueStr(value));
                }

                return TextUtils.join(",", flatList);
            }

            private String asValueStr(Object value) {
                return null == value ? "" : value.toString();
            }
        });

        return values;
    }
}
