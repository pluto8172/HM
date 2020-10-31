package com.wl.pluto.arch.model;

import java.util.List;

/**
 * Created by tangyx on 15/10/16.
 *
 */
public class DialogModel {
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content="";

    private String[] items;
    private List<String> list;
    /**
     * 退出是否有动画
     */
    private boolean existAnimation=false;
    /**
     * 点击弹出框其他地方是否消失
     */
    private boolean canceledOnTouchOutside=true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public boolean isExistAnimation() {
        return existAnimation;
    }

    public void setExistAnimation(boolean existAnimation) {
        this.existAnimation = existAnimation;
    }

    public boolean isCanceledOnTouchOutside() {
        return canceledOnTouchOutside;
    }

    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
    }
}
