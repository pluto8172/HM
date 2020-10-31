package com.wl.pluto.arch.base.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dl on 2020-02-27.
 */


public class OpenMedia implements Parcelable {
    /**
     * bizId :
     * cdnPath :
     * createdBy :
     * createdTime : yyyy-MM-dd HH:mm:ss
     * del : 0
     * fileSize : 0
     * fileType : 0
     * id :
     * imageHeight : 0
     * imageWidth : 0
     * newName :
     * originalName :
     * relativePath :
     * updatedBy :
     * updatedTime : yyyy-MM-dd HH:mm:ss
     * version : 0
     */

    private String bizId;
    private String cdnPath;
    private String createdBy;
    private String createdTime;
    private int del;
    private int fileSize;
    private int fileType;
    private String id;
    private int imageHeight;
    private int imageWidth;
    private String newName;
    private String originalName;
    private String relativePath;
    private String updatedBy;
    private String patientRemarkId;
    private String updatedTime;
    private int version;
    private int sort;
    private String url;

    public OpenMedia() {

    }


    protected OpenMedia(Parcel in) {
        bizId = in.readString();
        cdnPath = in.readString();
        createdBy = in.readString();
        createdTime = in.readString();
        del = in.readInt();
        fileSize = in.readInt();
        fileType = in.readInt();
        id = in.readString();
        imageHeight = in.readInt();
        imageWidth = in.readInt();
        newName = in.readString();
        originalName = in.readString();
        relativePath = in.readString();
        updatedBy = in.readString();
        patientRemarkId = in.readString();
        updatedTime = in.readString();
        version = in.readInt();
        sort = in.readInt();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bizId);
        dest.writeString(cdnPath);
        dest.writeString(createdBy);
        dest.writeString(createdTime);
        dest.writeInt(del);
        dest.writeInt(fileSize);
        dest.writeInt(fileType);
        dest.writeString(id);
        dest.writeInt(imageHeight);
        dest.writeInt(imageWidth);
        dest.writeString(newName);
        dest.writeString(originalName);
        dest.writeString(relativePath);
        dest.writeString(updatedBy);
        dest.writeString(patientRemarkId);
        dest.writeString(updatedTime);
        dest.writeInt(version);
        dest.writeInt(sort);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OpenMedia> CREATOR = new Creator<OpenMedia>() {
        @Override
        public OpenMedia createFromParcel(Parcel in) {
            return new OpenMedia(in);
        }

        @Override
        public OpenMedia[] newArray(int size) {
            return new OpenMedia[size];
        }
    };

    public String getPatientRemarkId() {
        return patientRemarkId;
    }

    public void setPatientRemarkId(String patientRemarkId) {
        this.patientRemarkId = patientRemarkId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getCdnPath() {
        return cdnPath;
    }

    public void setCdnPath(String cdnPath) {
        this.cdnPath = cdnPath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
