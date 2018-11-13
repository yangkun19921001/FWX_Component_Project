package com.it.yk.contacts_component.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by yangk on 2018/10/30.
 */

public class ContactsEntity extends BaseIndexPinyinBean implements MultiItemEntity {
    private boolean isTop;

    public ContactsEntity(){}
    public ContactsEntity(String name, int icon,int itemType){
        this.name = name;
        this.icon = icon;
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public int getIcon() {
        return icon == 0?-1:icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private int icon;
    private int itemType;


    @Override
    public String getTarget() {
        return name;
    }

    public boolean isTop() {
        return isTop;
    }

    public ContactsEntity setTop(boolean top) {
        isTop = top;
        return this;
    }


    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
