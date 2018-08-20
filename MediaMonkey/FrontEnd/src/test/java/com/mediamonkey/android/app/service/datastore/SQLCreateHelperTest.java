/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.content.ContentValues;
import android.net.Uri;

import com.fj.android.mediamonkey._base.AndroidTestBase;
import com.mediamonkey.android.lib.dto.contents.Category;
import com.mediamonkey.android.lib.dto.contents.Content;
import com.mediamonkey.android.lib.dto.contents.ThumbnailSpec;

import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;

import static com.mediamonkey.android.app.service.datastore.SQLCreateHelper.sqlCreate;
import static com.mediamonkey.android.app.service.datastore.SQLCreateHelper.sqlInsertValues;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
public class SQLCreateHelperTest extends AndroidTestBase {
    @Test
    public void testSqlCreate_withRowId() throws Exception {
        String expected = "CREATE TABLE `ThumbnailSpec` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`width` REAL," +
                "`height` REAL," +
                "`thumbnailUri` TEXT," +
                "`payload` TEXT);";
        String actual = sqlCreate(ThumbnailSpec.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testSqlCreate_withoutRowId() throws Exception {
        String expected = "CREATE TABLE `Content` (" +
                "`id` INTEGER PRIMARY KEY," +
                "`categoryIdList` TEXT," +
                "`title` TEXT," +
                "`thumbnailSpec` INTEGER," +
                "`author` INTEGER," +
                "`accessUri` TEXT," +
                "`lastAccessDate` INTEGER," +
                "`browseCount` INTEGER," +
                "`description` TEXT," +
                "`bookmarked` INTEGER," +
                "`payload` TEXT) WITHOUT ROWID;";
        String actual = sqlCreate(Content.class);

        assertEquals(expected, actual);
    }

    @Test
    public void sqlInsertValues_usePrimaryRowId() throws Exception {
        ThumbnailSpec thumbnail = new ThumbnailSpec.Builder()
                .id(9999L)
                .thumbnailUri(Uri.parse("file:///dev/null"))
                .width(1.0f)
                .height(1.0f)
                .create();

        ContentValues values = sqlInsertValues(thumbnail);
        assertEquals((double) values.getAsFloat("width"), (double) 1.0f, 0.0f);
        assertEquals((double) values.getAsFloat("height"), (double) 1.0f, 0.0f);
        assertEquals(values.getAsString("thumbnailUri"), "file:///dev/null");
        assertNull(values.get("id"));
    }

    @Test
    public void sqlInsertValues_useForeign() throws Exception {
        Category category = new Category.Builder()
                .id(9999L)
                .title("TITLE")
                .thumbnailSpec(new ThumbnailSpec.Builder()
                    .id(10000L).create())
                .create();

        ContentValues values = sqlInsertValues(category);
        assertEquals((long) values.getAsLong("id"), 9999L);
        assertEquals(values.getAsString("title"), "TITLE");
        assertEquals((long) values.getAsLong("thumbnailSpec"), 10000L);
    }


    @Test
    public void sqlInsertValues_withCollection() throws Exception {
        @SuppressWarnings("unchecked")
        Content category = new Content.Builder()
                .id(1L)
                .categoryIdList(Arrays.asList(new Long[] { 99L, 100L }))
                .create();

        ContentValues values = sqlInsertValues(category);
        assertEquals((long) values.getAsLong("id"), 1L);
        assertEquals(values.getAsString("categoryIdList"), "99,100");
    }
}
