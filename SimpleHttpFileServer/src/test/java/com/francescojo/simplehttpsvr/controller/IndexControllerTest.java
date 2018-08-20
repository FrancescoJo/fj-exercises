/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.controller;

import com.francescojo.simplehttpsvr.exception.NotFoundException;
import com.francescojo.simplehttpsvr.helper.FileListHelper;
import com.francescojo.simplehttpsvr.model.ApplicationConfig;
import com.francescojo.simplehttpsvr.model.FileItem;
import com.francescojo.simplehttpsvr.util.PathUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.francescojo.simplehttpsvr.controller.IndexController.SECRET_DOWNLOAD_PARAM_FILE;
import static com.francescojo.simplehttpsvr.controller.IndexController.SECRET_DOWNLOAD_URL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Nov - 2016
 */
public class IndexControllerTest {
    private FileListHelper    mockFileHelper;
    private ApplicationConfig mockAppConfig;
    private IndexController   controller;

    @Before
    public void setUp() throws Exception {
        this.mockFileHelper = mock(FileListHelper.class);
        this.mockAppConfig = mock(ApplicationConfig.class);
        this.controller = new IndexController(mockFileHelper, mockAppConfig);
    }

    @Test
    public void testIndex() throws Exception {
        String           path     = "";
        List<FileItem>   fileList = new ArrayList<>();
        ExtendedModelMap model    = new ExtendedModelMap();
        when(mockFileHelper.getAbsoluteFile(any())).thenReturn(new File(""));
        when(mockFileHelper.listFiles(path, mockAppConfig)).thenReturn(fileList);

        String pageName = controller.index(path, model);

        assertEquals(model.get("homedir"), PathUtils.getRelativePathOf(path));
        assertEquals(model.get("files"), fileList);
        assertEquals("index", pageName);
    }

    @Test(expected = NotFoundException.class)
    public void testAccess_notFound() throws Exception {
        TestAccessValues testConfig = testAccessSetup();
        when(testConfig.file.exists()).thenReturn(false);

        String pageName = controller.access(testConfig.model, testConfig.request);
        assertNull(pageName);
    }

    @Test
    public void testAccess_isDirectory() throws Exception {
        TestAccessValues testConfig = testAccessSetup();
        when(testConfig.file.exists()).thenReturn(true);
        when(testConfig.file.isDirectory()).thenReturn(true);

        String pageName = controller.access(testConfig.model, testConfig.request);
        assertEquals(pageName, "index");
    }

    @Test
    public void testAccess_isFile() throws Exception {
        TestAccessValues testConfig = testAccessSetup();
        String           path       = "";
        when(testConfig.file.exists()).thenReturn(true);
        when(testConfig.file.isDirectory()).thenReturn(false);
        when(testConfig.file.getAbsolutePath()).thenReturn(path);

        String pageName = controller.access(testConfig.model, testConfig.request);
        assertEquals(pageName, "forward:/" + SECRET_DOWNLOAD_URL + "?" + SECRET_DOWNLOAD_PARAM_FILE + "=" + path);
    }

    private TestAccessValues testAccessSetup() throws Exception {
        ExtendedModelMap   model   = new ExtendedModelMap();
        HttpServletRequest request = mock(HttpServletRequest.class);
        File               file    = mock(File.class);
        String             path    = "";
        when(request.getServletPath()).thenReturn(path);
        when(mockFileHelper.getAbsoluteFile(path)).thenReturn(file);

        return new TestAccessValues(model, request, file);
    }

    private static class TestAccessValues {
        /*default*/ final ExtendedModelMap   model;
        /*default*/ final HttpServletRequest request;
        /*default*/ final File               file;

        /*default*/ TestAccessValues(ExtendedModelMap arg1, HttpServletRequest arg2, File arg3) {
            this.model = arg1;
            this.request = arg2;
            this.file = arg3;
        }
    }
}
