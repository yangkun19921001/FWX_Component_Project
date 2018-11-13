package com.it.yk.contacts_component.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by yangk on 2018/11/4.
 */

public class AddFriendsEntity implements MultiItemEntity {

    public AddFriendsEntity(int itemType,String name, int icon){
        this.itemType = itemType;
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int itemType;
    private String name;

    public int getIcon() {
        return icon == 0?-1:icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private int icon;

    @Override
    public int getItemType() {
        return itemType;
    }
}
