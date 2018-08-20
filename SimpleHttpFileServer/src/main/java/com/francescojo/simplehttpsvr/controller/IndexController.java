/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.controller;

import com.francescojo.simplehttpsvr.exception.NotFoundException;
import com.francescojo.simplehttpsvr.helper.FileListHelper;
import com.francescojo.simplehttpsvr.interceptor.GlobalsWritingInterceptor;
import com.francescojo.simplehttpsvr.model.ApplicationConfig;
import com.francescojo.simplehttpsvr.model.FileItem;
import com.francescojo.simplehttpsvr.util.NullSafeUtils;
import com.francescojo.simplehttpsvr.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Oct - 2016
 */
@Controller
public class IndexController {
    /*default*/ static final String SECRET_DOWNLOAD_URL = ".simplehttpsvr-internal-secret-download";
    /*default*/ static final String SECRET_DOWNLOAD_PARAM_FILE = "f";
    private static final String STATIC_DOWNLOAD_URL = GlobalsWritingInterceptor.STATIC_DOWNLOAD_PREFIX;
    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    private final FileListHelper    fileHelper;
    private final ApplicationConfig appConfig;

    public IndexController(FileListHelper fileHelper, ApplicationConfig config) {
        this.fileHelper = fileHelper;
        this.appConfig = config;
    }

    @RequestMapping("")
    public String index(String path, Model model) {
        String relativePath = NullSafeUtils.get(path, "");
        LOG.debug("Browsing path: {}", fileHelper.getAbsoluteFile(relativePath));
        List<FileItem> files = fileHelper.listFiles(relativePath, appConfig);

        model.addAttribute("homedir", PathUtils.getRelativePathOf(relativePath));
        model.addAttribute("files", files);

        return "index";
    }

    @RequestMapping(value = {
        "{path:^(?!" + SECRET_DOWNLOAD_URL + "|" + STATIC_DOWNLOAD_URL + ").*$}",
        "{path:^(?!" + SECRET_DOWNLOAD_URL + "|" + STATIC_DOWNLOAD_URL + ").*$}/**"
    })
    public String access(Model model, HttpServletRequest request) {
        String path = request.getServletPath();
        File   f    = fileHelper.getAbsoluteFile(path);
        LOG.debug("Accessing : {} -> {}", path, f.getAbsolutePath());
        if (!f.exists()) {
            throw new NotFoundException("Requested resource " + path + " is not found.");
        }

        if (f.isDirectory()) {
            return index(path, model);
        } else {
            return "forward:/" + SECRET_DOWNLOAD_URL + "?" + SECRET_DOWNLOAD_PARAM_FILE + "=" + f.getAbsolutePath();
        }
    }

    @RequestMapping(value = "/" + SECRET_DOWNLOAD_URL,
                    params = { SECRET_DOWNLOAD_PARAM_FILE })
    @ResponseBody
    public ResponseEntity<InputStreamResource> download(@RequestParam(SECRET_DOWNLOAD_PARAM_FILE) String fileName) {
        LOG.debug("Downloading: {}", fileName);
        return fileHelper.sendRequestedFile(fileName);
    }
}
