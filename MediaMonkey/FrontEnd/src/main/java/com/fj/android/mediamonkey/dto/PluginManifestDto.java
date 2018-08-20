/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.dto;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.text.TextUtils;

import com.fj.android.mediamonkey.BuildConfig;
import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.util.UiUtils;
import com.mediamonkey.android.lib.dto.VersionDto;
import com.mediamonkey.android.lib.internal.HashCodeExclude;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Nov - 2016
 */
public class PluginManifestDto implements Parcelable {
    public static final String FILE_NAME_PKG_INFO  = "pkg-info.xml";

    private static final String XML_TAG_ROOT = "mmplugin";

    private File         pkgFile;
    private String       sha1Hash;
    private VersionDto   version;
    private String       name;
    private String       packageName;
    private String       author;
    private String       authorEmail;
    private Date         creationDate;
    private String       description;

    @HashCodeExclude
    private Spanned descriptionText;

    private PluginManifestDto(Parcel in) {
        this.pkgFile = new File(in.readString());
        this.sha1Hash = in.readString();
        this.version = in.readParcelable(VersionDto.class.getClassLoader());
        this.name = in.readString();
        this.packageName = in.readString();
        this.author = in.readString();
        this.authorEmail = in.readString();
        this.creationDate = new Date(in.readLong());
        this.description = in.readString();
        this.descriptionText = UiUtils.fromHtml(description);
    }

    private PluginManifestDto() { }

    public File getPkgFile() {
        return pkgFile;
    }

    public String getSha1Hash() {
        return sha1Hash;
    }

    public VersionDto getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public @Nullable String getAuthor() {
        return author;
    }

    public @Nullable String getAuthorEmail() {
        return authorEmail;
    }

    public @Nullable Date getCreationDate() {
        return creationDate;
    }

    public @Nullable CharSequence getDescription() {
        return descriptionText;
    }

    public CharSequence[] asInfoList() {
        List<CharSequence> infoList = new ArrayList<>();
        Context context = MediaMonkeyApplication.getInstance();
        infoList.add(context.getString(R.string.msg_file_hash, sha1Hash));
        infoList.add(context.getString(R.string.msg_plugin_name, name));
        infoList.add(context.getString(R.string.msg_plugin_package, packageName));
        infoList.add(context.getString(R.string.msg_plugin_version, version));
        asInfoListInternal(infoList, R.string.msg_plugin_author, author);
        asInfoListInternal(infoList, R.string.msg_plugin_author_email, authorEmail);
        if (null != creationDate) {
            asInfoListInternal(infoList, R.string.msg_plugin_date,
                    DateFormat.getDateInstance(DateFormat.LONG).format(creationDate));
        }
        asInfoListInternal(infoList, R.string.msg_plugin_description, descriptionText);
        return infoList.toArray(new CharSequence[infoList.size()]);
    }

    private void asInfoListInternal(List<CharSequence> infoList, int message, CharSequence text) {
        Context context = MediaMonkeyApplication.getInstance();
        if (!TextUtils.isEmpty(text)) {
            infoList.add(context.getString(message, text));
        }
    }

    public static PluginManifestDto create(InputStream xmlInputStream) throws
            IllegalArgumentException, IOException {
        return create(null, null, xmlInputStream);
    }

    public static PluginManifestDto create(File file, String sha1Hash, InputStream xmlInputStream) throws
            IllegalArgumentException, IOException {
        PluginManifestDto dto = new PluginManifestDto();
        dto.pkgFile = file;
        dto.sha1Hash = sha1Hash;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(xmlInputStream);
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            throw new IllegalArgumentException("Malformed " + FILE_NAME_PKG_INFO + " in " + file);
        }

        NodeList nList = doc.getElementsByTagName(XML_TAG_ROOT);
        Node rootNode = nList.item(0);
        if (null == rootNode) {
            throw new IllegalArgumentException("Root node " + XML_TAG_ROOT + " is not found in " + FILE_NAME_PKG_INFO);
        }
        Element elmtVersion = getElementByName(rootNode, "version");
        dto.version = parseVersion(elmtVersion);
        dto.name = getValueAsString(getElementByName(rootNode, "name"));
        dto.packageName = getValueAsString(getElementByName(rootNode, "package"));
        dto.author = getValueAsString(getElementByName(rootNode, "author"));
        dto.authorEmail = getValueAsString(getElementByName(rootNode, "email"));
        if (null == file) {
            dto.creationDate = new Date(0);
        } else {
            dto.creationDate = getValueAsDate(getElementByName(rootNode, "date"), "yyyy-MM-dd", file.lastModified());
        }
        dto.description = getValueAsString(getElementByName(rootNode, "description"));
        dto.descriptionText = UiUtils.fromHtml(dto.description);
        return dto;
    }

    private static Element getElementByName(Node node, String nodeName) {
        if (null == node) {
            throw new IllegalArgumentException("Node name with " + nodeName + " not found");
        } else if (Node.ELEMENT_NODE != node.getNodeType()) {
            throw new IllegalArgumentException(nodeName + " is not a XML element");
        }

        NodeList nl = node.getChildNodes();
        for (int i = 0, limit = nl.getLength(); i < limit; i++) {
            Node current = nl.item(i);
            if (Node.ELEMENT_NODE == current.getNodeType() && current.getNodeName().equals(nodeName)) {
                return (Element) current;
            }
        }

        throw new IllegalArgumentException(nodeName + " is not found");
    }

    private static VersionDto parseVersion(Element versionElement) {
        int major = getValueAsInt(getElementByName(versionElement, "major"));
        int minor = getValueAsInt(getElementByName(versionElement, "minor"));
        int build = getValueAsInt(getElementByName(versionElement, "build"));
        String name = getValueAsString(getElementByName(versionElement, "name"));

        return new VersionDto.Builder()
                .majorVer(major)
                .minorVer(minor)
                .buildNumber(build)
                .versionName(name)
                .create();
    }

    private static int getValueAsInt(Element element) {
        String value = getValueAsString(element);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value of " + getFullName(element) + " does not contains Integer");
        }
    }

    private static String getValueAsString(Element element) {
        return element.getTextContent();
    }

    private static Date getValueAsDate(Element element, String dateFormat, long defaultTimeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        String value = getValueAsString(element);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            return new Date(defaultTimeStamp);
        }
    }

    private static String getFullName(Element element) {
        Stack<String> nameStack = new Stack<>();
        nameStack.push(element.getNodeName());
        Node current = element;
        int length = nameStack.peek().length();
        while (!(current = current.getParentNode()).getNodeName().equals("#document")) {
            String nodeName = current.getNodeName();
            nameStack.push(nodeName);
            length += nodeName.length() + 1;
        }

        StringBuilder sb = new StringBuilder(length);
        while (nameStack.size() > 1) {
            sb.append(nameStack.pop()).append(".");
        }
        return sb.append(nameStack.pop()).toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pkgFile.getAbsolutePath());
        dest.writeString(sha1Hash);
        dest.writeParcelable(version, flags);
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeString(author);
        dest.writeString(authorEmail);
        dest.writeLong(creationDate.getTime());
        dest.writeString(description);
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return getClass().getCanonicalName() +
                    "@" + hashCode() + "[\n" +
                    "  pkgFile     =" + pkgFile + "\n" +
                    "  sha1Hash    =" + sha1Hash + "\n" +
                    "  version     =" + version + "\n" +
                    "  name        =" + name + "\n" +
                    "  packageName =" + packageName + "\n" +
                    "  author      =" + author + "\n" +
                    "  authorEmail =" + authorEmail + "\n" +
                    "  creationDate=" + creationDate + "\n" +
                    "  description =" + description + "\n" +
                    "]";
        } else {
            return super.toString();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PluginManifestDto> CREATOR = new Creator<PluginManifestDto>() {
        @Override
        public PluginManifestDto createFromParcel(Parcel in) {
            return new PluginManifestDto(in);
        }

        @Override
        public PluginManifestDto[] newArray(int size) {
            return new PluginManifestDto[size];
        }
    };
}
