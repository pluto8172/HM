package com.arch.demo.common.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * ================================================
 *
 * @function 分享数据
 * Created by plout on 2019/04/04.
 * ================================================
 */
public class ShareBean implements Parcelable {
    private String title;// 分享文本
    private String text;// 分享标题
    private String url;// 分享h5链接
    private String imageUrl;// 分享封面图
    private String shareType;// 0-图文分享；1-图片分享

    public ShareBean() {

    }

    public ShareBean(String title, String text, String url, String imageUrl) {
        this.title = title;
        this.text = text;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.text);
        dest.writeString(this.url);
        dest.writeString(this.imageUrl);
        dest.writeString(this.shareType);
    }

    protected ShareBean(Parcel in) {
        this.title = in.readString();
        this.text = in.readString();
        this.url = in.readString();
        this.imageUrl = in.readString();
        this.shareType = in.readString();
    }

    public static final Creator<ShareBean> CREATOR = new Creator<ShareBean>() {
        @Override
        public ShareBean createFromParcel(Parcel source) {
            return new ShareBean(source);
        }

        @Override
        public ShareBean[] newArray(int size) {
            return new ShareBean[size];
        }
    };
}
