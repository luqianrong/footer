package com.example.footer.model;

import java.io.Serializable;

/**
 * Created by 乃军 on 2017/12/14.
 */

public class Comment implements Serializable {
    public String unreadId;
    public String id;
    public String userPhone;
    public String userIcon;
    public String nickName;
    public String createTime;
    public String userComment;
    public String reUserPhone;
    public String reUserIcon;
    public String reNickName;
    public String spoorId;

    public String getUnreadId() {
        return unreadId;
    }

    public String getId() {
        return id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public String getNickName() {
        return nickName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUserComment() {
        return userComment;
    }

    public String getReUserPhone() {
        return reUserPhone;
    }

    public String getReUserIcon() {
        return reUserIcon;
    }

    public String getReNickName() {
        return reNickName;
    }

    public String getSpoorId() {
        return spoorId;
    }

    public void setSpoorId(String spoorId) {
        this.spoorId = spoorId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", nickName='" + nickName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", userComment='" + userComment + '\'' +
                ", reUserPhone='" + reUserPhone + '\'' +
                ", reUserIcon='" + reUserIcon + '\'' +
                ", reNickName='" + reNickName + '\'' +
                ", spoorId='" + spoorId + '\'' +
                '}';
    }
}
