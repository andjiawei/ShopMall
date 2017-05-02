package com.shopmall.jiawei.shopmall.bean;

/**
 * 导航底部的tab
 */
public class Tab {

    private  int title; //TextView id
    private  int icon; //ImageView id
    private Class fragment; //对应fragment

    public Tab(Class fragment, int title, int icon) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
