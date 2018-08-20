/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.text.TextUtils;

import com.mediamonkey.android.lib.util.Consumer;
import com.mediamonkey.android.lib.util.DataStorable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mmplugin.marumaru.util.EmptyCheckUtils;
import timber.log.Timber;

import static com.mediamonkey.android.app.service.datastore.OnGoingWhereImpl.composeWhereClause;
import static com.mediamonkey.android.app.service.datastore.SQLBaseHelper.forEachValueField;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
class SQLSelectHelper {
    static <T extends DataStorable> String sqlSelectCount(T value, List<Where<?>> conditions) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT COUNT(*) FROM `").append(value.getClass().getSimpleName()).append("`");
        String whereClause = composeWhereClause(value, conditions);
        if (!TextUtils.isEmpty(whereClause)) {
            sb.append(" ").append(whereClause);
        }
        return sb.append(";").toString();
    }

    static <T extends DataStorable> String sqlSelect(Class<T> klass, List<String> fieldExclusions,
                                                     List<Where<?>> conditions, Order order,
                                                     int count, int offset) {
        String tableName = klass.getSimpleName();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        // Columns
        List<String> selections = new ArrayList<>();
        List<String> joinClauses = new ArrayList<>();
        selections.addAll(extractSelectFields(klass, fieldExclusions, joinClauses));
        sb.append(TextUtils.join(",", selections.toArray()));
        // FROM ...
        sb.append(String.format(" FROM `%s` %s", tableName, tableName));

        // LEFT JOIN ...
        if (joinClauses.size() > 0) {
            for (String joinClause : joinClauses) {
                sb.append(" LEFT JOIN ").append(joinClause);
            }
        }

        // WHERE ...
        String whereClause = composeWhereClause(conditions);
        if (!TextUtils.isEmpty(whereClause)) {
            sb.append(" ").append(whereClause);
        }

        // ORDER BY ...
        if (null != order) {
            sb.append(" ORDER BY `").append(order.columnName).append("` ").append(order.how);
        }

        // LIMIT
        if (count > 0) {
            sb.append(" LIMIT ").append(offset);
            if (offset >= 0) {
                sb.append(", ").append(count);
            }
        }

        return sb.append(";").toString();
    }

    private static <T extends DataStorable> List<String> extractSelectFields(final Class<T> klass,
                                                                             final List<String> fieldExclusions,
                                                                             final List<String> joins) {
        final String tableName = klass.getSimpleName();
        final List<String> selections = new ArrayList<>();
        final List<String> nestedSelections = new ArrayList<>();
        Timber.v("[extractSelectFields] Iterating over fields");
        forEachValueField(klass, new Consumer<Field>() {
            @Override
            public void accept(Field field) {
                DataStorable.Value columnInfo = field.getAnnotation(DataStorable.Value.class);
                String columnName = field.getName();
                String convertedName = tableName + "_" + columnName;
                if (!EmptyCheckUtils.isEmpty(fieldExclusions) && fieldExclusions.contains(convertedName)) {
                    return;
                }

                DataStorable.KeyType keyType = columnInfo.keyType();

                if (DataStorable.KeyType.FOREIGN == keyType) {
                    Class<?> fieldType = field.getType();
                    @SuppressWarnings("SuspiciousMethodCalls")
                    boolean isFieldDataStorable = Arrays.asList(fieldType.getInterfaces()).contains(DataStorable.class);
                    if(!isFieldDataStorable) {
                        Timber.w("%s is declared as FOREIGN, but not a DataStorable", field);
                        return;
                    }
                    @SuppressWarnings("unchecked")
                    Class<? extends DataStorable> dataStorableField = (Class<? extends DataStorable>)fieldType;
                    String conjunction = columnInfo.reference();
                    String[] conjArgs = conjunction.split("_");

                    if (conjArgs.length != 2) {
                        Timber.w("Wrong conjunction definition %s", conjunction);
                        Timber.w("                          on %s", field);
                        return;
                    }
                    String conjTable = conjArgs[0];
                    String conjCol = conjArgs[1];
                    joins.add("`" + conjTable + "` " + conjTable + " ON `" + tableName + "`.`" +
                            columnName + "` = `" + conjTable + "`.`" + conjCol + "`");

                    nestedSelections.addAll(extractSelectFields(dataStorableField, fieldExclusions, joins));
                }

                selections.add(String.format("`%s`.`%s` AS '%s'", tableName, columnName, convertedName));
            }
        });

        selections.addAll(nestedSelections);
        return selections;
    }
}
