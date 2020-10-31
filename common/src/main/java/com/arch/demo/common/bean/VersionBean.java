package com.arch.demo.common.bean;


/**
 * ================================================
 *
 * @function 版本数据
 * Created by plout on 2019/02/15.
 * ================================================
 */
public class VersionBean {
    private String appKey;
    private String os;
    private String version;
    private String downloadUrl;
    private Boolean upgrade;

    public String content;
    public String forcedUpdate;


    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Boolean getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(Boolean upgrade) {
        this.upgrade = upgrade;
    }
}
