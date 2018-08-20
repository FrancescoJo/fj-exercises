/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mediamonkey.android.lib.util.DataStorable;

import java.util.List;

import static com.mediamonkey.android.app.service.datastore.OnGoingWhereImpl.composeWhereClause;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
class SQLDeleteHelper {
    static <T extends DataStorable> String sqlDelete(final T value,
                                                     @Nullable final List<Where<?>> conditions) {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM `").append(value.getClass().getSimpleName()).append("`");
        String whereClause = composeWhereClause(value, conditions);
        if (!TextUtils.isEmpty(whereClause)) {
            sb.append(" ").append(whereClause);
        }
        return sb.append(";").toString();
    }
}
