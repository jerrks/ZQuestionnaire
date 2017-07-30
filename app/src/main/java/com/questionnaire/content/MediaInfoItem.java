package com.questionnaire.content;

import java.io.File;

/**
 * 多媒体文件显示信息实体，用于列表显示
 * Created by zhanghao on 2017/7/27.
 */

public class MediaInfoItem {
    String name;
    String author;
    long date;
    long fileSise;
    String filePath;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getFileSise() {
        return fileSise;
    }

    public void setFileSise(long fileAise) {
        this.fileSise = fileAise;
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

    @Override
    public String toString() {
        return "AudioItem{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", date=" + date +
                ", fileSise=" + fileSise +
                ", filePath='" + filePath + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static MediaInfoItem fromFile(File file) {
        MediaInfoItem it = new MediaInfoItem();
        it.name = file.getName();
        it.fileSise = file.length();
        it.date = file.lastModified();
        it.filePath = file.getPath();
        it.description = file.getAbsolutePath();
        return it;
    }

    public static MediaInfoItem fromFile(String filePath) {
        File file = new File(filePath);
        return fromFile(file);
    }
}
