package com.it.yk.fwx_chat_component.mvp.model.entity;

/**
 * Created by yangk on 2018/11/19.
 */

public class GroupListEntity {

    //群组ID
    String groupID;
    //群组名称
    String GtoupName;

    public GroupListEntity(String group_Id,String group_name){
        this.groupID =group_Id;
        this.GtoupName = group_name;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGtoupName() {
        return GtoupName;
    }

    public void setGtoupName(String gtoupName) {
        GtoupName = gtoupName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    //创建时间
    String createTime;

}
