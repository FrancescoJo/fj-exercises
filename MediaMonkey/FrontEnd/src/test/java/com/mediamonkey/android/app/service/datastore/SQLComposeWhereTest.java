/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import com.fj.android.mediamonkey._base.AndroidTestBase;
import com.mediamonkey.android.lib.util.DataStorable;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;

import static com.mediamonkey.android.app.service.datastore.OnGoingWhereImpl.composeWhereClause;
import static org.junit.Assert.assertEquals;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Dec - 2016
 */
public class SQLComposeWhereTest extends AndroidTestBase {
    @Test(expected = UnsupportedOperationException.class)
    public void testComposeWhere_noPkWithoutCondition() throws Exception {
        composeWhereClause(new NoPk(), null);
    }

    @Test
    public void testComposeWhere_pkWithoutCondition() throws Exception {
        WithPk data = new WithPk();
        data.field1 = 10;
        data.field2 = "STR";
        data.field3 = Arrays.asList(1L, 2L, 3L);

        String expected = "WHERE (`field1`='10')";
        String actual = composeWhereClause(data, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testComposeWhere_pkWithSingleCondition() throws Exception {
        WithPk data = new WithPk();
        data.field1 = 10;
        data.field2 = "STR";
        data.field3 = Arrays.asList(1L, 2L, 3L);
        data.field4 = 0.1f;

        String expected = "WHERE (`field2`!='STR')";
        @SuppressWarnings("unchecked")
        List<Where<?>> conditions = (List<Where<?>>) Collections.singletonList(new Where<>(
                Conjunction.AND, "field2", Sign.NE, "STR"
        ));
        String actual = composeWhereClause(data, conditions);

        assertEquals(expected, actual);
    }

    @Test
    public void testComposeWhere_pkWithMultipleCondition() throws Exception {
        WithPk data = new WithPk();
        data.field1 = 10;
        data.field2 = "STR";
        data.field3 = Arrays.asList(1L, 2L, 3L);
        data.field4 = 0.1f;

        String expected = "WHERE (`field2`!='STR') OR (`field4`='5.5')";
        @SuppressWarnings("unchecked")
        List<Where<?>> conditions = Arrays.<Where<?>>asList(
                new Where<>(Conjunction.AND, "field2", Sign.NE, "STR"),
                new Where<>(Conjunction.OR, "field4", Sign.EQ, "5.5")
        );
        String actual = composeWhereClause(data, conditions);

        assertEquals(expected, actual);
    }

    @SuppressWarnings("all")
    private static class NoPk implements DataStorable {
        @DataStorable.Value(dataType = DataType.INTEGER)
        private int field1;
    }

    @SuppressWarnings("all")
    private static class WithPk implements DataStorable {
        @DataStorable.Value(dataType = DataType.INTEGER, keyType = KeyType.PRIMARY)
        private int field1;

        @DataStorable.Value(dataType = DataType.TEXT)
        private String field2;

        @DataStorable.Value(dataType = DataType.TEXT)
        private List<Long> field3;

        @DataStorable.Value(dataType = DataType.REAL)
        private double field4;
    }
}
