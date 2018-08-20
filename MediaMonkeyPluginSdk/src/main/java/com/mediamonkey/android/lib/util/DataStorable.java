/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Classes implementing this interface must implement a outside-world visible
 * single <code>android.database.Cursor</code> argument taking constructor. For example:
 *
 * <pre><code>
 * public class MyValueObject implements DataStorable {
 *     &#64;DataStorable.Value(dataType = DataType.INTEGER, keyType = KeyType.PRIMARY)
 *     private long id;
 *
 *     &#64;DataStorable.Value(dataType = DataType.TEXT)
 *     private String value;
 *
 *     // Accessor MUST BE package level at least
 *     public MyValueObject(Cursor cursor) {
 *         this.id    = cursor.getLong(cursor.getColumnIndex("myValueObject_id"));
 *         this.value = cursor.getString(cursor.getColumnIndex("myValueObject_value"));
 *     }
 * }
 * </code></pre>
 *
 * Also, a DataStorable must has a field for PRIMARY KEY for operation,
 * as denoted in example code above.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Dec - 2016
 */
@SuppressWarnings("JavaDoc")
public interface DataStorable {
    @Retention(RetentionPolicy.RUNTIME)
    @interface Value {
        DataType dataType();

        KeyType keyType() default KeyType.NO_KEY;

        String reference() default "";
    }

    enum DataType {
        /**
         * Sqlite INTEGER type, which corresponds to Java <code>int</code> or <code>long</code>.
         */
        INTEGER("INTEGER"),
        /**
         * Sqlite TEXT type, which corresponds to Java <code>String</code>.
         */
        TEXT("TEXT"),
        /**
         * Sqlite TEXT type, which corresponds to Java <code>float</code> or <code>double</code>.
         */
        REAL("REAL");

        public final String value;

        DataType(String s1) {
            this.value = s1;
        }
    }

    enum KeyType {
        /**
         * Field noted as PRIMARY or PRIMARY_ROWID must be only one in schema.
         */
        PRIMARY,
        /**
         * Field noted as PRIMARY or PRIMARY_ROWID must be only one in schema.
         * Data type of field noted as PRIMARY_ROWID must be INTEGER.
         */
        PRIMARY_ROWID,
        /**
         * Data type of field noted as FOREIGN must be INTEGER.
         */
        FOREIGN,
        /**
         * Denoting field as ordinary value column.
         */
        NO_KEY;
    }
}
