package com.wl.pluto.arch.base.bean;

public class BaseTabBean {
    public int tabId;
    public String tabName;
    public int tabContent;

    public BaseTabBean(int tabId, String tabName, int content) {
        this.tabId = tabId;
        this.tabName = tabName;
        this.tabContent = content;
    }
}
