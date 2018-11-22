package com.it.yk.fwx_chat_component.mvp.model.entity;

/**
 * Created by yangk on 2018/11/16.
 */

public class AllContactsEntity {


    public AllContactsEntity() {
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public AllContactsEntity(boolean isSelect, String name, int icon) {
        this.isSelect = isSelect;
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private boolean isSelect;
    private String name;

    public int getIcon() {
        return icon == 0 ? -1 : icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private int icon;


}
