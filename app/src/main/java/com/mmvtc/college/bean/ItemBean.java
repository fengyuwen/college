package com.mmvtc.college.bean;

/**
 * Created by Jay on 2015/10/8 0008.
 */
public class ItemBean {

    private int iconId;
    private String iconName;

    public ItemBean() {
    }

    public ItemBean(int iconId, String iconName) {
        this.iconId = iconId;
        this.iconName = iconName;
    }

    public int getIconId() {
        return iconId;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}

