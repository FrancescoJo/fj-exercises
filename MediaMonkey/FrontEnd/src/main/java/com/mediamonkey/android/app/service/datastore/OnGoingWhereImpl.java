/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fj.android.mediamonkey.util.lang.CollectionUtils;
import com.fj.android.mediamonkey.util.lang.EmptyCheckUtils;
import com.mediamonkey.android.app.service.DataStoreService;
import com.mediamonkey.android.lib.util.DataStorable;
import com.mediamonkey.android.lib.internal.Objects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mediamonkey.android.app.service.datastore.SQLBaseHelper.getPrimaryKeyName;
import static com.mediamonkey.android.app.service.datastore.SQLBaseHelper.getPrimaryValue;
import static com.mediamonkey.android.app.service.datastore.SQLBaseHelper.valueAsSQL;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
abstract class OnGoingWhereImpl<T extends DataStorable> implements DataStoreService.OnGoingWhere<T> {
    private final SQLiteDatabase db;
    private List<Where<?>> conditions;

    OnGoingWhereImpl(final SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public DataStoreService.OnGoingWhere<T> where(final Where<?> where) {
        if (null == conditions) {
            conditions = new ArrayList<>();
        }
        conditions.add(where);
        return this;
    }

    SQLiteDatabase getDatabase() {
        return db;
    }

    @Nullable
    protected List<Where<?>> getConditions() {
        return conditions;
    }

    static <T extends DataStorable> String composeWhereClause(final T value, final List<Where<?>> conditions) {
        if (CollectionUtils.size(conditions) == 1 && conditions.get(0) == Where.EMPTY) {
            return "";
        }

        List<Where<?>> whereList;
        if (null == conditions) {
            String pk = getPrimaryKeyName(value);
            if (TextUtils.isEmpty(pk)) {
                throw new UnsupportedOperationException("No conditions without any primary key in: " + value.getClass().getSimpleName());
            }
            Object pkValue = getPrimaryValue(value, pk);
            if (null == pkValue || Objects.equals(pkValue, -1) || Objects.equals(pkValue, -1L)) {
                return "";
            }
            whereList = new ArrayList<>();
            whereList.add(new Where<>(pk, Sign.EQ, pkValue));
        } else {
            whereList = conditions;
        }

        return composeWhereClause(whereList);
    }

    static String composeWhereClause(List<Where<?>> conditions) {
        if (EmptyCheckUtils.isEmpty(conditions)) {
            return "";
        }

        StringBuilder sb = new StringBuilder().append("WHERE ");
        List<String> clauses = new ArrayList<>();
        for (int i = 0, limit = conditions.size(); i < limit; i++) {
            Where where = conditions.get(i);
            if (where == Where.EMPTY) {
                continue;
            }

            StringBuilder sb2 = new StringBuilder();
            if (isConjunctionApplicable(i, where.conjunction)) {
                sb2.append(where.conjunction.value).append(" ");
            }
            sb2.append("(");
            if (isRhsMultipleValues(where.rhs)) {
                sb2.append(flattenMultipleValues(where.lhs, where.sign, where.rhs));
            } else {
                sb2.append("`").append(where.lhs).append("`").append(where.sign.value).append(valueAsSQL(where.rhs));
            }
            sb2.append(")");
            clauses.add(sb2.toString());
        }

        sb.append(TextUtils.join(" ", clauses));
        return sb.toString();
    }

    private static boolean isConjunctionApplicable(final int i, final Conjunction conjunction) {
        return 0 != i && null != conjunction;
    }

    private static boolean isRhsMultipleValues(final Object rhsValue) {
        return rhsValue.getClass().isArray() || rhsValue instanceof Collection;
    }

    private static String flattenMultipleValues(final String lhs, final Sign sign, final Object rhs) {
        List<String> conditions = new ArrayList<>();
        if (rhs.getClass().isArray()) {
            for (int i = 0, limit = Array.getLength(rhs); i < limit; i++) {
                Object value = Array.get(rhs, i);
                conditions.add("(`" + lhs + "`" + sign.value + valueAsSQL(value) + ")");
            }
        } else {
            Collection col = (Collection) rhs;
            for (Object value : col) {
                conditions.add("(`" + lhs + "`" + sign.value + valueAsSQL(value) + ")");
            }
        }
        return "(" + TextUtils.join(" OR ", conditions) + ")";
    }
}
