/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

/**
 * Ordering specifier which is used in {@link com.mediamonkey.android.app.service.datastore.Where} condition object.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
public class Order {
    public static final Order ASCENDING = new Order(null, "ASC");
    public static final Order DESCENDING = new Order(null, "DESC");

    final String columnName;
    final String how;

    /**
     * Constructs an Order specifying object.
     * Default sorting order is {@link Order#ASCENDING}.
     */
    public Order(String columnName) {
        this(columnName, ASCENDING);
    }

    public Order(String columnName, Order order) {
        this(columnName, order.how);
    }

    private Order(String columnName, String how) {
        this.columnName = columnName;
        this.how = how;
    }
}
