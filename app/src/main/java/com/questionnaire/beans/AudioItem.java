package com.questionnaire.beans;

/**
 * Created by zhanghao on 2017/7/27.
 */

public class AudioItem {
    public String name;
    public String author;
    public long date;
    public long fileAise;
    public String filePath;
    public String description;

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

    public long getFileAise() {
        return fileAise;
    }

    public void setFileAise(long fileAise) {
        this.fileAise = fileAise;
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
                ", fileAise=" + fileAise +
                ", filePath='" + filePath + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
