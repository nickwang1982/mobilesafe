package com.example.course.mobilesafe.domain;

/**
 * Item bean for Main Adapter
 */
public class ItemBeanInfo {
    public int ItemId;
    public String ItemTitle;
    public int ItemRes;
    public ItemBeanInfo(int itemId, String itemTitle, int itemRes) {
        ItemId = itemId;
        ItemTitle = itemTitle;
        ItemRes = itemRes;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getItemTitle() {
        return ItemTitle;
    }

    public void setItemTitle(String itemTitle) {
        ItemTitle = itemTitle;
    }

    public int getItemRes() {
        return ItemRes;
    }

    public void setItemRes(int itemRes) {
        ItemRes = itemRes;
    }
}
