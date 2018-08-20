/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.net.Uri;

import com.fj.android.mediamonkey._base.AndroidTestBase;
import com.mediamonkey.android.lib.dto.contents.Category;
import com.mediamonkey.android.lib.dto.contents.ThumbnailSpec;
import com.mediamonkey.android.lib.util.DataStorable;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mediamonkey.android.app.service.datastore.SQLSelectHelper.sqlSelect;
import static com.mediamonkey.android.app.service.datastore.SQLSelectHelper.sqlSelectCount;
import static org.junit.Assert.assertEquals;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
public class SqlSelectHelperTest extends AndroidTestBase {
    @Test
    public void testSqlSelectCount_one() throws Exception {
        DataStorable thumbnail = new ThumbnailSpec.Builder()
                .id(10L)
                .thumbnailUri(Uri.parse("file:///dev/null"))
                .width(1.0f)
                .height(1.0f)
                .create();

        String expected = "SELECT COUNT(*) FROM `ThumbnailSpec` WHERE (`id`='10');";
        String actual = sqlSelectCount(thumbnail, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testSqlSelectCount_noPkValue() throws Exception {
        DataStorable thumbnail = new ThumbnailSpec.Builder()
                .id(-1L)
                .thumbnailUri(Uri.parse("file:///dev/null"))
                .width(1.0f)
                .height(1.0f)
                .create();

        String expected = "SELECT COUNT(*) FROM `ThumbnailSpec`;";
        String actual = sqlSelectCount(thumbnail, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testSqlSelect_join() throws Exception {
        String expected = "SELECT `Category`.`id` AS 'Category_id'," +
                "`Category`.`title` AS 'Category_title'," +
                "`Category`.`thumbnailSpec` AS 'Category_thumbnailSpec'," +
                "`Category`.`lastAccessDate` AS 'Category_lastAccessDate'," +
                "`Category`.`browseCount` AS 'Category_browseCount'," +
                "`Category`.`bookmarked` AS 'Category_bookmarked'," +
                "`Category`.`description` AS 'Category_description'," +
                "`Category`.`payload` AS 'Category_payload'," +
                "`ThumbnailSpec`.`id` AS 'ThumbnailSpec_id'," +
                "`ThumbnailSpec`.`width` AS 'ThumbnailSpec_width'," +
                "`ThumbnailSpec`.`height` AS 'ThumbnailSpec_height'," +
                "`ThumbnailSpec`.`thumbnailUri` AS 'ThumbnailSpec_thumbnailUri'," +
                "`ThumbnailSpec`.`payload` AS 'ThumbnailSpec_payload' " +
                "FROM `Category` Category " +
                "LEFT JOIN `ThumbnailSpec` ThumbnailSpec " +
                "ON `Category`.`thumbnailSpec` = `ThumbnailSpec`.`id`;";
        String actual = sqlSelect(Category.class, null, null, null, 0, -1);

        assertEquals(expected, actual);
    }

    @Test
    public void testSqlSelect() throws Exception {
        String expected = "SELECT `Category`.`id` AS 'Category_id'," +
                "`Category`.`title` AS 'Category_title'," +
                "`Category`.`thumbnailSpec` AS 'Category_thumbnailSpec'," +
                "`Category`.`lastAccessDate` AS 'Category_lastAccessDate'," +
                "`Category`.`browseCount` AS 'Category_browseCount'," +
                "`Category`.`bookmarked` AS 'Category_bookmarked'," +
                "`Category`.`description` AS 'Category_description'," +
                "`Category`.`payload` AS 'Category_payload'," +
                "`ThumbnailSpec`.`id` AS 'ThumbnailSpec_id'," +
                "`ThumbnailSpec`.`width` AS 'ThumbnailSpec_width'," +
                "`ThumbnailSpec`.`height` AS 'ThumbnailSpec_height'," +
                "`ThumbnailSpec`.`thumbnailUri` AS 'ThumbnailSpec_thumbnailUri'," +
                "`ThumbnailSpec`.`payload` AS 'ThumbnailSpec_payload' " +
                "FROM `Category` Category " +
                "LEFT JOIN `ThumbnailSpec` ThumbnailSpec " +
                "ON `Category`.`thumbnailSpec` = `ThumbnailSpec`.`id` " +
                "WHERE (`deleted`!='1') " +
                "ORDER BY `title` DESC LIMIT 0, 30;";
        List<Where<?>> wheres = new ArrayList<>();
        wheres.add(new Where<>("deleted", Sign.NE, true));
        String actual = sqlSelect(Category.class, null, wheres, new Order("title", Order.DESCENDING), 30, 0);

        assertEquals(expected, actual);
    }
}
