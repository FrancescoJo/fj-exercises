/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.helper;

import com.francescojo.simplehttpsvr.exception.NotFoundException;
import com.francescojo.simplehttpsvr.model.ApplicationConfig;
import com.francescojo.simplehttpsvr.model.FileItem;
import com.francescojo.simplehttpsvr.util.EmptyCheckUtils;
import com.francescojo.simplehttpsvr.util.MimeUtils;
import com.francescojo.simplehttpsvr.util.NullSafeUtils;
import com.francescojo.simplehttpsvr.util.StringPatternUtils;
import com.google.common.base.Strings;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Oct - 2016
 */
public class FileListHelper {
    private final File rootPath;

    public FileListHelper(File rootPath) {
        this.rootPath = rootPath;
    }

    public List<FileItem> listFiles(String path, ApplicationConfig config) {
        File   absoluteFile = getAbsoluteFile(path);
        File[] files        = absoluteFile.listFiles();

        if (EmptyCheckUtils.isEmpty((Object[]) files)) {
            return new ArrayList<>();
        }

        List<FileItem> fileList = Arrays.stream(files).filter(file -> {
            if (EmptyCheckUtils.isEmpty(config.getNameFilter())) {
                return true;
            }

            for (String filter : config.getNameFilter()) {
                String  filterRegex = StringPatternUtils.wildcard2Regex(filter);
                Pattern p           = Pattern.compile(filterRegex);
                Matcher m           = p.matcher(file.getName());
                if (m.find()) {
                    return false;
                }
            }

            return true;
        }).sorted((o1, o2) -> {
            if (o1.isDirectory() && o2.isDirectory()) {
                return 0;
            } else if (o1.isDirectory() && !o2.isDirectory()) {
                return -1;
            } else {
                return 1;
            }
        }).map(file -> {
            String icon = getIconName(file);
            String name = file.isDirectory() ? file.getName() + "/" : file.getName();
            return new FileItem.Builder()
                        .iconName(icon)
                        .name(name)
                        .path(name)
                        .lastModified(file.lastModified())
                        .size(file.length())
                        .description(NullSafeUtils.get(config.getDescriptionMap().get(file.getName()), ""))
                        .locale(LocaleContextHolder.getLocale())
                        .build();
        }).collect(Collectors.toList());

        if (!absoluteFile.getPath().equals(rootPath.getPath())) {
            File   parent       = absoluteFile.getParentFile();
            String toParentPath = parent.getPath().substring(rootPath.getPath().length());
            if (Strings.isNullOrEmpty(toParentPath)) {
                toParentPath = "/";
            }

            fileList.add(0, new FileItem.Builder()
                    .iconName(getIconName(new File("..")))
                    .name("Parent directory")
                    .path(toParentPath)
                    .lastModified(parent.lastModified())
                    .size(parent.length())
                    .description("")
                    .locale(LocaleContextHolder.getLocale())
                    .build());
        }

        return fileList;
    }

    public File getAbsoluteFile(String relativePath) {
        return new File(rootPath, relativePath);
    }

    public ResponseEntity<InputStreamResource> sendRequestedFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new NotFoundException("Requested resource " + fileName + " is not found.");
        }

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        respHeaders.setContentLength(file.length());
        respHeaders.setContentDispositionFormData("attachment", file.getName());

        InputStreamResource isr;
        try {
            isr = new InputStreamResource(new FileInputStream(file));
            return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            // Will happen in here VERY RARELY, but...
            throw new NotFoundException("Requested resource " + fileName + " is not found.");
        }
    }

    private static String getIconName(File file) {
        NameToIconStrategy iconInfo = NameToIconStrategy.guess(file);
        return iconInfo.icon;
    }

    /*default*/ enum NameToIconStrategy {
        DIR("_ic_folder.png"),
        DIR_PARENT("_ic_folder_parent.png"),
        FILE("_ic_file.png"),
        FILE_EXECUTABLE("ic_executable.png"),
        FILE_FONT("ic_font.png"),
        FILE_HIDDEN("_ic_hidden.png"),
        FILE_AUDIO("ic_audio.png"),
        FILE_IMAGE("ic_image.png"),
        FILE_TEXT("ic_text.png"),
        FILE_VIDEO("ic_video.png"),
        FILE_PACKAGE("ic_package.png")
        ;

        /*default*/ final String icon;

        private static final String N_PARENT = "..";
        private static final String N_HIDDEN = ".";

        NameToIconStrategy(String icon) {
            this.icon = icon;
        }

        /*default*/ static NameToIconStrategy guess(File file) {
            String fileName = file.getName();
            if (N_PARENT.equals(fileName)) {
                return DIR_PARENT;
            } else if (file.isDirectory()) {
                return DIR;
            } else if (fileName.startsWith(N_HIDDEN) || file.isHidden()) {
                return FILE_HIDDEN;
            }

            String mimeType = null;
            try {
                mimeType = Files.probeContentType(file.toPath());
            } catch (IOException ignore) {
            }

            if (Strings.isNullOrEmpty(mimeType)) {
                mimeType = MimeUtils.guessMimeTypeFromExtension(getFileExtension(file.getName()));
            }

            if (Strings.isNullOrEmpty(mimeType)) {
                return FILE;
            }

            if (mimeType.startsWith("audio")) {
                return FILE_AUDIO;
            } else if (mimeType.startsWith("image")) {
                return FILE_IMAGE;
            } else if (mimeType.startsWith("text")) {
                return FILE_TEXT;
            } else if (mimeType.startsWith("video")) {
                return FILE_VIDEO;
            }

            if (mimeType.contains("font")) {
                return FILE_FONT;
            } else if (mimeType.contains("package")) {
                return FILE_PACKAGE;
            }

            if (file.canExecute()) {
                return FILE_EXECUTABLE;
            }

            return FILE;
        }

        private static String getFileExtension(String fileName) {
            if (!fileName.contains(".") || fileName.startsWith(".")) {
                return "";
            }

            int periodIndex = fileName.indexOf(".");
            if (periodIndex + 1 == fileName.length()) {
                return "";
            }

            return fileName.substring(periodIndex + 1);
        }
    }
}
