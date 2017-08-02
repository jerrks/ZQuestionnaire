package com.questionnaire.content;

import android.graphics.Bitmap;

import java.io.File;

/**
 * 多媒体文件显示信息实体，用于列表显示
 * Created by zhanghao on 2017/7/27.
 */

public class MediaInfoItem {
    String name;
    String mediaType;//one of MediaManager.TYPE_*
    long date;
    long length;
    String fileSise;
    String filePath;
    String description;
    Bitmap thumbnail;

    public MediaInfoItem(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setFileSise(String fileSise) {
        this.fileSise = fileSise;
    }

    public String getFileSise() {
        return this.fileSise;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    @Override
    public String toString() {
        return "AudioItem{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", fileSise=" + fileSise +
                ", filePath='" + filePath + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static MediaInfoItem fromFile(File file, String mediaType) {
        MediaInfoItem it = new MediaInfoItem(mediaType);
        it.name = file.getName();
        it.name = file.getName();
        it.length = file.length();
        it.fileSise = MediaManager.formatFileSize(it.length);
        it.date = file.lastModified();
        it.filePath = file.getPath();
        it.description = file.getAbsolutePath();
        it.thumbnail = MediaManager.extractThumbnail(it.filePath, mediaType);
        return it;
    }

    public static MediaInfoItem fromFile(String filePath, String mediaType) {
        File file = new File(filePath);
        return fromFile(file, mediaType);
    }
}
