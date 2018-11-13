package com.it.yk.find_component.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by yangk on 2018/11/1.
 */

public class FindEntity implements MultiItemEntity {
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private String content;
    private int itemType;
    private int icon;

    /**
     * 当前布局类型
     */
    public FindEntity(String content,int itemType,int icon) {
        this.content = content;
        this.itemType = itemType;
        this.icon = icon;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
